# 001 · Alta de socio

**Estado:** Implementada · **Repo:** backend · **Rama:** `feature/create-member`

## Contexto

`POST /members` ya existe, pero al crear un socio le regala **un mes de membresía
con precio 0**. El socio recién creado aparece "al día" y el check-in lo deja
entrar sin haber pagado nunca, así que la pantalla de Deudores no se entera.

La decisión es que el alta **no crea membresía**: el socio nace activo pero sin
membresía, y por lo tanto **Deudor**, hasta que se le registre el primer pago. El
estado ya existe: el seed tiene un socio así y `MemberSpecification`, el check-in
y el badge del frontend lo tratan como vencido.

Eso destapa un bug que hoy está latente: `PaymentServiceImpl` asume que la
membresía existe (`memberEntity.getMembership().getExpirationDate()`), así que
cobrarle a un socio sin membresía tira un NPE y responde 500. Hoy le pasa al socio
del seed (DNI 34199827); con este cambio le pasaría a **todo socio nuevo**. Por eso
el arreglo entra en esta misma spec.

## Alcance

**Entra**

- El alta deja de crear membresía.
- El primer pago crea la membresía.
- Un DNI repetido responde **409 Conflict** en lugar de 400.
- El alta responde **201 Created** en lugar de 200.
- Validación de formato del DNI y de campos en blanco.

**No entra**

- **Reactivar un socio dado de baja.** Su DNI sigue ocupado, así que intentar
  darlo de alta de nuevo responde 409. Reactivarlo es otra feature.
- Fecha de alta del socio (`MemberEntity` no la tiene; ver Pendientes).
- Foto del socio.
- La pantalla del frontend: va en su propia spec `001-alta-de-socio` del repo
  `rodi-gym-frontend`.

## Requisitos

- **R1.** Un alta válida crea el socio con `status = true` y **sin membresía**.
- **R2.** La respuesta del alta tiene código **201** y el socio creado en el cuerpo,
  con `expiration_date: null`.
- **R3.** El socio recién creado aparece en la búsqueda con `status=EXPIRED`
  (Deudores) y no con `ACTIVE`.
- **R4.** Si ya existe un socio con ese DNI, **activo o dado de baja**, responde
  **409** y no modifica al existente.
- **R5.** El DNI tiene que estar entre **1.000.000 y 99.999.999** (7 u 8 dígitos).
  Fuera de rango responde 400.
- **R6.** `name`, `last_name` y `phone_number` no pueden estar vacíos **ni tener
  solo espacios**. Si no, responde 400.
- **R7.** Un pago a un socio **sin membresía** la crea con `start_date = hoy`,
  `expiration_date = hoy + 1 mes` y `price` igual al monto cobrado.
- **R8.** El comportamiento de los pagos a socios **con** membresía no cambia: si
  está vencida arranca hoy y, si no, se extiende un mes desde el vencimiento.

## Contrato

### `POST /members`

```json
{
  "id": 40123456,
  "name": "Laura",
  "last_name": "Quiroga",
  "phone_number": "3515550199"
}
```

`id` es el DNI. Es la clave primaria del socio y la usa el check-in.

| Caso | Código | Cuerpo |
|---|---|---|
| Alta correcta | **201** | `MemberResponseDto` |
| Falta un campo, está en blanco o el DNI está fuera de rango | 400 | `ErrorApi`, con los campos en `message` |
| El DNI ya existe | **409** | `ErrorApi` |

Respuesta 201:

```json
{
  "id": 40123456,
  "name": "Laura",
  "last_name": "Quiroga",
  "phone_number": "3515550199",
  "status": true,
  "expiration_date": null
}
```

El frontend identifica el DNI repetido por el **código 409**, no por el texto de
`message`.

### `POST /payments`

El contrato no cambia. Solo deja de responder 500 para un socio sin membresía.

## Diseño

### Alta (`MemberServiceImpl.createMember`)

- Se borra la creación de `MembershipEntity`. El socio se guarda con
  `membership = null`.
- El chequeo de duplicado pasa a tirar una excepción nueva,
  `MemberAlreadyExistsException`, en vez de `IllegalArgumentException`.
- `existsById` busca **cualquier** socio, incluidos los dados de baja. Así tiene
  que ser: el DNI es la clave primaria.

**Por qué el chequeo previo es obligatorio y no un simple `save`:** el id es
asignado a mano, así que Spring Data considera que la entidad **no es nueva** y
`save` hace un `merge`. Sin el chequeo, un alta con un DNI existente **pisaría en
silencio** los datos del socio. La ventana de carrera entre el `existsById` y el
`save` se acepta: hay un único administrador.

### Errores (`ControllerExceptionHandler`)

- Nuevo handler para `MemberAlreadyExistsException` → 409.
- **Descartado:** reutilizar `IllegalArgumentException` para el 409. Ya se usa
  para otros 400 (dar de baja a un socio inactivo) y habría que distinguirlos por
  el texto.

### Validación (`MemberCreateDto`)

- `id`: `@NotNull`, `@Min(1_000_000)` y `@Max(99_999_999)`.
- `name`, `last_name` y `phone_number`: `@NotBlank` en lugar de `@NotEmpty`, que
  deja pasar `"   "`.
- El teléfono no se valida más allá de no estar en blanco. El formato argentino
  varía (con o sin 0 y 15, con característica) y un validador estricto rechazaría
  números reales.

### Controller

- `createMember` devuelve `ResponseEntity.status(HttpStatus.CREATED)`.
- **Descartado:** `ResponseEntity.created(uri)` con header `Location`. El proxy
  del frontend reescribe el prefijo `/api`, así que la URI saldría mal armada, y
  el cliente no la necesita porque ya conoce el DNI.

### Pagos (`PaymentServiceImpl.createPayment`)

- Si `getMembership()` es `null`, se crea una `MembershipEntity` que arranca hoy
  y se vinculan los dos lados (`membership.setMember`, `member.setMembership`),
  igual que hacía el alta. El cascade del socio la persiste.
- Si existe, la lógica queda como está (R8).

## Pruebas

| Requisito | Test |
|---|---|
| R1, R4 (no pisa al existente) | `MemberServiceImplTest` (`@DataJpaTest` + `@Import` del servicio y `MappersConfig`) |
| R2, R4 (409), R5, R6 | `MemberControllerTest` (`@WebMvcTest`, servicio mockeado) |
| R3 | `MemberServiceImplTest`: crear y buscar con `EXPIRED` y con `ACTIVE` |
| R7, R8 | `PaymentServiceImplTest` (`@DataJpaTest` + `@Import`), sobre un socio sin membresía, uno vencido y uno al día |

Los tests de seed existentes no cambian: el seed sigue teniendo su socio sin
membresía.

## Tareas

- [x] `MemberAlreadyExistsException` y su handler 409.
- [x] Validaciones de `MemberCreateDto`.
- [x] `createMember` sin membresía y respondiendo 201.
- [x] `createPayment` crea la membresía si falta.
- [x] Tests de las tres clases.
- [x] `./mvnw test` en verde.
- [x] Actualizar `CLAUDE.md` (endpoints, trampas y pendientes) y pasar esta spec a
  **Implementada**.

## Pendientes

- **Reactivar socios dados de baja.** Hoy el único camino es cobrarles: el pago
  pone `status = true`. Hay que decidir si esa es la forma o si va un endpoint
  propio.
- `MemberEntity` sigue sin **fecha de alta**. Con este cambio tampoco se puede
  inferir de la membresía, porque no se crea en el alta.
- Los mensajes de error siguen en inglés. Unificarlos es trabajo aparte.
