# 002 · Listado de pagos

**Estado:** Implementada · **Repo:** backend · **Rama:** `feature/payments-list`

## Contexto

La pantalla de Pagos del frontend es un placeholder. La primera mitad de la
feature es el **listado**: todos los pagos, del más reciente al más viejo, con un
buscador por socio y chips por medio de pago. La segunda mitad, **registrar un
pago**, va en una spec aparte.

`GET /payments` ya pagina y filtra por `member_id`, `payment_method` y rango de
fechas. Le faltan dos cosas para esta pantalla:

- **Buscar por socio a partir de texto.** El administrador tipea "perez" o parte
  de un DNI, no un id exacto.
- **El nombre del socio en cada pago.** Hoy cada pago trae solo `member_id`, así
  que el listado mostraría una columna de DNIs. La alternativa sería que el
  frontend pida el padrón entero para resolver los nombres, que es lo que ya hace
  Ingresos y está anotado como algo a corregir.

## Alcance

**Entra**

- Parámetro `search` en `GET /payments`.
- `member_name` y `member_last_name` en cada pago.

**No entra**

- Registrar un pago: `GET /config`, 201 en el `POST` y protección contra cobros
  duplicados. Va en la spec de registro.
- Cambios en los filtros existentes (`member_id`, `payment_method`, `from`, `to`).
- Hacer lo mismo en los check-ins.

## Requisitos

- **R1.** `search` matchea igual que la búsqueda de socios: nombre, apellido,
  nombre completo ("ana garcia") y parte del DNI, sin distinguir mayúsculas.
- **R2.** Un `search` ausente, vacío o con solo espacios no filtra.
- **R3.** `search` se combina con los demás filtros. Con `payment_method` a la
  vez, devuelve solo los pagos que cumplen los dos.
- **R4.** Cada pago de la respuesta trae `member_name` y `member_last_name`.
- **R5.** El `totalElements` de la página cuenta pagos, no filas duplicadas por
  el join con el socio.
- **R6.** Los campos que ya existían en la respuesta no cambian.

## Contrato

### `GET /payments`

Parámetro nuevo:

| Parámetro | Tipo | Significa |
|---|---|---|
| `search` | texto, opcional | Busca en nombre, apellido, nombre completo y DNI del socio. |

Los demás parámetros no cambian: `member_id`, `payment_method`, `from`, `to`,
`page`, `size` y `sort`. El orden por defecto sigue siendo `payment_date`
descendente.

Cada elemento de `content`:

```json
{
  "id": 34,
  "member_id": 30111222,
  "member_name": "Ana",
  "member_last_name": "Garcia",
  "amount": 7000.0,
  "payment_date": "2026-09-22 10:05:00",
  "payment_method": "CASH"
}
```

`payment_date` sigue viniendo **sin la `T`**.

## Diseño

### Búsqueda compartida (`MemberSpecification`)

La regla de qué texto matchea a un socio se extrae a un método público,
`MemberSpecification.searchPredicate(cb, member, search)`, que recibe el `Path`
del socio. `MemberSpecification.matches` lo usa sobre su `root`, y
`PaymentSpecification` sobre el join.

- **Descartado:** copiar el `cb.or(...)` en `PaymentSpecification`. Serían dos
  definiciones de "buscar socio" que se desalinean con el primer cambio, por
  ejemplo cuando se ignoren los acentos (que está pendiente).

### Filtro (`PaymentSpecification.memberMatches`)

- Hace `root.join("member")` y aplica `searchPredicate`. Devuelve `null` sin
  texto, como los demás filtros.
- El join es **interno**: todo pago tiene socio.
- No duplica filas, porque es un ManyToOne: cada pago tiene exactamente un socio
  (R5).

### Respuesta (`PaymentResponseDto`)

- Se agregan `memberName` y `memberLastName` con `@JsonProperty`.
- El `Payment` ya lleva el `Member` completo. `memberId` se aplana solo, por los
  tokens `[member, id]` (ver `CLAUDE.md`). Con `memberName` y `memberLastName`
  debería pasar lo mismo, pero **se prueba en `MappersConfigTest`**, porque si el
  matching falla el campo llega null sin error. Si no alcanza, se hace un
  `TypeMap` explícito como el de `expirationDate`.
- **Resultado:** el aplanado automático alcanzó, sin `TypeMap`. Lo fija
  `MappersConfigTest.shouldFlattenTheMemberNameIntoThePaymentResponse`.

### Controller y servicio

`searchPayments` recibe el `search` y lo pasa a `Specification.allOf` con los
demás filtros.

## Pruebas

| Requisito | Test |
|---|---|
| R1, R2, R3, R5 | `PaymentSpecificationTest`: por apellido, por nombre completo, por parte de DNI, texto en blanco, combinado con medio de pago y total de la página. |
| R1 (la búsqueda de socios sigue igual) | Los tests existentes de `MemberSpecificationTest`, sin tocar. |
| R4, R6 | `MappersConfigTest`: nombre y apellido aplanados, sin perder `memberId`. |

## Tareas

- [x] Extraer `MemberSpecification.searchPredicate`.
- [x] `PaymentSpecification.memberMatches`.
- [x] Parámetro `search` en el controller y el servicio.
- [x] `memberName` y `memberLastName` en `PaymentResponseDto`.
- [x] Tests.
- [x] `./mvnw test` en verde.
- [x] Actualizar `CLAUDE.md` y pasar esta spec a **Implementada**.

## Pendientes

- **Consultas N+1.** `PaymentEntity.member` es un ManyToOne EAGER, así que cada
  página hace una consulta extra por socio distinto, más su membresía. Con 10 o
  20 pagos por página no se nota. Si crece, conviene un `@EntityGraph` en el
  repositorio.
- Los check-ins tienen el mismo problema de nombres (Ingresos pide el padrón
  entero). Se puede resolver igual que acá.
