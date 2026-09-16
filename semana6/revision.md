# Copilot CLI Session

> [!NOTE]
> - **Session ID:** `c0c2ff48-50c8-4ec4-a1f7-e274ba908f4f`  
> - **Started:** 16/9/2026, 3:12:54 p.m.  
> - **Duration:** 1m 12s  
> - **Exported:** 16/9/2026, 3:14:06 p.m.  

---

<sub>2s</sub>

### User

Revisa semana6/proyecto-final.diff contra la especificación specs/assignee.md.

---

<sub>14s</sub>

### Copilot

Leeré el diff y la especificación en paralelo para identificar los archivos modificados y comparar su contenido con la spec. Luego abriré en paralelo cada archivo tocado por el diff para revisarlos con contexto. Ejecutando lecturas de los dos archivos iniciales.

---

<sub>14s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\semana6\proyecto-final.diff.**

C:\Users\sergi\taskflow-copilot-sergioprado7\semana6\proyecto-final.diff

<details>
<summary>227 lines</summary>

```diff
diff --git a/src/main/java/com/taskflow/controller/TaskController.java b/src/main/java/com/taskflow/controller/TaskController.java
index cae57b7..f173471 100644
--- a/src/main/java/com/taskflow/controller/TaskController.java
+++ b/src/main/java/com/taskflow/controller/TaskController.java
@@ -3,6 +3,7 @@ package com.taskflow.controller;
 import com.taskflow.dto.TaskRequest;
 import com.taskflow.dto.TaskResponse;
 import com.taskflow.dto.TaskStatusUpdateRequest;
+import com.taskflow.dto.TaskAssigneeUpdateRequest;
 import com.taskflow.exception.ProjectNotFoundException;
 import com.taskflow.exception.TaskNotFoundException;
 import com.taskflow.exception.TaskValidationException;
@@ -155,4 +156,15 @@ public class TaskController {
         Task actualizada = taskService.cambiarStatus(id, request.status());
         return TaskMapper.aResponse(actualizada);
     }
+
+    @Operation(summary = "Reasigna el responsable de una tarea",
+            description = "Cambia solo el campo assigneeId de la tarea indicada. 404 si no existe; 422 si la tarea ya está DONE.")
+    @PatchMapping("/tasks/{id}/assignee")
+    public TaskResponse patchAssignee(@PathVariable("id") Long id,
+                                      @Valid @RequestBody TaskAssigneeUpdateRequest request) {
+        Task task = taskService.buscarPorId(id)
+                .orElseThrow(() -> new com.taskflow.exception.TaskNotFoundException(id));
+        Task reasignada = taskService.reasignar(task, request.assigneeId());
+        return TaskMapper.aResponse(reasignada);
+    }
 }
diff --git a/src/main/java/com/taskflow/dto/TaskAssigneeUpdateRequest.java b/src/main/java/com/taskflow/dto/TaskAssigneeUpdateRequest.java
new file mode 100644
index 0000000..848bfcb
--- /dev/null
+++ b/src/main/java/com/taskflow/dto/TaskAssigneeUpdateRequest.java
@@ -0,0 +1,10 @@
+package com.taskflow.dto;
+
+import jakarta.validation.constraints.NotNull;
+import jakarta.validation.constraints.Positive;
+
+/**
+ * DTO para actualizar solo el responsable (assigneeId) de una tarea.
+ */
+public record TaskAssigneeUpdateRequest(@NotNull @Positive Long assigneeId) {
+}
diff --git a/src/main/java/com/taskflow/service/TaskService.java b/src/main/java/com/taskflow/service/TaskService.java
index 9e89212..2f93f7b 100644
--- a/src/main/java/com/taskflow/service/TaskService.java
+++ b/src/main/java/com/taskflow/service/TaskService.java
@@ -86,6 +86,18 @@ public class TaskService {
         return repository.save(task);
     }
 
+    /**
+     * Reasigna el responsable de una tarea ya encontrada. Lanza TaskStateException si la tarea
+     * está en estado DONE (no se pueden reasignar tareas terminadas).
+     */
+    public Task reasignar(Task tarea, Long assigneeId) {
+        if (tarea.getStatus() == TaskStatus.DONE) {
+            throw new TaskStateException("No se puede reasignar una tarea terminada.");
+        }
+        tarea.setAssigneeId(assigneeId);
+        return repository.save(tarea);
+    }
+
     /**
      * Completa una tarea (la pasa a DONE). Tras MP-9 queda DELEGANDO en cambiarStatus(id, DONE): un
      * solo camino para "cambiar de estado", que además hereda la traducción a 422.
diff --git a/src/test/java/com/taskflow/slice/ReasignarTareaControllerTest.java b/src/test/java/com/taskflow/slice/ReasignarTareaControllerTest.java
new file mode 100644
index 0000000..cea766e
--- /dev/null
+++ b/src/test/java/com/taskflow/slice/ReasignarTareaControllerTest.java
@@ -0,0 +1,99 @@
+package com.taskflow.slice;
+
+import com.taskflow.controller.TaskController;
+import com.taskflow.exception.TaskStateException;
+import com.taskflow.model.Priority;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.security.JwtAuthenticationFilter;
+import com.taskflow.service.ProjectService;
+import com.taskflow.service.TaskService;
+import org.junit.jupiter.api.Test;
+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
+import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
+import org.springframework.http.MediaType;
+import org.springframework.test.context.bean.override.mockito.MockitoBean;
+import org.springframework.test.web.servlet.MockMvc;
+
+import java.time.LocalDate;
+import java.util.Optional;
+
+import static org.mockito.ArgumentMatchers.any;
+import static org.mockito.ArgumentMatchers.eq;
+import static org.mockito.Mockito.when;
+import static org.mockito.Mockito.verify;
+import static org.mockito.Mockito.never;
+import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
+
+@WebMvcTest(TaskController.class)
+@AutoConfigureMockMvc(addFilters = false)
+class ReasignarTareaControllerTest {
+
+    @Autowired
+    private MockMvc mockMvc;
+
+    @MockitoBean
+    private TaskService taskService;
+
+    @MockitoBean
+    private ProjectService projectService;
+
+    @MockitoBean
+    private JwtAuthenticationFilter jwtAuthenticationFilter;
+
+    @Test
+    void patchAssignee_retorna200YAssigneeActualizado() throws Exception {
+        LocalDate hoy = LocalDate.now();
+        Task original = new Task(4L, "Titulo", "desc", TaskStatus.TODO, Priority.MED, 1L, null, hoy.plusDays(7));
+        Task updated = new Task(4L, "Titulo", "desc", TaskStatus.TODO, Priority.MED, 1L, 2L, hoy.plusDays(7));
+
+        when(taskService.buscarPorId(4L)).thenReturn(Optional.of(original));
+        when(taskService.reasignar(any(Task.class), eq(2L))).thenReturn(updated);
+
+        mockMvc.perform(patch("/tasks/4/assignee")
+                        .contentType(MediaType.APPLICATION_JSON)
+                        .content("{ \"assigneeId\": 2 }"))
+                .andExpect(status().isOk())
+                .andExpect(jsonPath("$.assigneeId").value(2));
+    }
+
+    @Test
+    void patchAssignee_tareaDONE_retorna422() throws Exception {
+        LocalDate hoy = LocalDate.now();
+        Task done = new Task(2L, "Titulo", "desc", TaskStatus.DONE, Priority.MED, 1L, 1L, hoy);
+
+        when(taskService.buscarPorId(2L)).thenReturn(Optional.of(done));
+        when(taskService.reasignar(any(Task.class), any(Long.class)))
+                .thenThrow(new TaskStateException("No se puede reasignar una tarea terminada."));
+
+        mockMvc.perform(patch("/tasks/2/assignee")
+                        .contentType(MediaType.APPLICATION_JSON)
+                        .content("{ \"assigneeId\": 3 }"))
+                .andExpect(status().isUnprocessableEntity())
+                .andExpect(jsonPath("$.status").value(422));
+    }
+
+    @Test
+    void patchAssignee_tareaInexistente_retorna404() throws Exception {
+        when(taskService.buscarPorId(99L)).thenReturn(Optional.empty());
+
+        mockMvc.perform(patch("/tasks/99/assignee")
+                        .contentType(MediaType.APPLICATION_JSON)
+                        .content("{ \"assigneeId\": 2 }"))
+                .andExpect(status().isNotFound())
+                .andExpect(jsonPath("$.status").value(404));
+    }
+
+    @Test
+    void patchAssignee_cuerpoVacio_devuelve400_yNoLlamaService() throws Exception {
+        mockMvc.perform(patch("/tasks/6/assignee")
+                        .contentType(MediaType.APPLICATION_JSON)
+                        .content("{}"))
+                .andExpect(status().isBadRequest());
+
+        verify(taskService, never()).reasignar(any(), any(Long.class));
+    }
+}
diff --git a/src/test/java/com/taskflow/unit/ReasignarTareaServiceTest.java b/src/test/java/com/taskflow/unit/ReasignarTareaServiceTest.java
new file mode 100644
index 0000000..dd4cf61
--- /dev/null
+++ b/src/test/java/com/taskflow/unit/ReasignarTareaServiceTest.java
@@ -0,0 +1,50 @@
+package com.taskflow.unit;
+
+import com.taskflow.exception.TaskStateException;
+import com.taskflow.exception.TaskValidationException;
+import com.taskflow.model.Priority;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.repository.TaskRepository;
+import com.taskflow.service.TaskService;
+import org.junit.jupiter.api.Test;
+import org.junit.jupiter.api.extension.ExtendWith;
+import org.mockito.ArgumentCaptor;
+import org.mockito.InjectMocks;
+import org.mockito.Mock;
+import org.mockito.junit.jupiter.MockitoExtension;
+
+import static org.junit.jupiter.api.Assertions.*;
+import static org.mockito.Mockito.*;
+
+@ExtendWith(MockitoExtension.class)
+class ReasignarTareaServiceTest {
+
+    @Mock
+    private TaskRepository repository;
+
+    @InjectMocks
+    private TaskService taskService;
+
+    @Test
+    void reasignar_tareaTODO_sinResponsable_seGuardaConNuevoAssignee() throws TaskValidationException {
+        Task tarea = new Task(10L, "Titulo", "desc", TaskStatus.TODO, Priority.MED, 1L, null, null);
+
+        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
+
+        Task resultado = taskService.reasignar(tarea, 5L);
+
+        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
+        verify(repository).save(captor.capture());
+        assertEquals(5L, captor.getValue().getAssigneeId());
+        assertEquals(5L, resultado.getAssigneeId());
+    }
+
+    @Test
+    void reasignar_tareaDONE_lanzaTaskStateException_y_noSave() throws TaskValidationException {
+        Task tarea = new Task(2L, "Hecho", "desc", TaskStatus.DONE, Priority.MED, 1L, 1L, null);
+
+        assertThrows(TaskStateException.class, () -> taskService.reasignar(tarea, 3L));
+        verify(repository, never()).save(any());
+    }
+}
```

