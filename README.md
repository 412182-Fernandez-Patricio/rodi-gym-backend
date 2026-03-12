# RODI GYM - Especificación Técnica del Proyecto

## 📌 Visión General
RODI GYM es un sistema de gestión simplificado diseñado específicamente para gimnasios pequeños que necesitan digitalizar su control de acceso sin incurrir en costos elevados o sistemas complejos. El diferencial radica en la validación mediante DNI y un registro de auditoría visual.

## 🛠️ Stack Tecnológico
* **Frontend:** Angular con Tailwind CSS (Responsive Web App).
* **Backend:** Java Spring Boot (Arquitectura RESTful).
* **Base de Datos:** MySQL.
* **Infraestructura:** AWS (EC2 para la app y RDS para la base de datos).

## 📋 Alcance del Proyecto (MVP)

### Funcionalidades Incluidas (In Scope)
* **Gestión de Miembros:** Creación, actualización y seguimiento de perfiles de socios.
* **Control de Acceso (Check-in):** Sistema automático que valida la entrada mediante el ingreso del DNI en un teclado numérico.
* **Auditoría Visual:** El sistema permitirá cargar una fotografía del socio para que el dueño pueda verificar la identidad de manera asincrónica.
* **Registro de Asistencia:** Registro de marcas de tiempo para cada ingreso exitoso.
* **Analíticas de Pago:** Dashboard básico para visualizar tendencias de cobro y retención de alumnos.

### Funcionalidades Excluidas (Out of Scope)
* **Check-out:** No se registrará la salida del gimnasio para mantener la agilidad del proceso.
* **Facturación Legal:** El sistema no emite facturas con validez fiscal.
* **Aplicación Móvil Standalone:** El sistema funcionará exclusivamente como Web App/PWA.
* **Integración Social Media:** No se incluye compartir actividades en redes sociales para evitar complejidad innecesaria.

## 🗄️ Modelo de Datos Sugerido
* **`members`**: Contendrá `id`, `name`, `dni`, `membership_status`, `expiry_date` y `photo_url`.
* **`check_ins`**: Contendrá `id`, `member_id` y `checkin_time`.
* **`payments`**: Contendrá `id`, `member_id`, `amount` y `payment_date`.

## 🚦 Lógica de Validación (Flujo de Entrada)
1.  **Entrada de Datos:** El socio ingresa su DNI en el teclado numérico (o interfaz simulada).
2.  **Verificación:** El backend consulta el estado y la fecha de vencimiento.
3.  **Resultado Positivo:** Si la membresía está al día, el sistema registra el ingreso, emite un sonido de confirmación y otorga acceso.
4.  **Resultado Negativo:** Si el DNI no existe o la membresía está vencida, el sistema deniega el acceso y muestra un mensaje de error claro.

## 🎨 Guía de Estilo
* **Concepto:** Simplicidad y accesibilidad (similar a Google Forms).
* **Tipografía:** Roboto (16px para cuerpo, 24px para encabezados).
* **Paleta:** Azul (#3B82F6) como color principal y Verde (#10B981) como secundario.