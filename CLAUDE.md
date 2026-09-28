# RODI GYM — Backend

API del sistema de administración de un gimnasio. El cliente es una PWA Angular
(repo `rodi-gym-frontend`) pensada para **el administrador**, no para los socios.

Spring Boot 3.5.2 · Java · H2 en memoria · ModelMapper · springdoc · Lombok

## Comandos

```bash
./mvnw test                  # 58 tests
./mvnw -q -DskipTests compile
```

**No levantar el servidor por iniciativa propia** — lo hace el usuario, para tener
control de los logs. Si hace falta ver una salida concreta, pedírselo.
Checkstyle corre en la fase `validate` con `failsOnError`, así que un problema de
estilo rompe la compilación.

## Endpoints

| Método | Ruta |
|---|---|
| GET/POST | `/members` (búsqueda con `search`, `status`, paginado; el alta responde 201 y 409 si el DNI existe) |
| GET/PUT/DELETE | `/members/{id}` (baja **lógica**) |
| GET | `/members/{id}/attendance?month=aaaa-MM` |
| GET/POST | `/payments` (filtros `search`, `member_id`, `payment_method`, `from`, `to`; cada pago trae `member_name` y `member_last_name`) |
| GET | `/checkins` (filtros `member_id`, `success`, `from`, `to`) |
| POST | `/checkins/{memberId}` |
| PUT | `/config/monthly-price` |

## Convenciones ya establecidas

- **DTOs en snake_case** vía `@JsonProperty`. El cliente los convierte a camelCase
  con un interceptor, así que no hay que mandar camelCase.
- **Búsquedas**: `Specification` + `Pageable` + `PageResponseDto` propio. No se
  devuelve el `Page` de Spring: su JSON no es contrato estable entre versiones.
- Los filtros opcionales devuelven **predicado `null`** cuando no se informan;
  Spring Data los descarta al combinar. Se combinan con `Specification.allOf`,
  **no** con `where()`, que está deprecado en esta versión.
- **Rangos de fecha semiabiertos** `[from, to)`: `>=` abajo y `<` arriba. Evita
  perder registros entre las 23:59:59 y la medianoche. Hay tests que fijan el borde.
- **Buscar un socio por texto es una sola regla**: `MemberSpecification.searchPredicate`,
  que recibe el socio como `Path`. La usan Socios y Pagos (por el join); una
  búsqueda nueva por socio la reusa en vez de copiar el `cb.or(...)`.
- Las Specifications son para filtros **opcionales y combinables**. Para un filtro
  fijo (como la asistencia de un mes) va una query JPQL directa, que se lee mejor.

## Trampas conocidas

- **ModelMapper no aplana rutas anidadas.** `membership.expirationDate` →
  `expirationDate` necesitó un `TypeMap` explícito en `MappersConfig`. En cambio
  `member.id` → `memberId` sí funciona solo, porque matchea por tokens y
  `memberId` se parte en `[member, id]`. Cuando falla, el campo llega **null** y
  no falla nada: `MappersConfigTest` cubre los dos casos.
- **El `ObjectMapper` propio de `MappersConfig`** reemplaza al de Spring Boot. Si
  se le saca `disable(WRITE_DATES_AS_TIMESTAMPS)`, las fechas vuelven a salir como
  arrays `[2026, 4, 1]` en lugar de `"2026-04-01"`.
- **`CheckinReason` lleva su propio mensaje**, y de él se derivan `success` y
  `message`. No setearlos por separado: si se desalinean, el cliente pinta un
  ingreso en verde con el texto de un rechazo.
- **`MemberSpecification` usa LEFT JOIN** con la membresía. Con un join interno,
  el socio activo **sin** membresía desaparece de "Deudores" — y es justo a quien
  hay que cobrarle. La pantalla se vería bien, solo que faltando gente.
- **`data.sql` tiene fechas relativas** (`DATEADD` sobre `CURRENT_DATE`), no
  absolutas. Con fechas fijas los datos envejecen y las pantallas que filtran por
  hoy o por el mes en curso quedan vacías sin que nada falle. **No volver a poner
  fechas literales.** Ata el archivo a la sintaxis de H2, que es un costo aceptado.
- **El alta no crea membresía; la crea el primer pago.** Un socio nuevo nace
  Deudor. Todo código que lea `getMembership()` tiene que contemplar `null`
  (spec `001-alta-de-socio`).
- **`MemberEntity` tiene id asignado a mano (el DNI)**, así que `save` hace
  `merge`: guardar un socio con un DNI existente lo **pisa** sin error. Por eso el
  alta chequea `existsById` antes y responde 409.
- **`payments.id` y `check_ins.id` son `IDENTITY`**, así que siguen el orden de
  inserción: las filas del seed van ordenadas por fecha, o quedan registros con id
  menor y fecha posterior. Hay tests que lo verifican.

## Datos de prueba

13 socios (5 al día, 6 vencidos —uno **sin membresía**—, 2 inactivos), 34 pagos
(12 de un mismo socio, para ver la paginación) y ~32 check-ins, con actividad
**siempre del día de hoy** incluyendo ingresos y rechazos por los tres motivos
posibles. Tests de seed fijan que ese surtido no se pierda.

## Pendientes

- **No hay CORS ni autenticación.** En desarrollo se esquiva con el proxy del
  frontend. El `POST /checkins/{memberId}` queda abierto: lo va a consumir un
  dispositivo físico con numpad (Arduino).
- `PaymentEntity` no modela **qué período cubre** un pago, solo cuándo se hizo.
- `MemberEntity` no tiene **fecha de alta**, que el mockup del perfil pide.
- `ConfigController` solo expone el PUT, no hay GET.
- La búsqueda de socios ignora mayúsculas pero **no acentos**.
- `PaymentEntity.member` es ManyToOne EAGER: el listado de pagos hace una
  consulta extra por socio distinto. Si pesa, `@EntityGraph` en el repositorio.
- **Reactivar un socio dado de baja**: su DNI queda ocupado (el alta da 409) y el
  único camino hoy es cobrarle, porque el pago pone `status = true`.
- `success` en el check-in quedó derivable de `reason`: redundancia a limpiar.

## Flujo de trabajo

**Spec primero.** Cada feature arranca con una spec en `docs/specs/` que se
aprueba antes de escribir código y viaja en el mismo PR. El proceso y la
estructura están en `docs/specs/README.md`.

Ramas cortas desde `develop`, un PR por feature. Para sacar una rama sin arrastrar
un `develop` local viejo:

```bash
git fetch origin && git checkout -b feature/lo-que-sea origin/develop
```

Si se apilan PRs, la base de cada uno es su rama padre y se mergea **con merge
commit**: un squash rompe la cadena para los hijos.
