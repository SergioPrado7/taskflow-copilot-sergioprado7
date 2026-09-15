# Arquitectura de TaskFlow

Este documento resume la arquitectura para un desarrollador nuevo: capas y paquetes, el recorrido de `POST /projects/{projectId}/tasks`, dónde viven las reglas de negocio, cómo funciona la seguridad JWT y la organización de los tests.

## Capas y paquetes

- `com.taskflow.controller` — Controladores HTTP que reciben `DTO`s (`record`) y validan entradas. Ej.: `TaskController` (`src/main/java/com/taskflow/controller/TaskController.java`).
- `com.taskflow.dto` — DTOs de entrada/salida como `TaskRequest` y `TaskResponse` (`src/main/java/com/taskflow/dto/TaskRequest.java`).
- `com.taskflow.mapper` — Mappers manuales que transforman entre entidades y DTOs. Ej.: `TaskMapper` con `TaskMapper.aResponse` (`src/main/java/com/taskflow/mapper/TaskMapper.java`).
- `com.taskflow.service` — Casos de uso / lógica de orquestación. Ej.: `TaskService` (`src/main/java/com/taskflow/service/TaskService.java`).
- `com.taskflow.repository` — Repositorios Spring Data JPA. Ej.: `TaskRepository` (`src/main/java/com/taskflow/repository/TaskRepository.java`).
- `com.taskflow.model` — Entidades ricas en comportamiento (reglas de negocio dentro de las entidades). Ej.: `Task` (`src/main/java/com/taskflow/model/Task.java`).
- `com.taskflow.security` — Configuración de seguridad, filtros JWT y clases auxiliares como `SecurityConfig` y `ProjectSecurity` (`src/main/java/com/taskflow/config/SecurityConfig.java`, `src/main/java/com/taskflow/security/ProjectSecurity.java`).
- `src/main/resources/static` — UI estática servida por la API.

Las capas siguen el patrón: controller → service → repository → base de datos. Los controladores retornan DTOs (nunca entidades) y usan mappers.

## Recorrido de `POST /projects/{projectId}/tasks`

Paso a paso (simplificado):

1. POST a `/projects/{projectId}/tasks` llega a `TaskController.createTask` (`src/main/java/com/taskflow/controller/TaskController.java`).
   - Spring valida el `@RequestBody` `TaskRequest` con `@Valid`.
2. `TaskController.createTask` comprueba que el proyecto existe llamando a `ProjectService.buscarPorId` (`src/main/java/com/taskflow/service/ProjectService.java`); si no existe lanza `ProjectNotFoundException` (404).
3. El controller invoca `TaskService.crear(request, projectId)` (`src/main/java/com/taskflow/service/TaskService.java`).
4. `TaskService.crear` convierte el DTO a entidad nueva usando `TaskMapper.aEntidadNueva(request, projectId)` (`src/main/java/com/taskflow/mapper/TaskMapper.java`), que a su vez usa `Task.crear(...)` para aplicar las reglas de dominio (status `TODO`, dueDate no en el pasado, etc.).
5. `TaskService.crear` persiste la entidad con `TaskRepository.save(task)` (`src/main/java/com/taskflow/repository/TaskRepository.java`) y devuelve la entidad guardada con id.
6. `TaskController.createTask` responde `201 Created` con la cabecera `Location` apuntando a `/tasks/{id}` y el cuerpo es el DTO `TaskMapper.aResponse(creada)`.
7. Las validaciones y errores suben y son manejados por `GlobalExceptionHandler` (`src/main/java/com/taskflow/advice/GlobalExceptionHandler.java`) para producir respuestas uniformes (400, 404, 422, 401, 403, 409).

## Dónde viven las reglas de negocio

- Reglas de negocio y invariantes fundamentales están en las entidades del paquete `com.taskflow.model` (p. ej. `Task`). Reutilizar estos métodos en `service` y no duplicar lógica en controladores.
- Orquestación, composición de pasos y verificaciones transaccionales (acceso a repositorios, coordinación entre entidades) viven en `service` (p. ej. `TaskService`).
- Validación de permisos finos que dependen de contexto pueden residir en `service` o en clases de seguridad como `ProjectSecurity` (usadas por `@PreAuthorize`).

## Seguridad (JWT)

- Seguridad sin estado basada en JWT: `SecurityConfig` instala filtros que extraen el token JWT, validan firma y crean autenticación en el contexto de seguridad.
- Endpoints públicos: `/auth/**`, `/info`, Swagger y consola H2, y los archivos estáticos en `src/main/resources/static`.
- Todos los demás endpoints requieren token.
- Roles y autorizaciones se usan con `@PreAuthorize` y utilidades de seguridad. Operaciones sensibles (p. ej. eliminar un proyecto) verifican que el solicitante sea el dueño o tenga role `ADMIN`. Esa lógica reside en `ProjectSecurity` (`src/main/java/com/taskflow/security/ProjectSecurity.java`) y está referenciada en los controladores/servicios (`@PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#projectId, authentication.name)")`).
- Errores de autenticación/permiso se traducen a 401/403 por `GlobalExceptionHandler` y por la configuración de seguridad.

## Organización de tests

- Unit tests: JUnit 5 + Mockito, sin arrancar Spring. Ubicación ejemplo: `src/test/java/com/taskflow/unit/TaskServiceTest.java`. Se prueban reglas de negocio y lógicas puras.
- Slice tests: `@WebMvcTest` o `@DataJpaTest` para probar controladores o repositorios aislados. Ej.: `src/test/java/com/taskflow/slice/TaskControllerTest.java`.
- Integration tests: `@SpringBootTest` con perfil `test` (y Testcontainers para `*IT.java`), pero por defecto no se ejecutan en `mvn test` salvo que se establezca `-Ddocker.tests=true`.
- Perfil `h2`: arrancar la app con `mvn spring-boot:run "-Dspring-boot.run.profiles=h2"` crea la BD y `DataSeeder` la puebla con usuarios `ana`, `luis`, `admin` y datos de ejemplo (`src/main/java/com/taskflow/config/DataSeeder.java`).

Comandos útiles:
- Ejecutar tests unitarios: `mvn -q test` o `mvn -q test "-Dtest=TaskServiceTest"`.
- Ejecutar app con H2 y datos de ejemplo: `mvn spring-boot:run "-Dspring-boot.run.profiles=h2"`.
- Generar jar sin tests: `mvn -q package -DskipTests`.

## Buenas prácticas al contribuir

- No duplicar reglas de negocio fuera de las entidades.
- Controladores → validar y delegar; no poner lógica de negocio en controladores.
- Los DTOs son `record` con Bean Validation y se usan con `@Valid`.
- Mantener la inyección por constructor y evitar Lombok.
- `POST` que crea devuelve `201 Created` con `Location`; `DELETE` devuelve `204 No Content`.

---

Si se desea, se puede añadir un diagrama simple o ejemplos de request/response como complemento. Este documento pretende dar al nuevo desarrollador la vista práctica para comenzar a contribuir rápido.