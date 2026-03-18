# Guía de Manejo de Errores - RODI GYM API

Esta API utiliza un manejador de excepciones global (`@ControllerAdvice`) para asegurar que todas las respuestas de error sigan una estructura consistente y predecible.

## 📋 Estructura de la Respuesta de Error

Todas las excepciones capturadas devolverán un objeto JSON con el siguiente formato:

```json
{
  "timestamp": "2026-03-12T12:05:30.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Detalle descriptivo del error"
}
```

| Campo | Descripción |
| :--- | :--- |
| `timestamp` | Fecha y hora exacta en que ocurrió el error (ISO 8601). |
| `status` | Código de estado HTTP (ej. 400, 404, 500). |
| `error` | Descripción corta del estándar HTTP asociado al código. |
| `message` | Mensaje detallado para el desarrollador o el usuario final. |

---

## 🚀 Ejemplos de Uso y Respuestas

### 1. Error de Validación (400 Bad Request)
Ocurre cuando el cuerpo del JSON enviado en un `POST` o `PUT` no cumple con las reglas definidas en los DTOs (ej. falta el nombre o el ID).

**Escenario:** Crear un socio sin enviar el nombre ni el ID.
**Endpoint:** `POST /members`

**Respuesta:**
```json
{
  "timestamp": "2026-03-12T12:10:00.456",
  "status": 400,
  "error": "Bad Request",
  "message": "id: ID is required, name: Name is required and cannot be empty"
}
```

### 2. Recurso No Encontrado (404 Not Found)
Ocurre cuando se solicita un ID que no existe en la base de datos.

**Escenario:** Consultar un socio con un ID inexistente.
**Endpoint:** `GET /members/999999`

**Respuesta:**
```json
{
  "timestamp": "2026-03-12T12:12:15.789",
  "status": 404,
  "error": "Not Found",
  "message": "Member not found with id: 999999"
}
```

### 3. Error Interno del Servidor (500 Internal Server Error)
Es la red de seguridad para cualquier error inesperado (bases de datos caídas, errores de puntero nulo no controlados, etc.).

**Respuesta:**
```json
{
  "timestamp": "2026-03-12T12:15:00.111",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Causa raíz del error inesperado..."
}
```

---

## 🛠️ Cómo agregar nuevos manejadores

Si necesitas capturar una nueva excepción específica, debes agregar un método en `ControllerExceptionHandler.java`:

```java
@ExceptionHandler(MiExcepcionPersonalizada.class)
public ResponseEntity<ErrorApi> handleMiExcepcion(MiExcepcionPersonalizada e) {
    ErrorApi error = ErrorApi.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(HttpStatus.CONFLICT.value())
            .error(HttpStatus.CONFLICT.getReasonPhrase())
            .message(e.getMessage())
            .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
}
```

## 💡 Notas para el Frontend
- Siempre valida el código de estado HTTP antes de procesar la respuesta.
- El campo `message` puede contener múltiples errores de validación separados por comas, ideales para mostrar en notificaciones tipo "Toast" o alertas.