</details>


---

<sub>14s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\specs\assignee.md.**

C:\Users\sergi\taskflow-copilot-sergioprado7\specs\assignee.md

<details>
<summary>73 lines</summary>

````
# Especificación — `PATCH /tasks/{id}/assignee`

## Qué

Un endpoint que cambia **solo** el responsable de una tarea. Hoy la única forma es `PUT /tasks/{id}`,
que obliga a mandar la tarea completa (título, prioridad, fecha…) para tocar un campo. Es el mismo
razonamiento de `PATCH /tasks/{id}/status`: un DTO chico por operación.

```
PATCH /tasks/4/assignee
Authorization: ******
Content-Type: application/json

{ "assigneeId": 2 }
```

## Reglas

1. La tarea no existe → **`404`** con el `ErrorResponse` uniforme (`TaskNotFoundException`), resuelto
   **como en la ruta vecina** `GET /tasks/{id}`:
   `taskService.buscarPorId(id).orElseThrow(() -> new TaskNotFoundException(id))`.
2. La tarea está `DONE` → **`422`** con el `ErrorResponse` uniforme y el mensaje
   `No se puede reasignar una tarea terminada.`. Se lanza `TaskStateException`, la misma que usa
   `cambiarStatus` para su 422. Una tarea terminada conserva a quien la terminó.
