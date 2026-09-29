# 003 · Registro de pagos

**Estado:** Implementada · **Repo:** backend · **Rama:** `feature/register-payment`

## Contexto

Es la segunda mitad de Pagos. El listado quedó en la spec 002. `POST /payments`
ya existe y hace todo el trabajo: toma el monto de la config, extiende la
membresía un mes (la crea si falta, spec 001) y reactiva al socio si estaba dado
de baja. Para que el frontend arme el formulario le faltan dos cosas:

- **Saber cuánto se va a cobrar antes de cobrar.** El monto lo decide el
  servidor, pero solo existe `PUT /config/monthly-price`, no hay un GET. El
  administrador tiene que ver el monto antes de confirmar.
- **Un código de estado coherente.** El alta de socio responde 201 desde la spec
  001, y el alta de pago todavía responde 200.

## Alcance

**Entra**

- `GET /config/monthly-price`.
- `POST /payments` responde 201.
- Tests del contrato HTTP del `POST`, que hoy no tiene ninguno.

**No entra**

- **Bloquear cobros duplicados** en el backend. Ver Decisiones.
- Que el pago guarde **qué período cubre**. Sigue pendiente.
- Montos distintos de la cuota (descuentos, cobros parciales). El monto lo sigue
  fijando el servidor.
- Anular o editar un pago.

## Requisitos

- **R1.** `GET /config/monthly-price` devuelve la cuota **que va a cobrar el
  próximo `POST /payments`**, incluido el valor por defecto (5000) si no hay
  ninguna configurada.
- **R2.** La respuesta del GET tiene la misma forma que la del PUT.
- **R3.** Un `POST /payments` válido responde **201** con el pago creado, que
  incluye `member_name` y `member_last_name` (spec 002).
- **R4.** Un `member_id` inexistente responde **404**.
- **R5.** Si falta `member_id` o `payment_method`, o el medio de pago no es
  `CASH`, `TRANSFER` ni `DEBIT`, responde **400**.
- **R6.** Lo que el pago le hace a la membresía **no cambia**: lo cubren los tests
  de `PaymentServiceImplTest` de la spec 001.

## Contrato

### `GET /config/monthly-price` (nuevo)

```json
{ "key": "monthly_price", "value": "7000.0" }
```

`value` es texto, igual que en la respuesta del `PUT`. El frontend lo convierte a
número.

### `POST /payments`

Request, sin cambios:

```json
{ "member_id": 30111222, "payment_method": "CASH" }
```

| Caso | Código | Cuerpo |
|---|---|---|
| Pago registrado | **201** | `PaymentResponseDto` |
| Falta un campo o el medio de pago no existe | 400 | `ErrorApi` |
| El socio no existe | 404 | `ErrorApi` |

## Diseño

### Config

- `ConfigController` suma un `GET /monthly-price` que devuelve
  `new ConfigResponseDto("monthly_price", configService.getMonthlyPrice().toString())`.
- **Usa `getMonthlyPrice()`**, que es lo mismo que usa el `POST`, y no lee la
  tabla directo. Así el precio que se muestra y el que se cobra no pueden
  diferir, ni siquiera cuando se aplica el valor por defecto (R1).
- **Descartado:** devolver `value` como número. Sería más cómodo, pero rompe la
  simetría con el `PUT`, y cambiar el `PUT` queda fuera de alcance.

### Pagos

- `createPayment` devuelve `ResponseEntity.status(HttpStatus.CREATED)`, sin header
  `Location`, por el mismo motivo que en la spec 001: el proxy reescribe el
  prefijo.

### Enum inválido → 400

Un `"payment_method": "BITCOIN"` hoy no llega a la validación: Jackson falla al
deserializar y tira `HttpMessageNotReadableException`, que cae en el handler
genérico y responde **500**. Se agrega un handler para esa excepción que responde
400. Además cubre cualquier JSON mal formado, en cualquier endpoint.

### Decisiones: cobros duplicados

Nada impide cobrarle dos veces seguidas al mismo socio, y cada cobro suma un
mes. Hay dos casos:

- **Doble envío** (doble tap, o reintento tras un error de red): lo resuelve el
  frontend deshabilitando "Cobrar" mientras la request está en curso.
- **Cobrarle a alguien que ya está al día**: puede ser un error o un pago
  adelantado, que es legítimo. El backend no puede distinguirlos. Lo resuelve el
  frontend **advirtiendo** antes de confirmar ("Está al día hasta el 12/10; este
  pago suma un mes").

**Descartado:** que el backend rechace con 409 un pago del mismo socio dentro de
los N minutos del anterior. Bloquearía el caso legítimo de cobrar dos meses
juntos, y la ventana sería arbitraria.

## Pruebas

| Requisito | Test |
|---|---|
| R1, R2 | `ConfigControllerTest` (`@WebMvcTest`, servicio mockeado): el GET devuelve lo que da `getMonthlyPrice()`. |
| R1 (valor por defecto) | `ConfigServiceImplTest` (`@DataJpaTest`): sin la fila de config, devuelve 5000. |
| R3, R4, R5 | `PaymentControllerTest` (`@WebMvcTest`): 201 con el nombre del socio; 404; 400 sin `member_id`; 400 con `BITCOIN`. |
| R6 | `PaymentServiceImplTest`, que ya existe y no cambia. |

## Tareas

- [x] `GET /config/monthly-price`.
- [x] 201 en `POST /payments`.
- [x] Handler de `HttpMessageNotReadableException` que responde 400. El
  `message` es genérico ("Malformed request body"): el de Jackson expone nombres
  de clases internas.
- [x] Tests (`PaymentControllerTest`, `ConfigControllerTest` y
  `ConfigServiceImplTest`).
- [x] `./mvnw test` en verde: 67 tests.
- [x] Actualizar `CLAUDE.md`.
- [x] Probarlo contra el frontend (lo levanta el usuario).
- [x] Pasar esta spec a **Implementada**.

## Pendientes

- **Período que cubre cada pago**: el pago sigue sin guardarlo. Con cobros
  adelantados se vuelve más necesario.
- Un enum inválido en un **query param** (`GET /payments?payment_method=BITCOIN`)
  tira `MethodArgumentTypeMismatchException` y también responde 500. Es el
  mismo problema que el del body, en otro lugar.
- Anular un pago cargado por error. Hoy el único arreglo es a mano en la base.
- El `PUT /config/monthly-price` recibe el precio como query param y lo devuelve
  como texto. Conviene revisarlo cuando haya pantalla de configuración.