3. El cuerpo no trae `assigneeId`, lo trae en `null` o trae un número menor que 1 → **`400`** por
   Bean Validation (`@NotNull` y `@Positive` en el DTO, `@Valid` en el controller). El
   `GlobalExceptionHandler` ya responde ese 400 con el detalle por campo en `errors`.
4. En cualquier otro caso: se asigna el responsable con `Task.setAssigneeId`, se guarda con el
   repositorio y responde **`200`** con el `TaskResponse` actualizado (vía `TaskMapper.aResponse`).
5. Seguridad: igual que el resto de `/tasks` (sin token → `401`). No cambies `SecurityConfig`.

**Fuera de alcance** (no lo implementes aunque parezca buena idea):

- Comprobar que el usuario `assigneeId` existe. Exigiría inyectar `UserRepository` en `TaskService`,
  y los tests existentes de `TaskService` no lo conocen.
- Quitar el responsable (`assigneeId: null`). Hoy es un 400 a propósito.

## Dónde

- DTO de entrada nuevo en `com.taskflow.dto`:
  `public record TaskAssigneeUpdateRequest(@NotNull @Positive Long assigneeId)`, con su Javadoc, en la
  misma forma que `TaskStatusUpdateRequest`.
- **Sin mapper nuevo**: la respuesta es el `TaskResponse` de siempre.
- Método nuevo en `TaskService`: `public Task reasignar(Task tarea, Long assigneeId)`. Recibe la
  tarea **ya encontrada** (el 404 lo resuelve el controller) y lanza `TaskStateException` si está
  `DONE`.
- Endpoint nuevo en `TaskController`: `@PatchMapping("/tasks/{id}/assignee")`, con
  `@PathVariable("id") Long id` y `@Valid @RequestBody TaskAssigneeUpdateRequest request`.

## Tests que deben existir al terminar

Los dos en **clases nuevas** (las que ya existen no se tocan):

- **Unit** `src/test/java/com/taskflow/unit/ReasignarTareaServiceTest.java`, con
  `@Mock TaskRepository` e `@InjectMocks TaskService`:
  - tarea `TODO` sin responsable → se llama a `save` con la tarea ya con el `assigneeId` pedido
    (compruébalo con `ArgumentCaptor`, no con lo que devuelve el mock de `save`);
  - tarea `DONE` → `TaskStateException` y `save` **nunca** se llama.
- **Slice** `src/test/java/com/taskflow/slice/ReasignarTareaControllerTest.java`, con
  `@WebMvcTest(TaskController.class)`, `@AutoConfigureMockMvc(addFilters = false)` y `@MockitoBean`
  de `TaskService`, `ProjectService` y `JwtAuthenticationFilter`:
  - `200` y `$.assigneeId` = el pedido;
  - `422` cuando `reasignar` lanza `TaskStateException`;
  - `404` cuando `buscarPorId` devuelve `Optional.empty()`;
  - `400` con el cuerpo `{}`, y `reasignar` **nunca** se llama (`verify(taskService, never())`).

## Resultado esperado con la semilla

Con la app recién arrancada con el perfil `h2` y el token de `ana`, **en este orden** (el paso 3
depende del 1):

| # | Petición | Resultado |
|---|---|---|
| 1 | `PATCH /tasks/4/assignee` con `{"assigneeId": 2}` | `200`, `assigneeId` = 2 (la tarea 4 no tenía responsable) |
| 2 | `GET /tasks/4` | `assigneeId` = 2: se guardó, no solo se respondió |
| 3 | `PATCH /tasks/4/status` con `{"status": "DONE"}` | `200`. Antes del paso 1 respondía `422` («sin responsable»): la regla de `Task.setStatus` ahora deja pasar |
| 4 | `PATCH /tasks/2/assignee` con `{"assigneeId": 3}` | `422` con `message` = `No se puede reasignar una tarea terminada.` (la 2 es `DONE`) |
| 5 | `PATCH /tasks/99/assignee` con `{"assigneeId": 2}` | `404` |
| 6 | `PATCH /tasks/6/assignee` con `{}` | `400`, y en `errors` aparece `assigneeId` |
| 7 | `PATCH /tasks/6/assignee` con `{"assigneeId": 0}` | `400` |
| 8 | el paso 1 sin token | `401` |

## Restricciones para el agente

- No modifiques, borres ni desactives ningún test existente.
- No toques archivos fuera de `TaskAssigneeUpdateRequest.java` (nuevo), `TaskService.java`,
  `TaskController.java` y las dos clases de test nuevas.
- Al terminar, `mvn -q test` tiene que pasar completo.

El pull request del proyecto final lleva además `semana6/`, `.github/skills/verificar-taskflow/casos-assignee.ps1`
y una línea nueva en `verificar.ps1`. Esos archivos los agregas tú en el proceso (PF-2 a PF-7 de la guía),
no el agente: no cuentan contra esta restricción.
````

</details>


---

<sub>28s</sub>

### Copilot

Abrir en paralelo los archivos relevantes del repo para comparar el contexto real con el diff y la spec: TaskController, TaskService, Task, TaskMapper, TaskStateException, TaskNotFoundException y GlobalExceptionHandler. Esto permite verificar alcance, reuso de reglas y manejo de excepciones. Ejecutando lecturas.

---

<sub>28s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\controller\TaskController.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\controller\TaskController.java

<details>
<summary>156 lines</summary>

```
package com.taskflow.controller;

import com.taskflow.dto.TaskRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.dto.TaskStatusUpdateRequest;
import com.taskflow.dto.TaskAssigneeUpdateRequest;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.mapper.TaskMapper;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * TaskController — la PUERTA HTTP de las tareas. HOY se completa el CRUD y la entidad Task deja de
 * salir por la puerta: toda firma habla en DTOs (TaskRequest / TaskResponse).
 *
 * Contrato de responsabilidades (T6): el controller traduce HTTP <-> dominio y delega. Novedad de
 * hoy: además de TaskService, inyecta ProjectService para el cableo Task↔Project (validar que el
 * proyecto exista al crear una tarea bajo su path).
 *
 * La checked TaskValidationException se declara con throws y SUBE al GlobalExceptionHandler (que la
 * mapea a 400). No se atrapa aquí: contraste con la IOException de S1D5 (aquélla se manejaba en su
 * capa porque nadie más podía; ésta tiene un handler global esperándola).
 */
@RestController
@Tag(name = "Tasks", description = "CRUD de tareas: crear bajo un proyecto, leer, reemplazar, cambiar estado y borrar.")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    /** GET /tasks — todas o filtradas por ?status= / ?priority= (stretch). Devuelve TaskResponse. */
    @Operation(summary = "Lista tareas",
            description = "Todas las tareas, o filtradas por ?status= / ?priority= (stretch). Devuelve TaskResponse.")
    @GetMapping("/tasks")
    public List<TaskResponse> getTasks(
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) Priority priority) {
        List<Task> tareas;
        if (status != null) {
            tareas = taskService.porEstado(status);
        } else if (priority != null) {                 // STRETCH: mismo patrón que ?status=
            tareas = taskService.porPrioridad(priority);
        } else {
            tareas = taskService.listar();
        }
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    /**
     * GET /tasks/overdue — lista las tareas vencidas de todos los proyectos.
     * Se añade como ruta literal para que no sea capturada por /tasks/{id}.
     */
    @Operation(summary = "Lista tareas vencidas",
            description = "Lista las tareas cuyo Task.estaVencida() es true, ordenadas por fecha ascendente.")
    @GetMapping("/tasks/overdue")
    public List<TaskResponse> getOverdueTasks() {
        List<Task> vencidas = taskService.vencidas();
        return vencidas.stream().map(TaskMapper::aResponse).toList();
    }

    @Operation(summary = "Lista tareas sin responsable",
            description = "Lista las tareas cuyo assigneeId es null, ordenadas por dueDate ascendente (nulls al final).")
    @GetMapping("/tasks/unassigned")
    public List<TaskResponse> getUnassignedTasks() {
        List<Task> sin = taskService.sinResponsable();
        return sin.stream().map(TaskMapper::aResponse).toList();
    }

    /** GET /tasks/{id} — 200 con TaskResponse, o 404 uniforme (orElseThrow -> advice). */
    @Operation(summary = "Obtiene una tarea por id",
            description = "200 con el TaskResponse; 404 uniforme si el id no existe.")
    @GetMapping("/tasks/{id}")
    public TaskResponse getTaskPorId(@PathVariable("id") Long id) {
        Task task = taskService.buscarPorId(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.aResponse(task);
    }

    /**
     * POST /projects/{projectId}/tasks — crea una tarea bajo un proyecto. 201 + header Location
     * apuntando a donde el recurso SE LEE (/tasks/{id}), no a la URL de creación. @Valid dispara Bean
     * Validation (400 si falla). Cableo: si el proyecto no existe -> 404 (ProjectNotFoundException).
     */
    @Operation(summary = "Crea una tarea dentro de un proyecto",
            description = "El proyecto debe existir (404 si no). La tarea nace en estado TODO.")
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable("projectId") Long projectId,
                                                   @Valid @RequestBody TaskRequest request)
            throws TaskValidationException {
        projectService.buscarPorId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Task creada = taskService.crear(request, projectId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/tasks/{id}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(location).body(TaskMapper.aResponse(creada));
    }

    /** PUT /tasks/{id} — reemplazo COMPLETO. 200 con TaskResponse; 404 si no existe. */
    @Operation(summary = "Reemplaza una tarea por completo",
            description = "Semántica PUT: conserva id, status y projectId; el resto viene del cuerpo.")
    @PutMapping("/tasks/{id}")
    public TaskResponse updateTask(@PathVariable("id") Long id,
                                   @Valid @RequestBody TaskRequest request)
            throws TaskValidationException {
        Task actualizada = taskService.reemplazar(id, request);
        return TaskMapper.aResponse(actualizada);
    }

    /** DELETE /tasks/{id} — 204 No Content; 404 uniforme si no existe (advice). */
    @Operation(summary = "Borra una tarea",
            description = "204 No Content; 404 uniforme si la tarea no existe.")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /tasks/{id}/status — cambia SOLO el estado. 200 con TaskResponse; 404 si no existe; y
     * 422 si la regla de estado lo prohíbe (DONE sin assignee), vía TaskStateException.
     */
    @Operation(summary = "Cambia el estado de una tarea",
            description = "TODO/IN_PROGRESS/DONE. Pasar a DONE sin responsable responde 422.")
    @PatchMapping("/tasks/{id}/status")
    public TaskResponse patchStatus(@PathVariable("id") Long id,
                                    @Valid @RequestBody TaskStatusUpdateRequest request) {
        Task actualizada = taskService.cambiarStatus(id, request.status());
        return TaskMapper.aResponse(actualizada);
    }

    @Operation(summary = "Reasigna el responsable de una tarea",
            description = "Cambia solo el campo assigneeId de la tarea indicada. 404 si no existe; 422 si la tarea ya está DONE.")
    @PatchMapping("/tasks/{id}/assignee")
    public TaskResponse patchAssignee(@PathVariable("id") Long id,
                                      @Valid @RequestBody TaskAssigneeUpdateRequest request) {
        Task task = taskService.buscarPorId(id)
                .orElseThrow(() -> new com.taskflow.exception.TaskNotFoundException(id));
        Task reasignada = taskService.reasignar(task, request.assigneeId());
        return TaskMapper.aResponse(reasignada);
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\service\TaskService.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\service\TaskService.java

<details>
<summary>142 lines</summary>

```
package com.taskflow.service;

import com.taskflow.dto.TaskRequest;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskStateException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.mapper.TaskMapper;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TaskService — la capa de negocio de las tareas. Orquesta; las reglas viven en Task (título 3-120,
 * projectId obligatorio, dueDate no en el pasado al crear, no DONE sin assignee).
 *
 * Evolución de HOY (S2D3) sobre el estado de D2:
 *   - crear(...) ya NO usa un projectId demo (murió el 1L): recibe el projectId del PATH y arma la
 *     entidad con el TaskMapper (que pasa por la factory Task.crear).
 *   - nacen reemplazar (PUT), eliminar (DELETE) y cambiarStatus (PATCH); completar(...) de D1 queda
 *     DELEGANDO en cambiarStatus (refactor de MP-9).
 *   - cambiarStatus TRADUCE la checked TaskValidationException que lanza setStatus a la unchecked
 *     TaskStateException -> el advice la mapea a 422. Sin tocar el dominio.
 *
 * La existencia del proyecto al crear (404) se valida en el controller (contra ProjectService): este
 * service no depende del repositorio de proyectos, y así el filtrado por proyecto vive solo en
 * ProjectService.tareasDe (promesa de D4: "no tocar TaskService").
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea una tarea a partir del DTO de entrada y el projectId del PATH. El TaskMapper pasa por la
     * factory Task.crear (status=TODO, id=null, regla "dueDate no en el pasado"); el repositorio le
     * asigna el id en save. La checked sube al advice si el título o la fecha no cumplen (-> 400).
     */
    public Task crear(TaskRequest request, Long projectId) throws TaskValidationException {
        Task nueva = TaskMapper.aEntidadNueva(request, projectId);
        return repository.save(nueva);
    }

    /**
     * Reemplazo COMPLETO (PUT): busca la tarea (404 si no existe), y arma una tarea rehidratada con el
     * MISMO id, conservando status y projectId actuales; el resto viene del request. save hace upsert.
     */
    public Task reemplazar(Long id, TaskRequest request) throws TaskValidationException {
        Task actual = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        Task reemplazo = TaskMapper.aReemplazo(id, actual.getStatus(), actual.getProjectId(), request);
        return repository.save(reemplazo);
    }

    /**
     * Elimina una tarea (DELETE): existe -> borra (204 en el controller); no existe -> TaskNotFound
     * -> el advice responde 404. El findById previo hace explícito el "no existe" (Optional de S1D4).
     */
    public void eliminar(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        repository.deleteById(id);
    }

    /**
     * Cambia el estado (PATCH): busca la tarea (404 si no existe) y aplica setStatus, que puede lanzar
     * la CHECKED TaskValidationException por la regla "no DONE sin assignee". La CAPTURA y la TRADUCE
     * a la unchecked TaskStateException -> el advice la mapea a 422. El dominio (setStatus) no se toca.
     */
    public Task cambiarStatus(Long id, TaskStatus nuevoStatus) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        try {
            task.setStatus(nuevoStatus);
        } catch (TaskValidationException e) {
            throw new TaskStateException(e.getMessage());
        }
        return repository.save(task);
    }

    /**
     * Reasigna el responsable de una tarea ya encontrada. Lanza TaskStateException si la tarea
     * está en estado DONE (no se pueden reasignar tareas terminadas).
     */
    public Task reasignar(Task tarea, Long assigneeId) {
        if (tarea.getStatus() == TaskStatus.DONE) {
            throw new TaskStateException("No se puede reasignar una tarea terminada.");
        }
        tarea.setAssigneeId(assigneeId);
        return repository.save(tarea);
    }

    /**
     * Completa una tarea (la pasa a DONE). Tras MP-9 queda DELEGANDO en cambiarStatus(id, DONE): un
     * solo camino para "cambiar de estado", que además hereda la traducción a 422.
     */
    public Task completar(Long id) {
        return cambiarStatus(id, TaskStatus.DONE);
    }

    /**
     * Lista todas las tareas ordenadas con la estrategia POR_URGENCIA (reuso LITERAL de la Strategy de
     * S1): vencidas primero, luego prioridad HIGH->LOW, luego fecha ascendente (nulls al final).
     */
    public List<Task> listar() {
        return repository.findAll().stream()
                .sorted(TaskOrders.POR_URGENCIA)
                .toList();
    }

    /** Filtra las tareas por estado (?status=). '==' entre enums es seguro. */
    public List<Task> porEstado(TaskStatus status) {
        return repository.findAll().stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    /** Busca una tarea por id: delega en el repositorio y deja subir el Optional TAL CUAL. */
    public Optional<Task> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /** STRETCH — filtro por prioridad (?priority=): MISMO patrón que porEstado. */
    public List<Task> porPrioridad(Priority priority) {
        return repository.findAll().stream()
                .filter(t -> t.getPriority() == priority)
                .toList();
    }

    /**
     * Devuelve las tareas vencidas según la regla del dominio Task.estaVencida().
     * Ordena por fecha ascendente (TaskOrders.POR_FECHA) tal como especifica specs/overdue.md.
     */
    public List<Task> vencidas() {
        return repository.findAll().stream()
                .filter(Task::estaVencida)
                .sorted(TaskOrders.POR_FECHA)
                .toList();
    }

    /**
     * Devuelve las tareas sin responsable (assigneeId == null). Usa el predicado nombrado
     * ReportService.SIN_ASIGNAR y ordena por fecha ascendente con TaskOrders.POR_FECHA.
     */
    public List<Task> sinResponsable() {
        return repository.findAll().stream()
                .filter(ReportService.SIN_ASIGNAR)
                .sorted(TaskOrders.POR_FECHA)
                .toList();
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\model\Task.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\model\Task.java

<details>
<summary>225 lines</summary>

```
package com.taskflow.model;

import com.taskflow.exception.TaskValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Task — entidad central del dominio TaskFlow. ESTADO FINAL del Día 4 (persistida con JPA).
 *
 * La entidad ES la clase que YA teníamos (canónica S1, 8 campos): NO se escribió un "modelo nuevo",
 * se ANOTÓ el Task de D3. Novedades de HOY, todas por anotación:
 *   - @Entity + @Table(name = "tasks"): esta clase mapea a la tabla TASKS.
 *   - @Id + @GeneratedValue(IDENTITY): el id lo asigna la BD (columna autoincremental). La secuencia
 *     manual del InMemoryTaskRepository (el Math.max de S1D3) MURIÓ: el id lo pone la BD y nadie más.
 *   - @Enumerated(STRING) en status y priority: se guardan como TEXTO ('TODO'), NUNCA como ORDINAL
 *     (0,1,2): reordenar o insertar un enum corrompería datos históricos. El orden LOW,MED,HIGH de
 *     S1D3 es contrato del SORT (compareTo), no del almacenamiento.
 *   - relación @ManyToOne Task->Project (patrón "asociación de solo lectura"): la COLUMNA la sigue
 *     mandando el escalar 'projectId' (por eso Task.crear, TaskService, mappers y DTOs NO cambian);
 *     el objeto 'project' solo NAVEGA. Frase ancla: "la columna manda; el objeto navega".
 *
 * Tensión JPA vs validaciones (paga de S1D2 MP-8, fijada por el apéndice del CAPSTONE): JPA rehidrata
 * por reflexión con un constructor sin args protegido, SIN pasar por las validaciones. Está bien:
 * Task.crear(...) sigue siendo el ÚNICO camino de CREACIÓN de negocio; JPA solo REHIDRATA datos que
 * ya existían (la misma distinción crear/rehidratar de siempre).
 *
 * OJO: las columnas persistentes NO pueden ser 'final' (JPA las escribe por reflexión tras el no-arg);
 * por eso title/description/projectId/dueDate perdieron el 'final' que traían de D3. La inmutabilidad
 * de negocio la sigue garantizando la ausencia de setters públicos para esos campos.
 */
@Entity
@Table(name = "tasks")
public class Task implements Comparable<Task> {

    // Reglas de longitud del título (del capstone).
    private static final int TITULO_MIN = 3;
    private static final int TITULO_MAX = 120;

    // id nace null; lo asigna la BD (IDENTITY). Ver setId (uso interno de JPA/repo).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    private String description;                 // por convención -> columna DESCRIPTION

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;                  // texto 'TODO'/'IN_PROGRESS'/'DONE' (nunca 0/1/2)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;                  // texto 'LOW'/'MED'/'HIGH'

    // La COLUMNA project_id la posee este escalar (insertable/updatable por default). Sigue siendo
    // el dueño: Task.crear, el mapper y el servicio hablan con projectId, no con el objeto.
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // assigneeId se queda Long a secas (juicio explícito): es nullable, NUNCA navegamos al User
    // completo al leer tareas, y las derived queries operan igual. No toda FK necesita ser objeto:
    // mapea objeto cuando NAVEGAS, deja el id cuando solo REFERENCIAS.
    @Column(name = "assignee_id")
    private Long assigneeId;

    private LocalDate dueDate;                  // por convención -> columna DUE_DATE (camelCase->snake_case)

    /**
     * Asociación de SOLO LECTURA hacia el proyecto (MP-8). Comparte la MISMA columna project_id con
     * el escalar de arriba, pero marcada insertable=false/updatable=false: no escribe la columna (el
     * escalar lo hace), solo permite NAVEGAR (task.getProject().getName()). LAZY: no se carga hasta
     * que se toca — y fuera de una sesión abierta, tocarla lanza LazyInitializationException (por eso
     * la API responde con DTOs, nunca con la entidad).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    /**
     * Constructor NO-ARG protegido: EXCLUSIVO de JPA (rehidratación por reflexión). Nunca es un camino
     * de creación de negocio. Su existencia es obligatoria para cualquier @Entity: sin él, al arrancar
     * Hibernate lanza "No default constructor for entity: com.taskflow.model.Task".
     */
    protected Task() {
        // solo para JPA: rehidratación, nunca creación de negocio
    }

    /**
     * Constructor de REHIDRATACIÓN: reconstruye una tarea que YA existía (semilla del DataSeeder,
     * datos releídos). Valida las INVARIANTES que siempre deben cumplirse (título y proyecto), pero
     * NO la regla temporal de dueDate: una tarea vencida es un dato válido que hay que poder releer.
     */
    public Task(Long id, String title, String description, TaskStatus status,
                Priority priority, Long projectId, Long assigneeId, LocalDate dueDate)
            throws TaskValidationException {
        // Invariante 1: título obligatorio, 3-120 chars.
        if (title == null || title.isBlank()) {
            throw new TaskValidationException("El título es obligatorio (no puede ir vacío).");
        }
        if (title.length() < TITULO_MIN || title.length() > TITULO_MAX) {
            throw new TaskValidationException(
                    "El título debe tener entre " + TITULO_MIN + " y " + TITULO_MAX
                            + " caracteres; recibí " + title.length() + ": \"" + title + "\".");
        }
        // Invariante 2: una tarea no puede existir sin proyecto (regla del capstone).
        if (projectId == null) {
            throw new TaskValidationException(
                    "Una tarea no puede existir sin proyecto (projectId == null).");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.dueDate = dueDate;
    }

    /**
     * Factory de CREACIÓN de negocio: añade la regla temporal (dueDate no en el pasado) y delega en
     * el constructor. id nace null, status nace TODO. Firma canónica S1 (apéndice del CAPSTONE-SPEC):
     * la usan el mapper y el servicio tal cual — por eso NO cambia hoy pese a que Task ya sea @Entity.
     */
    public static Task crear(String title, String description, Priority priority,
                             LocalDate dueDate, Long projectId, Long assigneeId)
            throws TaskValidationException {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new TaskValidationException(
                    "La fecha límite no puede estar en el pasado: " + dueDate + ".");
        }
        return new Task(null, title, description, TaskStatus.TODO, priority, projectId, assigneeId, dueDate);
    }

    // ---- Comportamiento: vive CON los datos ----

    /** true si tiene fecha, ya pasó, y la tarea aún no está terminada. */
    public boolean estaVencida() {
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && status != TaskStatus.DONE;
    }

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    /** Uso interno (JPA/repo) para el id autoincremental. Nadie más lo llama a mano. */
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Navega al proyecto asociado (asociación de solo lectura). Fuera de una sesión JPA abierta puede
     * lanzar LazyInitializationException (por eso la API expone DTOs, no la entidad). En el runtime
     * nadie del camino principal lo usa; existe para NAVEGAR cuando haga falta.
     */
    public Project getProject() {
        return project;
    }

    // ---- Setters SOLO donde el dominio muta ----

    /**
     * Cambia el estado aplicando la regla del capstone: no se puede pasar a DONE una tarea sin
     * responsable (assigneeId). La regla vive AQUÍ, no en el servicio ni en el menú.
     */
    public void setStatus(TaskStatus status) throws TaskValidationException {
        if (status == TaskStatus.DONE && assigneeId == null) {
            throw new TaskValidationException(
                    "No se puede marcar como " + TaskStatus.DONE
                            + " una tarea sin responsable: \"" + title + "\".");
        }
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    // ---- Identidad de ENTIDAD por id (S1D3 MP-6 = literalmente lo que hace JPA) ----

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task otra = (Task) o;
        return Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ---- Orden natural por prioridad (Comparable) ----

    @Override
    public int compareTo(Task otra) {
        return this.priority.compareTo(otra.priority);
    }

    @Override
    public String toString() {
        return "Task{id=" + id
                + ", title='" + title + '\''
                + ", status=" + status
                + ", priority=" + priority
                + ", assigneeId=" + (assigneeId == null ? "sin asignar" : assigneeId)
                + ", dueDate=" + (dueDate == null ? "sin fecha" : dueDate)
                + (estaVencida() ? " *VENCIDA*" : "")
                + '}';
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\mapper\TaskMapper.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\mapper\TaskMapper.java

<details>
<summary>48 lines</summary>

```
package com.taskflow.mapper;

import com.taskflow.dto.TaskRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;

/**
 * TaskMapper — el puente entre el CONTRATO (DTOs) y el DOMINIO (entidad Task). Mapeo A MANO con
 * métodos estáticos: barato, visible y testeable. Es una clase de utilidades — constructor privado,
 * no se instancia.
 *
 * Mención 1 min (MP-2): MapStruct automatiza este mapeo en el mundo real; el capstone lo hace a mano
 * primero para ENTENDER el costo antes de esconderlo detrás de una librería.
 *
 * Nota clave "crear vs rehidratar" (paga en HTTP): aEntidadNueva pasa por la FACTORY Task.crear
 * (aplica "dueDate no en el pasado"); aReemplazo pasa por el CONSTRUCTOR de rehidratación (permite
 * editar una tarea vieja con fecha ya pasada) — la misma distinción que releer el CSV en S1.
 */
public final class TaskMapper {

    private TaskMapper() {
        // no instanciable
    }

    /**
     * POST: DTO de entrada + projectId del PATH -> entidad NUEVA por la factory de negocio.
     * Task.crear fija status=TODO e id=null, y aplica la regla temporal de dueDate. Firma canónica
     * del apéndice del capstone: crear(title, description, priority, dueDate, projectId, assigneeId).
     */
    public static Task aEntidadNueva(TaskRequest req, Long projectId) throws TaskValidationException {
        return Task.crear(req.title(), req.description(), req.priority(),
                req.dueDate(), projectId, req.assigneeId());
    }

    /**
     * PUT (reemplazo COMPLETO): DTO de entrada + el estado y el proyecto ACTUALES -> entidad
     * rehidratada con el MISMO id. Conservamos id, status y projectId (el cliente no los toca por
     * PUT); el resto (title, description, priority, assigneeId, dueDate) viene del request. El
     * constructor de rehidratación valida las invariantes (título, projectId) sin la regla temporal.
     */
    public static Task aReemplazo(Long id, TaskStatus statusActual, Long projectIdActual, TaskRequest req)
            throws TaskValidationException {
        return new Task(id, req.title(), req.description(), statusActual, req.priority(),
                projectIdActual, req.assigneeId(), req.dueDate());
    }

    /** Entidad -> DTO de salida (los 8 campos). La entidad Task NO cruza el controller: cruza esto. */
    public static TaskResponse aResponse(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(),
                t.getPriority(), t.getProjectId(), t.getAssigneeId(), t.getDueDate());
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\exception\TaskStateException.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\exception\TaskStateException.java

<details>
<summary>20 lines</summary>

```
package com.taskflow.exception;

/**
 * TaskStateException — el request está BIEN FORMADO, pero el ESTADO del dominio no permite la
 * operación. Nace HOY (S2D3, MP-9) y el GlobalExceptionHandler la mapea a 422 Unprocessable Entity.
 *
 * Por qué existe (decisión canónica del capstone, 422 vs 400): "pasar a DONE una tarea sin
 * responsable" no es un error de FORMATO (eso sería 400) ni de "recurso inexistente" (404): el JSON
 * es impecable, pero la regla de negocio dice "todavía no". 422 comunica exactamente eso: "no
 * reintentes igual — primero asigna a alguien".
 *
 * Es UNCHECKED (extiende RuntimeException): sube sola hasta el advice sin ensuciar las firmas. La
 * regla de negocio sigue viviendo en Task.setStatus (que lanza la CHECKED TaskValidationException de
 * S1); TaskService.cambiarStatus la CAPTURA y la TRADUCE a esta excepción. Así TaskValidationException
 * conserva su significado (400 en creación/edición) y el 422 queda reservado a la regla de estado.
 */
public class TaskStateException extends RuntimeException {

    public TaskStateException(String mensaje) {
        super(mensaje);
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\exception\TaskNotFoundException.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\exception\TaskNotFoundException.java

<details>
<summary>28 lines</summary>

```
package com.taskflow.exception;

/**
 * TaskNotFoundException — se lanza cuando se busca una tarea por id y NO existe.
 *
 * A diferencia de TaskValidationException (checked, para reglas de negocio del input),
 * esta es UNCHECKED (extiende RuntimeException): un id inexistente suele ser un caso de
 * "no encontrado" que se maneja en un punto concreto (el menú), no una condición que TODO
 * método que la toque deba declarar. Nace HOY (S1D4) con la cirugía de Optional/orElseThrow:
 *
 *     Task t = repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
 *
 * Guarda el id que no se encontró (útil para el mensaje y para el log). Forma parte del
 * estado final de S1 y viaja a S2 tal cual: allá el advice REST la mapeará a un 404.
 *
 * Nombre y paquete canónicos de S1 (apéndice del CAPSTONE-SPEC):
 * com.taskflow.exception.TaskNotFoundException.
 */
public class TaskNotFoundException extends RuntimeException {

    private final Long id;

    public TaskNotFoundException(Long id) {
        super("No existe tarea con id " + id + ".");
        this.id = id;
    }

    /** El id que se buscó y no existía. */
    public Long getId() {
        return id;
    }
}
```

</details>


---

<sub>28s</sub>

### `view` — Failed

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\exception\GlobalExceptionHandler.java.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\main\java\com\taskflow\exception\GlobalExceptionHandler.java

```
Path does not exist
```


---

<sub>40s</sub>

### Copilot

Next: list test files in src/test/java/com/taskflow/slice and some unit tests to check annotation patterns. Running parallel reads of two sample tests.

---

<sub>40s</sub>

### `view`

**view the file at C:\Users\sergi\taskflow-copilot-sergioprado7\src\test\java\com\taskflow\slice.**

C:\Users\sergi\taskflow-copilot-sergioprado7\src\test\java\com\taskflow\slice (lines 1--1)

```
ProjectControllerTest.java
ProjectSummaryControllerTest.java
ReasignarTareaControllerTest.java
TaskControllerTest.java
TaskRepositoryTest.java
```


---

<sub>1m 12s</sub>

### Copilot

Resumen breve: la diff implementa exactamente lo pedido por specs/assignee.md: DTO record, controller PATCH, TaskService.reasignar y los dos tests (unit + slice). Hallazgos abajo.

| # | Gravedad | Archivo:línea | Punto checklist | Qué pasa | Qué harías |
|---|---|---:|---|---|---|
| 1 | Sugerencia | src/main/java/com/taskflow/controller/TaskController.java:~155 | Contrato / controller | Usa la referencia totalmente cualificada new com.taskflow.exception.TaskNotFoundException(id) (innecesaria). | Reemplazar por new TaskNotFoundException(id) por consistencia estilística. |
| 2 | Corregir? (info) | src/main/java/com/taskflow/service/TaskService.java:~86 | Reglas del dominio / traducción de error | reasignar lanza TaskStateException con el mensaje esperado ("No se puede reasignar una tarea terminada.") — OK. | Ninguno. |
| 3 | APROBADO | src/main/java/com/taskflow/dto/TaskAssigneeUpdateRequest.java:1 | DTO como record + validación | DTO cumple (@NotNull @Positive), Javadoc presente. | Ninguno. |
| 4 | APROBADO | src/test/java/com/taskflow/unit/ReasignarTareaServiceTest.java:1 | Tests unitarios | Test cubre TODO sin responsable (captor) y DONE (no save). Firma y anotaciones correctas. | Ninguno. |
| 5 | APROBADO | src/test/java/com/taskflow/slice/ReasignarTareaControllerTest.java:1 | Tests slice | WebMvcTest con addFilters=false y @MockitoBean correcto; cubre 200,422,404,400 y verify(never()). | Ninguno. |
| 6 | Bloquea (informativo) | (no pude abrir) GlobalExceptionHandler | Contrato de errores (422/404/400) | No pude abrir el handler para verificar que TaskStateException mapea a 422 y que TaskNotFound -> 404; spec asume que existe. | Confirmar que GlobalExceptionHandler mapea TaskStateException -> 422 y TaskNotFoundException -> 404; si existe, merge OK. |

Casos sin test:
- Ninguno: la spec pedía los cuatro casos en slice y los dos en unit y todos están presentes.

Veredicto: APROBADO (pendiente la verificación del GlobalExceptionHandler mapeo 422/404; si existe como en el resto del proyecto, merge seguro).

---

<sub>Generated by [GitHub Copilot CLI](https://github.com/features/copilot/cli)</sub>
