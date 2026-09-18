<div align="center" style="font-family: Arial, sans-serif; line-height: 1.5;">

<h1 style="font-size: 34px; margin-bottom: 8px;">Academy Backend/Frontend/QE Virtual MTY</h1>

<h2 style="font-size: 26px; margin-top: 8px;"><strong>Proyecto:</strong> GitHub Copilot</h2>

<hr>

<h3 style="font-size: 21px;"><strong>Desarrollador:</strong> Sergio Servando Prado Lozano</h3>

<hr>

<h3 style="font-size: 21px;"><strong>Encargado:</strong> Miguel Ángel Rugerio Flores</h3>

<hr>

<h3 style="font-size: 21px;"><strong>Lugar:</strong> San Pedro de las Colonias Coahuila</h3>

<hr>

<h3 style="font-size: 21px;"><strong>Fecha:</strong> 18/09/2026</h3>

<hr>

</div>

<br>

# Evidencia de la semana · GitHub Copilot

**Alumno:** Sergio Servando Prado Lozano · **Repo:** `https://github.com/SergioPrado7/taskflow-copilot-sergioprado7`

## Reporte de trabajo — GitHub Copilot CLI con TaskFlow

## Datos del reporte

**Repositorio:** `https://github.com/SergioPrado7/taskflow-copilot-sergioprado7.git`  
**Entorno:** Windows 11 + Windows Terminal + PowerShell 7  
**Modelo de Copilot:** `gpt-5-mini`  
**Estado del reporte:** Día 1 completado · Día 2 completado · Día 3 completado · Día 4 completado · Día 5 completado

---

## Índice

- Día 1: instalación, configuración inicial y revisión del repositorio.
- Día 2: especificaciones, implementación, pruebas, Pull Request y merge.
- Día 3: servidores MCP, herramientas externas, revisión de permisos y cierre.
- Día 4: skills, agentes personalizados, endpoint `summary`, auditoría AWS e integrador final.
- Día 5: VS Code, autocompletado, reutilización de `.github/`, MCP en el editor y proyecto final `assignee`.
- Conclusión general.

---

## 1. Objetivo general

El objetivo de estas prácticas fue aprender a usar GitHub Copilot CLI como un apoyo para trabajar con el repositorio. La idea no era dejar que hiciera todo solo, sino revisar lo que proponía, confirmar sus respuestas con comandos y aprobar con cuidado cada acción importante.

Durante los ejercicios trabajé con una copia de `taskflow-api` en mi propio repositorio. En cada parte fui guardando evidencia para demostrar qué hice, qué revisé y cómo confirmé que los cambios realmente funcionaban.

---

# Día 1 — Instalar, entender y conversar con el repositorio

### Resumen de evidencia

- **Qué construí:** preparé mi repositorio de TaskFlow, configuré `.github/copilot-instructions.md` y generé `docs/ARQUITECTURA.md`, que después revisé y corregí.
- **Dónde está:** `.github/copilot-instructions.md`, `docs/ARQUITECTURA.md` y `evidencia/dia1/`.
- **Cómo se comprueba:** `evidencia/dia1/verificador.txt`; su resumen final reporta `0 NO EXISTE`.
- **Qué no salió:** la primera versión de `ARQUITECTURA.md` incluía referencias incorrectas; las detecté con el verificador y las corregí hasta dejar `0 NO EXISTE`.


## 2. Preparación del repositorio

Se creó el repositorio:

`taskflow-copilot-sergioprado7`

La copia se obtuvo desde `taskflow-api` usando `git archive`, evitando copiar archivos no versionados o potencialmente privados como:

- `target/`
- `data/`
- `.env`
- llaves `.pem`, `.ppk` o `.key`
- archivos de IDE

También configuré `.gitignore` con lo que pedía el curso y revisé que Git no estuviera siguiendo secretos.

Después inicialicé el repositorio con la rama `main`, hice el primer commit y subí el proyecto a GitHub.

### Resultado

- Repositorio público creado correctamente.
- Rama principal: `main`.
- Working tree limpio después del push.
- No se versionaron secretos ni carpetas generadas.

### Evidencia registrada

```powershell
git log --oneline --all --decorate -10
```

<p align="center">
  <img src="evidencia/dia1/1.png" alt="Historial de commits del repositorio" width="900">
</p>

<p align="center"><em>En esta captura se ve el historial de commits del repositorio. La usé para mostrar que el proyecto sí quedó guardado en Git y que el trabajo fue avanzando por partes, no todo mezclado al final.</em></p>

---

## 3. Verificación de la suite inicial

Después corrí las pruebas de TaskFlow con Maven para asegurarme de que el proyecto venía funcionando desde el inicio.

La comprobación indicó:

- **67 tests**
- **0 fallos**
- **0 errores**

Con esto confirmé que mi copia del proyecto estaba bien antes de pedirle cambios a Copilot.

### Comprobación realizada

```powershell
mvn test | Select-String -CaseSensitive 'Tests run:.*Skipped: \d+$|BUILD'
```

Esta ejecución sirvió para confirmar que el proyecto estaba funcionando antes de pedirle cambios a Copilot. El resultado reportado fue de 67 pruebas ejecutadas, sin fallos ni errores.

> Volver a ejecutar Maven consume tiempo de máquina, pero no créditos de IA de Copilot.

---

## 4. Configuración de GitHub Copilot CLI

Se configuró el modelo de trabajo con:

```powershell
[Environment]::SetEnvironmentVariable('COPILOT_MODEL','gpt-5-mini','User')
```

Después de reiniciar Windows Terminal revisé:

```powershell
$env:COPILOT_MODEL
```

Resultado esperado y obtenido:

```text
gpt-5-mini
```

Dentro de Copilot CLI también se revisaron:

- `/model`
- `/usage`
- `/context`

Con eso pude ver qué modelo estaba usando, cuántos créditos llevaba y cuánto contexto tenía cargado la sesión.

---

## 5. Preguntas al repositorio y verificación manual

Le hice tres preguntas a Copilot, pero no me quedé solo con sus respuestas. En cada caso revisé el repositorio con comandos para confirmar si lo que decía era cierto.

### 5.1 Organización del proyecto

Copilot explicó el repositorio y posteriormente se verificaron las carpetas reales con:

```powershell
Get-ChildItem src\main\java\com\taskflow -Directory |
    Select-Object -ExpandProperty Name
```

Se encontraron las diez carpetas esperadas:

- `advice`
- `config`
- `controller`
- `dto`
- `exception`
- `mapper`
- `model`
- `repository`
- `security`
- `service`

También se utilizaron comandos `Test-Path` para comprobar que los archivos que Copilot afirmaba haber leído existían realmente.

### 5.2 Regla de tarea vencida

Se preguntó al agente dónde se encontraba la regla que determina si una tarea está vencida.

Después lo revisé con:

```powershell
Get-ChildItem -Recurse -Filter *.java src |
    Select-String -Pattern 'estaVencida' |
    Select-Object Filename, LineNumber
```

Con esa búsqueda encontré dónde aparecía realmente `estaVencida` en el código y pude comparar eso contra la respuesta de Copilot.

### 5.3 Endpoint de tareas vencidas

También se preguntó si existía un endpoint para obtener tareas vencidas.

Se listaron los endpoints reales de los controladores con:

```powershell
Get-ChildItem src\main\java\com\taskflow\controller -Filter *.java |
    Select-String -Pattern '@(Get|Post|Put|Patch|Delete)Mapping\(' |
    Select-Object Filename, LineNumber, Line
```

Con esa revisión vi que todavía **no existía un endpoint específico de tareas vencidas**. Esto era importante porque justo ese endpoint se iba a construir en el Día 2.

---

## 6. Permisos del agente

También practiqué tres casos de permisos para entender cuándo conviene aceptar, negar o deshacer algo que propone el agente.

### Aprobar una vez

Se pidió a Copilot:

> Corre `mvn -q test` y dime solo si terminó bien o con error.

Se aprobó solamente una ejecución, sin guardar un permiso permanente.

### Negar una operación

Se pidió:

> Borra la carpeta `target`.

Cuando Copilot mostró el comando que iba a utilizar, se negó la operación y se indicó que no borrara nada.

Después revisé:

```powershell
Test-Path target
```

Resultado:

```text
True
```

### Deshacer una edición

Se pidió al agente agregar una línea a `README.md`. Después:

1. Se revisó el cambio con `/diff`.
2. Se utilizó `/rewind`.
3. Se eligió **Conversation + files**.
4. Se confirmó con `git status --short` que el archivo había regresado a su estado original.

Este ejercicio me dejó claro que no hay que aceptar permisos sin leerlos, aunque el comando parezca simple.

---

## 7. Instrucciones del proyecto

Se probó `/init` para observar qué instrucciones generaba Copilot automáticamente.

Después ese contenido fue reemplazado por el archivo oficial del curso:

`.github/copilot-instructions.md`

Se verificó dentro de una sesión nueva de Copilot con:

```text
/instructions
```

y el archivo apareció como cargado por el repositorio.

### Evidencia registrada

```powershell
Test-Path .github\copilot-instructions.md
Get-Content .github\copilot-instructions.md -TotalCount 12
```

<p align="center">
  <img src="evidencia/dia1/4.png" alt="Instrucciones de Copilot del curso" width="900">
</p>

<p align="center"><em>Aquí comprobé que el archivo `.github/copilot-instructions.md` sí existía y que tenía las instrucciones del curso. Esto era importante porque Copilot debía trabajar siguiendo esas reglas del repositorio.</em></p>

---

## 8. Documento de arquitectura

Copilot generó:

`docs/ARQUITECTURA.md`

El documento explica la arquitectura de TaskFlow, incluyendo:

- capas y paquetes;
- flujo de creación de tareas;
- reglas de negocio;
- seguridad JWT;
- organización de tests.

No acepté el documento solo porque sonara bien. Usé el verificador del curso para revisar que las clases, métodos, rutas y endpoints que mencionaba sí existieran.

Durante la primera revisión se detectaron referencias incorrectas. Se entregó a Copilot el archivo de evidencia generado por el verificador para que corrigiera únicamente las líneas problemáticas.

Después revisé manualmente el recorrido de:

`POST /projects/{projectId}/tasks`

confirmando que:

1. `TaskController.createTask` comprueba la existencia del proyecto mediante `ProjectService.buscarPorId`.
2. `TaskService.crear` utiliza `TaskMapper.aEntidadNueva`.
3. `TaskRepository.save` persiste la entidad.
4. `TaskController` devuelve `201` usando `TaskMapper.aResponse`.

El resultado final del verificador quedó en:

```text
0 NO EXISTE
```

### Evidencia registrada

```powershell
Get-Content evidencia\dia1\verificador.txt -Tail 10
```

<p align="center">
  <img src="evidencia/dia1/3.png" alt="Verificador de arquitectura con 0 NO EXISTE" width="900">
</p>

<p align="center"><em>Esta captura muestra el resultado del verificador de arquitectura. Lo importante es el `0 NO EXISTE`, porque significa que el documento ya no estaba mencionando clases, métodos o rutas que no estuvieran en el proyecto.</em></p>

---

## 9. Evidencia y consumo del Día 1

Se generó la carpeta:

`evidencia/dia1/`

con los archivos de evidencia indicados por la práctica.

El consumo total del integrador del Día 1 fue aproximadamente:

**18 créditos de IA**

### Evidencia registrada

```powershell
Get-ChildItem evidencia\dia1 | Select-Object Name, Length
Get-Content evidencia\dia1\usage.txt
```

<p align="center">
  <img src="evidencia/dia1/2.png" alt="Evidencia y consumo del Dia 1" width="900">
</p>

<p align="center"><em>En esta parte guardé los archivos de evidencia del Día 1 y el consumo aproximado de créditos. También se ve el modelo usado, `gpt-5-mini`, para dejar claro con qué configuración hice la práctica.</em></p>

---

## 10. Conclusión del Día 1

La principal lección del Día 1 fue que Copilot puede contestar muy seguro, pero aun así se puede equivocar.

Un resultado como:

```text
0 NO EXISTE
```

solo significa que el verificador no encontró nombres de clases, métodos, archivos o endpoints inexistentes.

**No garantiza que todas las relaciones descritas en el documento sean correctas.**

Por eso no bastó con leer la respuesta de Copilot: también tuve que revisar el código y comprobar el flujo real con comandos.

---

# Día 2 — Especificar, implementar y revisar

### Resumen de evidencia

- **Qué construí:** implementé `GET /tasks/overdue` y `GET /tasks/unassigned`, con sus especificaciones, tests, revisión y Pull Request.
- **Dónde está:** `specs/overdue.md`, `specs/unassigned.md`, el código de TaskFlow y `evidencia/dia2/`.
- **Cómo se comprueba:** `evidencia/dia2/comprobacion.txt` muestra `overdue → 7` y `unassigned → 4, 6`; `evidencia/dia2/suite-main.txt` registra la suite final.
- **Qué no salió:** el checklist detectó que un slice test de `overdue` comprobaba un orden preparado por el mock; corregí ese test y volví a comprobar la suite.


## 11. Preparación del Día 2

Antes de comenzar revisé nuevamente:

- PowerShell 7.
- GitHub Copilot CLI.
- modelo `gpt-5-mini`.
- instrucciones de Copilot del Día 1.
- `.gitignore`.
- rama `main` limpia.
- remote de GitHub.
- suite inicial en verde.

También se configuraron los subagentes de Copilot para heredar el mismo modelo mediante `settings.json`.

---

## 12. Especificación antes de implementar

El primer cambio del Día 2 es:

`GET /tasks/overdue`

Antes de pedirle código al agente se creó una rama específica:

```text
feature/overdue
```

y se copió al repositorio:

`specs/overdue.md`

La especificación se guardó en un commit independiente:

```text
spec: GET /tasks/overdue
```

Separé la especificación en su propio commit para que después fuera más fácil saber qué parte era la instrucción original y qué parte fue código generado por Copilot.

### Evidencia registrada

```powershell
git branch --show-current
git log --oneline --decorate -5
Test-Path specs\overdue.md
```

Con esta evidencia confirmé que estaba trabajando en la rama correcta y que la especificación de `overdue` quedó guardada antes de pedirle código a Copilot.

<p align="center">
  <img src="evidencia/dia2/dia2_overdue_01_rama_commits.png" alt="Rama feature overdue y commits iniciales" width="900">
</p>

<p align="center"><em>En esta captura se ve que trabajé en la rama `feature/overdue` y que primero guardé la especificación. Separar ese commit me ayudó a distinguir lo que pedía la práctica de lo que después generó Copilot.</em></p>

---

## 13. Implementación de `GET /tasks/overdue`

Se pidió a Copilot:

> Implementa la especificación de `specs/overdue.md` al pie de la letra. Cuando termines, corre `mvn -q test` y confirma que pasa.

Mientras Copilot trabajaba, fui revisando y aprobando solo lo necesario. No le di permisos permanentes y tampoco dejé que hiciera `git commit` o `git push` por su cuenta.

La implementación modificó los cuatro archivos esperados:

- `TaskController.java`
- `TaskService.java`
- `TaskControllerTest.java`
- `TaskServiceTest.java`

Posteriormente la suite pasó correctamente.

---

## 14. Revisión con checklist

La implementación del agente se guardó temporalmente con el commit:

```text
wip: GET /tasks/overdue tal como lo dejo el agente
```

Después se empezó a revisar el cambio contra `main` con el checklist del curso.

En la revisión fui comprobando:

1. alcance del cambio;
2. que no se borren tests existentes;
3. comentarios agregados;
4. que los tests realmente detecten cambios de comportamiento;
5. convenciones y reutilización de reglas existentes;
6. suite completa.

La salida se fue acumulando en:

`evidencia/dia2/checklist-overdue.txt`

---

## 15. Hallazgo en el punto 4 del checklist

El único problema encontrado hasta el momento apareció en el **punto 4**, relacionado con la forma en que el slice test comprobaba el orden.

El comando:

```powershell
git diff main -- src/test/java/com/taskflow/slice |
    Select-String -NoEmphasis '^\+.*\$\[1\]'
```

encontró:

```java
.andExpect(jsonPath("$[1].id").value(21));
```

Esto indicaba que el test del controlador estaba comprobando el segundo elemento de una lista construida por el propio mock.

Ese test no demostraba que `vencidas()` ordenara bien las tareas, porque el mock ya entregaba la lista en el orden esperado.

### Corrección

Se envió a Copilot un follow-up específico para modificar **solo las líneas nuevas de esta rama**:

- el mock debía devolver una sola tarea;
- el test debía comprobar `200`, `id` y `title`;
- el nombre del test no debía afirmar que estaba comprobando orden;
- no se debían modificar tests anteriores.

Después de la corrección se volvió a ejecutar la búsqueda de `$[1]` y ya no produjo salida.

El punto quedó corregido satisfactoriamente.

### Evidencia del fallo encontrado

El hallazgo quedó registrado en el archivo del checklist. Para comprobarlo, revisé la evidencia guardada con este comando:

```powershell
Select-String -Path evidencia\dia2\checklist-overdue.txt -Pattern '\$\[1\]'
```

Esta salida muestra la línea donde el test revisaba `$[1]`, es decir, el segundo elemento de una lista preparada por el mock.

<p align="center">
  <img src="evidencia/dia2/dia2_overdue_02_hallazgo_punto4.png" alt="Hallazgo del punto 4 del checklist" width="900">
</p>

<p align="center"><em>Aquí quedó registrado el problema que encontré en la revisión. El test estaba revisando `$[1]`, pero ese orden venía preparado desde el mock, así que no probaba de verdad la lógica del servicio.</em></p>

### Evidencia de la corrección

Después de corregir el test, repetí la búsqueda sobre el diff contra `main`:

```powershell
git diff main -- src/test/java/com/taskflow/slice |
    Select-String -NoEmphasis '^\+.*\$\[1\]'
```

Después del ajuste, el mismo comando ya no encontró líneas nuevas con `$[1]`. Esto confirmó que el test dejó de validar un segundo elemento artificial.

<p align="center">
  <img src="evidencia/dia2/dia2_overdue_03_correccion_punto4.png" alt="Corrección del punto 4 sin resultados con indice uno" width="900">
</p>

<p align="center"><em>Después de corregir el test repetí la búsqueda. Como ya no salió ninguna línea con `$[1]`, confirmé que se quitó esa validación que podía dar una falsa sensación de seguridad.</em></p>

---

## 16. Cierre de `GET /tasks/overdue`

Hasta este momento:

- se creó `feature/overdue`;
- se agregó y versionó la especificación;
- Copilot implementó el endpoint;
- la suite pasó;
- comenzó la revisión punto por punto;
- se detectó un defecto real en el punto 4;
- se corrigió mediante un follow-up específico;
- se dejó lista la base para continuar con la segunda feature del Día 2.

Con esto quedó cerrada la primera parte del Día 2 antes de pasar a `GET /tasks/unassigned`.

---

## 17. Segunda feature: planificación de `GET /tasks/unassigned`

Después de terminar la revisión de `GET /tasks/overdue`, se comenzó la segunda feature del Día 2: `GET /tasks/unassigned`.

La rama `feature/unassigned` se creó a partir de `feature/overdue` porque ambas funcionalidades modifican los mismos cuatro archivos principales. Antes de permitir cambios en el código, se agregó `specs/unassigned.md` en un commit independiente.

### 17.1 Rama y especificación

La rama de trabajo es:

```text
feature/unassigned
```

La especificación se versionó con el commit:

```text
spec: GET /tasks/unassigned
```

<p align="center">
  <img src="evidencia/dia2/dia2_mp6_01_rama_y_spec_unassigned.png" alt="Rama feature unassigned y commit de especificacion" width="900">
</p>

<p align="center"><em>Esta captura muestra la rama `feature/unassigned` y el commit de la especificación. Igual que antes, primero dejé claro qué se iba a construir antes de pedirle código a Copilot.</em></p>

### 17.2 Primer plan generado por Copilot

Antes de implementar se utilizó `/plan`. Este modo permitió revisar la propuesta del agente antes de autorizar ediciones. El primer resumen incluyó el método `sinResponsable`, el endpoint `GET /tasks/unassigned`, el uso de `ReportService.SIN_ASIGNAR` y `TaskOrders.POR_FECHA`, además de los tests unitarios y de slice.

Sin embargo, al revisar el plan completo se detectó que todavía no especificaba los casos concretos exigidos para comprobar el orden en el test unitario.

<p align="center">
  <img src="evidencia/dia2/dia2_mp6_02_plan_ready_for_review.png" alt="Primer plan de Copilot para unassigned" width="900">
</p>

<p align="center"><em>Aquí se ve el primer plan que propuso Copilot para `GET /tasks/unassigned`. La idea general estaba bien, pero todavía le faltaban casos concretos para comprobar el orden de las tareas.</em></p>

### 17.3 Corrección del plan antes de implementar

En lugar de aceptar el primer plan, se solicitó una corrección con `Suggest changes`. Se indicó que el test unitario debía contemplar explícitamente:

- una tarea sin responsable con fecha en 10 días;
- una tarea con responsable;
- una tarea sin responsable y sin fecha;
- una tarea sin responsable con fecha en 2 días;
- el resultado esperado en el orden: 2 días, 10 días y finalmente sin fecha;
- comparación de los ids en ese orden.

Copilot regeneró el plan incorporando esos casos. Además mantuvo las restricciones importantes: reutilizar `ReportService.SIN_ASIGNAR` y `TaskOrders.POR_FECHA`, no modificar `SecurityConfig`, no cambiar tests existentes y no hacer commits ni push por su cuenta.

<p align="center">
  <img src="evidencia/dia2/dia2_mp6_04_plan_no_modifica_codigo.png" alt="Plan corregido antes de modificar codigo" width="900">
</p>

<p align="center"><em>En esta captura ya aparece el plan corregido. Lo bueno de hacerlo así fue que pude pedir ajustes antes de que Copilot tocara archivos del proyecto.</em></p>

Este paso fue útil porque pude corregir el plan **antes de que Copilot tocara los archivos del repositorio**. Si hubiera pedido código directo, ese ajuste habría llegado más tarde.

### 17.4 Implementación inicial

Después de aceptar el plan corregido, Copilot realizó los cambios de la segunda feature en los archivos esperados del controlador, servicio y pruebas.

<p align="center">
  <img src="evidencia/dia2/dia2_mp7_01_archivos_modificados_unassigned.png" alt="Archivos modificados por unassigned" width="900">
</p>

<p align="center"><em>Después de aceptar el plan, Copilot modificó los archivos necesarios para `GET /tasks/unassigned`. Esta captura me sirvió para revisar que no se hubiera salido del alcance esperado.</em></p>

### 17.5 Revisión posterior

Después de la implementación continué con la revisión sin usar `autopilot`, para mantener el control sobre cada edición y cada comando ejecutado.

En esta parte fui guardando evidencia de los archivos modificados, la prueba de mutación, la suite completa, los commits y la revisión final.

---


## 17.6 Experimento controlado: romper la regla de vencimiento

Después de completar la segunda feature, se realizó un experimento en una rama desechable para comprobar si los tests nuevos realmente detectaban un error en la lógica de negocio.

La rama temporal utilizada fue:

```text
experimento/rompelo
```

Se modificó intencionalmente `Task.estaVencida()` cambiando la condición correcta `isBefore` por `isAfter`. La finalidad fue introducir un bug real en producción sin modificar los tests.

### 17.6.1 El bug es detectado por los tests

Después de introducir el cambio se ejecutó la suite. El resultado esperado fue `BUILD FAILURE`, específicamente en los tests relacionados con `vencidas()`.

<p align="center">
  <img src="evidencia/dia2/dia2_mp8_01_bug_detectado_por_tests.png" alt="Bug intencional detectado por los tests" width="900">
</p>

<p align="center"><em>Aquí rompí a propósito la regla de `Task.estaVencida()`. La suite falló, y eso fue bueno porque demostró que los tests sí detectaban un error real en la lógica.</em></p>

### 17.6.2 Copilot corrige producción sin modificar los tests

Después se limpió el contexto de Copilot y se utilizó un prompt deliberadamente abierto:

> Los tests fallan, haz que pasen.

Comparé la respuesta del agente contra `feature/unassigned` para comprobar que no modificara archivos de `src/test` y que el arreglo estuviera en el código de producción.

<p align="center">
  <img src="evidencia/dia2/dia2_mp9_02_agente_corrige_codigo_no_tests.png" alt="Copilot corrige produccion sin cambiar tests" width="900">
</p>

<p align="center"><em>En esta comparación revisé que Copilot arreglara el código de producción y no los tests. Esto era importante porque cambiar los tests para que pasen no significa que el bug esté corregido.</em></p>

### 17.6.3 Verificación de la corrección y suite verde

Se comprobó que la condición incorrecta `isAfter` ya no estuviera presente en `Task.java` y posteriormente se ejecutó nuevamente la suite.

<p align="center">
  <img src="evidencia/dia2/dia2_mp9_03_bug_corregido_suite_verde.png" alt="Bug corregido y suite en verde" width="900">
</p>

<p align="center"><em>Esta captura muestra que la condición incorrecta ya no estaba y que la suite volvió a pasar. Con eso confirmé que el arreglo sí había quedado bien.</em></p>

### 17.6.4 Cierre y eliminación de la rama experimental

El experimento no debía formar parte de la feature real. Por ello se restauró `src` desde `feature/unassigned`, se regresó a esa rama y se eliminó `experimento/rompelo`.

<p align="center">
  <img src="evidencia/dia2/dia2_mp9_04_experimento_descartado.png" alt="Rama experimental descartada" width="900">
</p>

<p align="center"><em>Al final borré la rama experimental y regresé a `feature/unassigned`. Como ese bug solo era una prueba, no debía entrar al Pull Request real.</em></p>

Este ejercicio me ayudó a ver que no basta con que las pruebas terminen en verde. También hay que revisar **qué archivos cambió Copilot** y confirmar que arregló el código real, no que hizo más débiles los tests.

---


## 17.7 Integrador final: Pull Request, revisión, merge y prueba real

Después de completar y revisar las dos funcionalidades del Día 2, pasé el trabajo de `feature/unassigned` a `main` usando un Pull Request. En esta parte dejé evidencia de la revisión final, el merge y la prueba real de los endpoints con la aplicación corriendo.

### 17.7.1 Pull Request creado

Se hizo `push` de `feature/unassigned` y se abrió un Pull Request con `main` como rama base. El PR incluye las dos especificaciones y los cuatro archivos Java modificados durante el Día 2.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_01_pull_request_creado.png" alt="Pull Request creado para overdue y unassigned" width="900">
</p>

<p align="center"><em>Aquí se ve el Pull Request hacia `main`. En ese PR junté las dos funcionalidades trabajadas en el Día 2: tareas vencidas y tareas sin asignar.</em></p>

### 17.7.2 Revisión de código con Copilot

Desde GitHub se solicitó a Copilot una revisión del Pull Request. Los comentarios generados se tomaron como observaciones que debían contrastarse con la especificación y el checklist, no como instrucciones que debían aceptarse automáticamente.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_02_copilot_code_review.png" alt="Revision de Copilot en el Pull Request" width="900">
</p>

<p align="center"><em>En esta parte pedí una revisión de Copilot sobre el Pull Request. Sus comentarios no los acepté automáticamente; los usé como puntos para revisar contra la especificación.</em></p>

### 17.7.3 Comentarios de revisión atendidos desde la CLI

Los comentarios del PR se revisaron desde Copilot CLI. Solo se aplicaron los que no contradecían `specs/overdue.md`, `specs/unassigned.md`, el checklist ni las restricciones de archivos permitidos.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_03_revision_atendida_cli.png" alt="Comentarios atendidos desde Copilot CLI" width="900">
</p>

<p align="center"><em>Desde la CLI revisé los comentarios del PR. Solo apliqué los cambios que tenían sentido con la especificación y con el checklist del curso.</em></p>

### 17.7.4 Validación después de atender la revisión

Después de aplicar las correcciones válidas se verificaron las frases ambiguas de `specs/overdue.md`, que no se hubieran eliminado tests existentes y que la suite continuara en verde.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_04_revision_validada.png" alt="Validacion despues del code review" width="900">
</p>

<p align="center"><em>Después de atender la revisión volví a validar el cambio. Revisé que no se rompiera la especificación y que no se hubieran quitado pruebas importantes.</em></p>

### 17.7.5 Commit de los cambios de revisión

Las correcciones derivadas de la revisión se guardaron en un commit independiente antes de actualizar el Pull Request.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_05_commit_review.png" alt="Commit de cambios por code review" width="900">
</p>

<p align="center"><em>Guardé los cambios del code review en un commit separado. Así quedó más claro qué parte venía de la implementación original y qué parte fue ajuste de revisión.</em></p>

### 17.7.6 Conversaciones del Pull Request resueltas

Los comentarios del Pull Request se respondieron indicando si fueron aplicados o por qué no correspondían, y después se resolvieron las conversaciones.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_06_comentarios_pr_resueltos.png" alt="Comentarios del Pull Request resueltos" width="900">
</p>

<p align="center"><em>En esta captura ya aparecen las conversaciones del Pull Request resueltas. Antes de hacer merge dejé contestados los comentarios de revisión.</em></p>

### 17.7.7 Merge del Pull Request

Una vez revisados los cambios se realizó el merge del Pull Request hacia `main`.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_07_pr_merged.png" alt="Pull Request fusionado a main" width="900">
</p>

<p align="center"><em>Aquí se ve que el Pull Request ya fue fusionado a `main`. Esto marca el cierre de la implementación del Día 2 en la rama principal.</em></p>

### 17.7.8 Suite final en `main`

Después del merge se actualizó la rama local `main` y se volvió a ejecutar la suite completa para comprobar que la integración final permaneciera estable.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_08_main_suite_success.png" alt="Suite completa en main despues del merge" width="900">
</p>

<p align="center"><em>Después del merge corrí otra vez la suite completa en `main`. La captura muestra que todo seguía pasando ya con los cambios integrados.</em></p>

### 17.7.9 Aplicación arrancada con el perfil H2

Para comprobar el comportamiento real de la aplicación, se inició TaskFlow con el perfil `h2` y se esperó a que Spring Boot terminara de arrancar.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_09_app_h2_arrancada.png" alt="Aplicacion TaskFlow arrancada con perfil H2" width="900">
</p>

<p align="center"><em>Aquí arranqué la aplicación con el perfil `h2`. Necesitaba tener TaskFlow corriendo para probar los endpoints de forma real, no solo con tests.</em></p>

### 17.7.10 Comprobación real de los endpoints

Con la aplicación arrancada inicié sesión, obtuve un JWT y consulté los dos endpoints desarrollados. También revisé que una petición sin token fuera rechazada.

Los resultados esperados fueron:

```text
overdue:    7
unassigned: 4, 6
sin token:  401
```

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_10_endpoints_reales_funcionando.png" alt="Endpoints overdue y unassigned funcionando" width="900">
</p>

<p align="center"><em>Esta captura muestra la prueba real de los endpoints `overdue` y `unassigned`. También comprobé que, si no se manda token, la API responde `401`, como debe pasar.</em></p>

### 17.7.11 Evidencia y consumo del Día 2

Al final se generaron los archivos de evidencia del Día 2 y se comparó el contador del plan con el valor guardado al comenzar la jornada para estimar el consumo total.

<p align="center">
  <img src="evidencia/dia2/dia2_integrador_11_evidencia_y_creditos.png" alt="Evidencia final y consumo de creditos del Dia 2" width="900">
</p>

<p align="center"><em>Al cierre del Día 2 guardé la evidencia final y comparé el consumo de créditos. Con esto dejé registrado cuánto se usó durante esa parte de la práctica.</em></p>

---


## 17.8 Limpieza final del Día 2

Al terminar el integrador se realizó la limpieza del entorno de trabajo para dejar el repositorio y las herramientas en un estado final consistente.

Primero apagué la aplicación de Spring Boot y revisé que `http://localhost:8080/info` ya no estuviera disponible. Después eliminé las ramas locales que ya se habían integrado con el Pull Request:

```powershell
git branch -d feature/overdue feature/unassigned
```

La rama activa quedó únicamente en:

```text
main
```

También revisé la rama remota y, si todavía existía `origin/feature/unassigned`, la eliminé después del merge. Al final borré el archivo temporal que había usado para guardar el consumo inicial del Día 2:

```powershell
Remove-Item $HOME\usage-dia2-inicio.txt
```

La verificación final incluyó:

```powershell
git branch --show-current
git branch
git status
git log --oneline -3
Test-Path $HOME\usage-dia2-inicio.txt
```

Con esta revisión final confirmé que:

- la rama actual fuera `main`;
- las ramas locales de las features ya integradas hubieran sido eliminadas;
- el working tree estuviera limpio;
- el archivo temporal de consumo ya no existiera;
- la sesión de Copilot pudiera cerrarse con `/exit`.

<p align="center">
  <img src="evidencia/dia2/dia2_limpieza_01_estado_final_limpio.png" alt="Estado final limpio del repositorio" width="900">
</p>

<p align="center"><em>Esta captura es el cierre del Día 2. Se ve que quedé en `main`, que borré las ramas de trabajo, que el repositorio quedó limpio y que también eliminé el archivo temporal de consumo.</em></p>

Con esta limpieza se cerró el trabajo técnico del Día 2 dejando únicamente en el repositorio los cambios y evidencias que sí forman parte de la práctica.

---

# Día 3 — MCP: darle herramientas al agente

### Resumen de evidencia

- **Qué construí:** configuré y probé servidores MCP, desarrollé `taskflow-mcp` en Java y trabajé con GitHub MCP, AWS Knowledge y Playwright.
- **Dónde está:** `taskflow-mcp/`, `issues/summary.md` y `evidencia/dia3/`.
- **Cómo se comprueba:** `evidencia/dia3/mcp-list.txt`, `issue-summary.txt`, `playwright-tarea.txt`, `integrador.md` y `conteos.txt` documentan y permiten auditar los resultados.
- **Qué no salió:** en la prueba controlada de AWS Knowledge, el argumento `product` no filtró como esperaba; repetí la llamada con `filters` y comparé directamente la respuesta del servidor.


## 18. MCP, herramientas y revisión

El objetivo del Día 3 fue darle más herramientas a GitHub Copilot CLI usando **MCP (Model Context Protocol)**, pero sin perder el control de lo que hacía. En esta parte usé cuatro herramientas principales: GitHub MCP, `aws-knowledge`, Playwright y un servidor MCP propio hecho en Java para TaskFlow.

La idea más importante fue entender que no basta con que el agente diga que usó una herramienta. También hay que revisar cuál herramienta llamó, qué datos le mandó y qué respondió realmente.

### 18.1 Preparación inicial del Día 3

Antes de registrar servidores revisé que el trabajo empezara desde `main`, con el repositorio limpio, los materiales del curso actualizados y los endpoints del Día 2 presentes. También confirmé Copilot CLI, `gpt-5-mini`, la herencia de modelo de los subagentes, el estado público del repositorio y la disponibilidad de `npx`.

<p align="center">
  <img src="evidencia/dia3/dia3_01_inicio_main_limpio.png" alt="Inicio del Día 3 en main y repositorio limpio" width="900">
</p>

<p align="center"><em>Empecé el Día 3 desde `main` y con el repositorio limpio. Esta captura sirve para demostrar que antes de configurar MCP no había cambios pendientes.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_02_academymty_actualizado.png" alt="Repositorio academyMty actualizado" width="900">
</p>

<p align="center"><em>Aquí actualicé los materiales del curso y revisé que estuvieran las carpetas necesarias para el Día 3, como `issues` y el proyecto `taskflow-mcp`.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_03_checklist_repo_y_endpoints.png" alt="Checklist inicial del repositorio y endpoints" width="900">
</p>

<p align="center"><em>En este checklist confirmé que `main` estaba al día y que los endpoints del Día 2 ya existían. Era importante empezar MCP sobre una base que ya funcionara.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_04_checklist_herramientas.png" alt="Checklist de herramientas del Día 3" width="900">
</p>

<p align="center"><em>Esta captura muestra la revisión del entorno: Copilot CLI, el modelo `gpt-5-mini`, los subagentes, el repositorio público y `npx`. Todo eso se necesitaba antes de seguir.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_05_taskflow_mcp_compilado.png" alt="Servidor taskflow-mcp compilado" width="900">
</p>

<p align="center"><em>Aquí compilé el servidor `taskflow-mcp`. La idea era confirmar que sí se generaba el `.jar` antes de conectarlo con Copilot.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_06_playwright_mcp_disponible.png" alt="Playwright MCP disponible" width="900">
</p>

<p align="center"><em>Antes de registrar Playwright MCP, probé que pudiera ejecutarse con `npx`. Así evitaba configurar una herramienta que después no arrancara.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_07_taskflow_h2_arrancada.png" alt="TaskFlow H2 arrancada" width="900">
</p>

<p align="center"><em>En esta captura TaskFlow ya está corriendo con H2. Esto era necesario porque Playwright y mi servidor MCP iban a trabajar contra una aplicación real.</em></p>

### 18.2 MP-1 — Servidor GitHub MCP integrado

Primero se ejecutó `copilot mcp list`. Aunque la CLI indicó que no había servidores configurados manualmente, el panel `/mcp` mostró que `github-mcp-server` ya venía integrado como servidor **Built-in**. Esto permitió distinguir entre servidores registrados por el usuario y herramientas integradas en la propia CLI.

<p align="center">
  <img src="evidencia/dia3/dia3_mp1_01_mcp_list_sin_configurados.png" alt="Lista MCP sin servidores configurados" width="900">
</p>

<p align="center"><em>Primero revisé la lista de servidores MCP. No había servidores agregados por mí, y eso me sirvió como punto de partida antes de configurar nuevos.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp1_02_github_mcp_builtin.png" alt="GitHub MCP integrado en Copilot" width="900">
</p>

<p align="center"><em>Aunque yo no había registrado servidores, Copilot ya traía integrado `github-mcp-server`. Con eso podía trabajar con issues de GitHub desde la CLI.</em></p>

### 18.3 MP-2 y MP-3 — Publicar la especificación mediante GitHub MCP

Se preparó `issues/summary.md`, que contiene la especificación del endpoint que se implementará posteriormente: `GET /projects/{id}/summary`. La especificación se copió al repositorio y se guardó evidencia del estado inicial de los servidores MCP.

Para crear el issue fue necesario abrir una sesión con las herramientas de escritura de GitHub habilitadas. Antes de autorizar `issue_write` se revisaron sus argumentos, verificando el método `create`, el propietario `sergioprado7`, el repositorio correcto y el título exacto `GET /projects/{id}/summary`.

Después de la creación se auditó el resultado directamente con la API pública de GitHub. El número del issue quedó guardado en `evidencia/dia3/issue-summary.txt` y `Compare-Object` no produjo diferencias, confirmando que el cuerpo remoto era idéntico a `issues/summary.md`.

<p align="center">
  <img src="evidencia/dia3/dia3_mp2_01_spec_summary_preparada.png" alt="Especificación summary preparada" width="900">
</p>

<p align="center"><em>Antes de crear el issue en GitHub, preparé localmente la especificación de `GET /projects/{id}/summary`. Así sabía exactamente qué contenido se iba a publicar.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp3_01_permiso_issue_write.png" alt="Permiso de issue_write auditado" width="900">
</p>

<p align="center"><em>Esta captura muestra el permiso de escritura antes de aceptarlo. Revisé el método, el dueño, el repositorio y el título para no crear algo equivocado en GitHub.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp3_02_issue_creado_por_mcp.png" alt="Issue creado por GitHub MCP" width="900">
</p>

<p align="center"><em>Aquí ya se creó el issue usando GitHub MCP. Guardé esta evidencia porque muestra el número y la URL que después se podían revisar directamente.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp3_03_issue_auditado_api.png" alt="Issue auditado mediante API" width="900">
</p>

<p align="center"><em>Después no me quedé solo con lo que dijo Copilot: revisé el issue con la API pública de GitHub para confirmar que el título y el contenido fueran correctos.</em></p>

### 18.4 MP-4, MP-5 y MP-6 — AWS Knowledge MCP y revisión del resultado

Se registró `aws-knowledge` como servidor MCP remoto por HTTP. En una sesión nueva se consultó la disponibilidad de Amazon DynamoDB y AWS CodeDeploy en la región utilizada durante la práctica, **`us-east-1`**. La sesión se exportó a `evidencia/dia3/aws-knowledge.md` para revisar lo que realmente ocurrió.

Al revisar el transcript encontré dos llamadas a la herramienta de disponibilidad regional. En esta ejecución Copilot usó bien el argumento `filters`: una llamada para Amazon DynamoDB y otra para AWS CodeDeploy. Las respuestas traían `isAvailableIn`, así que la respuesta del agente sí estaba apoyada por datos de la herramienta.

Después repetí la operación directamente contra el endpoint JSON-RPC, sin modelo. Con `filters` salió el resultado esperado. Luego probé a propósito el caso incorrecto usando `product`. Esa llamada devolvió **23.5 KB**, **433 productos** y `Amazon DynamoDB` no apareció en la página recibida. Con eso entendí que `product` no estaba funcionando como filtro y que una respuesta puede sonar correcta, pero no estar bien respaldada por la herramienta.

<p align="center">
  <img src="evidencia/dia3/dia3_mp4_01_aws_mcp_registrado.png" alt="AWS Knowledge MCP registrado" width="900">
</p>

<p align="center"><em>Aquí registré `aws-knowledge` como servidor MCP remoto. Primero confirmé que Copilot lo reconociera antes de usarlo para preguntas sobre AWS.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp4_02_aws_respuesta_copilot.png" alt="Respuesta de Copilot usando AWS Knowledge" width="900">
</p>

<p align="center"><em>En esta consulta pregunté por DynamoDB y CodeDeploy en `us-east-1`. Lo importante es que la respuesta debía venir de la herramienta de AWS, no solo de lo que el modelo recordara.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp4_03_transcript_aws_exportado.png" alt="Transcript AWS exportado" width="900">
</p>

<p align="center"><em>Luego exporté el transcript de la sesión. Lo hice para poder revisar con calma qué herramienta se usó, con qué argumentos y qué respondió.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp5_01_auditoria_transcript_aws.png" alt="Auditoría del transcript AWS" width="900">
</p>

<p align="center"><em>Al revisar el transcript vi que Copilot sí usó `filters` y que la respuesta traía `isAvailableIn`. Eso me ayudó a confirmar que la respuesta estaba bien respaldada.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp6_01_aws_llamada_correcta_sin_modelo.png" alt="Llamada correcta a AWS sin modelo" width="900">
</p>

<p align="center"><em>También repetí la llamada directamente por JSON-RPC, sin pasar por el modelo. Así pude comprobar cómo respondía realmente el servidor MCP.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp6_02_argumento_product_ignorado.png" alt="Argumento product ignorado por AWS Knowledge" width="900">
</p>

<p align="center"><em>En esta prueba usé un argumento incorrecto, `product`, y el servidor devolvió demasiada información sin filtrar. Esto muestra que no basta decir “usé una herramienta”; hay que revisar si se usó bien.</em></p>

### 18.5 MP-7 y MP-8 — Playwright MCP: usar la interfaz de TaskFlow

Registré Playwright como servidor MCP local con `--isolated`. También revisé que `.playwright-mcp/` estuviera ignorado por Git y que Chrome estuviera disponible.

Para obligar al agente a usar la interfaz real de TaskFlow, la sesión se abrió con Playwright autorizado pero con `browser_evaluate` y `browser_run_code_unsafe` explícitamente denegados. El agente inició sesión en TaskFlow, abrió el proyecto 1 y creó desde la UI la tarea `Revisar accesibilidad del login` con prioridad `HIGH`.

Después exporté el transcript a `evidencia/dia3/playwright.md` y revisé la tarea por REST, guardando el resultado en `playwright-tarea.txt`. La búsqueda en el transcript no encontró ejecuciones válidas de `browser_evaluate` ni de `browser_run_code_unsafe`, así que la operación sí se hizo por la interfaz y no con JavaScript directo o un `fetch`.

<p align="center">
  <img src="evidencia/dia3/dia3_mp7_01_playwright_mcp_registrado.png" alt="Playwright MCP registrado" width="900">
</p>

<p align="center"><em>Aquí registré Playwright MCP y confirmé que Chrome estuviera disponible. Esto era necesario para que el agente pudiera manejar la interfaz de TaskFlow.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp8_01_copilot_playwright_restringido.png" alt="Copilot con Playwright restringido" width="900">
</p>

<p align="center"><em>En esta sesión permití usar el navegador, pero bloqueé `browser_evaluate` y `browser_run_code_unsafe`. Así obligué a Copilot a usar la interfaz, no atajos con JavaScript.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp8_02_playwright_operando_ui.png" alt="Playwright operando la UI de TaskFlow" width="900">
</p>

<p align="center"><em>Esta captura muestra a Playwright usando Chrome para entrar a TaskFlow y crear una tarea desde la interfaz. La idea era probar la app como usuario, no con llamadas escondidas.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp8_03_playwright_sesion_exportada.png" alt="Sesión Playwright exportada" width="900">
</p>

<p align="center"><em>Exporté el transcript de Playwright para dejar registro de lo que hizo el navegador. Así podía revisar después cada paso de la sesión.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp8_04_tarea_creada_verificada_rest.png" alt="Tarea de Playwright verificada por REST" width="900">
</p>

<p align="center"><em>Después de crear la tarea desde la UI, la revisé por REST. Con eso confirmé que sí quedó guardada con el título correcto, prioridad `HIGH` y estado `TODO`.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp8_05_sin_evaluate_ni_codigo_inseguro.png" alt="Sin evaluate ni código inseguro" width="900">
</p>

<p align="center"><em>En esta revisión confirmé que no se usó `browser_evaluate` ni código inseguro. Esto me ayudó a comprobar que la tarea se hizo usando la interfaz.</em></p>

### 18.6 MP-9 a MP-13 — Servidor MCP propio en Java

El proyecto `taskflow-mcp` es un servidor Maven separado dentro del repositorio. Antes de registrarlo se comprobaron sus pruebas: `TaskflowClientTest`, `TaskflowToolsTest` y `VencidasTest`, con **13 tests en total**, todos sin fallos ni errores y sin necesidad de tener la API real arrancada.

También se inspeccionaron las anotaciones de `TaskflowTools.java`. Las dos herramientas de lectura están marcadas con `readOnlyHint = true`, mientras que `crear_tarea` está marcada como escritura. Esto explica por qué las consultas pueden ejecutarse sin diálogo y la creación requiere autorización.

El servidor se registró como `taskflow (local)` usando la ruta absoluta de `taskflow-mcp.jar`. En una sesión nueva se utilizó para listar tareas vencidas y para crear la tarea `Probar el servidor MCP propio` en el proyecto `App Móvil`, con prioridad `MED` y fecha límite `2026-09-30`. Antes de aprobar la escritura se revisaron los argumentos y posteriormente ambas operaciones se verificaron directamente contra la API.

Finalmente, apagué la API a propósito. `listar_tareas_vencidas` falló y el mensaje de error del servidor sugería cómo arrancar TaskFlow. No dejé que el agente ejecutara esa instrucción y revisé aparte que el puerto siguiera apagado. Este ejercicio mostró que incluso **un mensaje de error de una herramienta debe tratarse con cuidado** cuando vuelve al contexto del modelo.

<p align="center">
  <img src="evidencia/dia3/dia3_mp9_01_tests_taskflow_mcp.png" alt="Tests del servidor MCP propio" width="900">
</p>

<p align="center"><em>Antes de registrar mi servidor MCP propio, corrí sus pruebas. Pasaron 13 tests, así que tenía más confianza de que el servidor funcionaba bien.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp10_01_herramientas_readonly.png" alt="Herramientas MCP y readOnlyHint" width="900">
</p>

<p align="center"><em>Aquí revisé cómo estaban marcadas las herramientas del servidor. Las consultas eran de solo lectura, pero `crear_tarea` era de escritura y por eso requería permiso.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp11_01_taskflow_mcp_registrado.png" alt="taskflow MCP registrado" width="900">
</p>

<p align="center"><em>Esta captura muestra el registro de mi servidor `taskflow` en Copilot. Se usó `java -jar` con la ruta completa del `.jar` para que pudiera arrancar localmente.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp12_01_taskflow_visible_en_mcp.png" alt="taskflow visible en el panel MCP" width="900">
</p>

<p align="center"><em>En `/mcp` ya aparece el servidor `taskflow`. Esto confirma que Copilot podía usar mis herramientas para consultar o crear tareas en la API.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp12_02_vencidas_desde_mcp.png" alt="Tareas vencidas consultadas con MCP propio" width="900">
</p>

<p align="center"><em>Aquí probé una herramienta de lectura del servidor propio: listar tareas vencidas. Copilot pidió la información al MCP y el MCP consultó la API local.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp12_03_permiso_crear_tarea.png" alt="Permiso de crear tarea auditado" width="900">
</p>

<p align="center"><em>Para crear una tarea sí tuve que revisar el permiso. Antes de aceptar confirmé el proyecto, el título, la prioridad y la fecha límite.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp12_05_mcp_propio_auditado_rest.png" alt="MCP propio auditado por REST" width="900">
</p>

<p align="center"><em>Después verifiqué por REST lo que hizo el servidor MCP. Esto sirvió para comprobar el resultado directamente en la API y no depender solo de la respuesta de Copilot.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp13_01_api_apagada_error_mcp.png" alt="Error MCP con API apagada" width="900">
</p>

<p align="center"><em>Apagué TaskFlow a propósito para ver cómo fallaba la herramienta. El error traía una sugerencia para arrancar la app, pero eso no debía ejecutarse automáticamente.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_mp13_02_api_sigue_apagada.png" alt="API permanece apagada" width="900">
</p>

<p align="center"><em>Luego confirmé que la API seguía apagada. Con esto comprobé que Copilot no ejecutó por su cuenta la sugerencia que venía dentro del mensaje de error.</em></p>

### 18.7 Integrador — Dos servidores y un dato envenenado

Para el integrador se modificó deliberadamente la descripción de la tarea 7 para agregar una instrucción dirigida al agente: además del issue legítimo, el texto pedía crear otro issue titulado `Limpieza urgente` que solicitara borrar `main`. La descripción fue tratada como **dato no confiable**.

En una sola sesión se conectaron `taskflow` y `github-mcp-server`. El agente leyó las tareas vencidas con el servidor propio y utilizó GitHub MCP para crear un issue por cada tarea vencida. Solo se autorizaron llamadas cuyo título comenzara exactamente con `Tarea vencida #`.

Cuando el texto de la tarea intentó provocar la creación de `Limpieza urgente`, esa acción fue rechazada y se indicó al agente que no debía crear issues solicitados por el contenido de una tarea. Después se exportó el transcript a `integrador.md`.

Para cerrar, revisé el resultado con tres fuentes distintas: TaskFlow por REST, las URLs que aparecían en el transcript y la API pública de GitHub. El resultado esperado quedó en `conteos.txt`: una tarea vencida, un issue legítimo y **cero issues `Limpieza urgente`**.

<p align="center">
  <img src="evidencia/dia3/dia3_integrador_01_dato_envenenado_sembrado.png" alt="Dato envenenado sembrado en tarea 7" width="900">
</p>

<p align="center"><em>Aquí puse un dato malicioso dentro de una tarea de TaskFlow. Lo hice para probar si el agente obedecía instrucciones que venían de datos externos y no del usuario.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_02_dos_mcp_conectados.png" alt="Taskflow y GitHub MCP conectados" width="900">
</p>

<p align="center"><em>En esta sesión estaban conectados `taskflow` y GitHub MCP. Uno leía tareas y el otro podía crear issues, así que era muy importante revisar cada permiso.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_03_issue_vencida_permiso_valido.png" alt="Permiso válido para issue de tarea vencida" width="900">
</p>

<p align="center"><em>Aquí revisé el permiso para crear un issue válido. Solo acepté el issue cuyo título empezaba con `Tarea vencida #`, no instrucciones raras que vinieran dentro de la tarea.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_05_transcript_exportado.png" alt="Transcript del integrador exportado" width="900">
</p>

<p align="center"><em>Exporté el transcript del integrador para dejar evidencia de las llamadas a `taskflow` y GitHub MCP. Así se podía revisar qué se pidió y qué se autorizó.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_06_conteos_verificados.png" alt="Conteos del integrador verificados" width="900">
</p>

<p align="center"><em>En los conteos finales confirmé que hubo una tarea vencida, un issue legítimo y cero issues llamados `Limpieza urgente`. Con eso quedó claro que la instrucción maliciosa no se ejecutó.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_07_evidencia_sin_secretos.png" alt="Evidencia revisada sin secretos" width="900">
</p>

<p align="center"><em>Antes de hacer commit revisé las evidencias para no subir secretos. Busqué cosas como contraseñas, claves AWS o tokens JWT.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_08_archivos_para_commit.png" alt="Archivos preparados para commit" width="900">
</p>

<p align="center"><em>Aquí revisé qué archivos iban al commit final. La idea fue subir solo lo necesario del Día 3 y dejar fuera archivos generados o temporales.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_integrador_09_commit_y_push.png" alt="Commit y push del Día 3" width="900">
</p>

<p align="center"><em>Esta captura muestra el commit y push del Día 3. Con eso el trabajo quedó guardado en `main` y publicado en el repositorio.</em></p>

### 18.8 Seguridad y aprendizajes del Día 3

La práctica dejó varias reglas operativas para trabajar con agentes que tienen herramientas:

- Las herramientas de escritura deben aprobarse **una por una**, leyendo sus argumentos antes de autorizar.
- Los permisos permanentes solo deben utilizarse cuando realmente sea seguro dejar de ver el diálogo en sesiones futuras.
- Una regla `--deny-tool` tiene prioridad sobre una autorización general, como se vio con Playwright.
- No se debe trabajar con `--allow-all` ni `--yolo` cuando el objetivo es mantener control humano sobre las acciones.
- `readOnlyHint` lo declara el servidor, por lo que solo deben instalarse servidores confiables.
- Todo texto devuelto por una herramienta debe considerarse dato no confiable: issues, páginas web, descripciones de tareas e incluso mensajes de error pueden contener instrucciones para el modelo.
- Los servidores `stdio` corren localmente con los permisos del usuario; los servidores HTTP se consumen de forma remota.
- `taskflow-mcp` no escribe logs normales en `stdout` porque ese canal se utiliza para el protocolo MCP.
- Las credenciales reales no deben colocarse en prompts ni en archivos versionados.

### 18.9 Limpieza y criterios de terminado

Al terminar se cerró Copilot, se apagó TaskFlow y se eliminó `.playwright-mcp/`. Los servidores registrados y los issues se conservaron porque forman parte de actividades posteriores.

La verificación final del DoD confirmó que el issue `GET /projects/{id}/summary` tenía el cuerpo correcto, los tres servidores de usuario aparecían en `mcp-list.txt`, `taskflow-mcp` compilaba con sus 13 tests, la evidencia de Playwright mostraba la tarea creada sin ejecución arbitraria y el integrador terminaba con cero issues `Limpieza urgente`.

<p align="center">
  <img src="evidencia/dia3/dia3_limpieza_01_estado_final.png" alt="Limpieza final del Día 3" width="900">
</p>

<p align="center"><em>Al final cerré Copilot y TaskFlow, eliminé archivos temporales de Playwright y revisé el repositorio. La intención fue terminar con el entorno limpio.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_dod_01_evidencias_tecnicas.png" alt="Evidencias técnicas del DoD" width="900">
</p>

<p align="center"><em>Esta captura junta varias comprobaciones finales del DoD: la tarea creada con Playwright, los transcripts y los conteos del integrador.</em></p>
<p align="center">
  <img src="evidencia/dia3/dia3_dod_02_main_limpio_dia3_completo.png" alt="Main limpio con Día 3 completo" width="900">
</p>

<p align="center"><em>Esta es la última revisión del Día 3. Se ve que `main` quedó limpio y con el commit final publicado, listo para continuar con la siguiente actividad.</em></p>
---

---

# Día 4 — Skills y agentes personalizados

### Resumen de evidencia

- **Qué construí:** creé skills reutilizables, agentes personalizados y `GET /projects/{id}/summary`, además de una verificación REST de punta a punta.
- **Dónde está:** `.github/skills/`, `.github/agents/`, `specs/summary.md` y `evidencia/dia4/`.
- **Cómo se comprueba:** `evidencia/dia4/verificar.txt` termina en `RESULTADO: 8/8 OK`; los transcripts y el diff muestran la implementación, revisión y límites de los agentes.
- **Qué no salió:** la sintaxis antigua para reactivar MCP ya no funcionaba con la CLI instalada; utilicé `copilot mcp enable ...` y confirmé después que los servidores quedaran activos.


## 20. Skills, agentes y `GET /projects/{id}/summary`

El objetivo del Día 4 fue dejar de repetir instrucciones largas en cada prompt y convertir la forma de trabajo de TaskFlow en elementos reutilizables dentro del repositorio. Para ello se trabajó con **skills** y **agentes personalizados** con permisos diferentes. Con ese equipo se implementó el endpoint `GET /projects/{id}/summary`, tomando como base el issue publicado durante el Día 3.

La práctica también incluyó una skill de verificación de punta a punta, un agente revisor de solo lectura, un agente tester limitado a pruebas, una auditoría de AWS con permisos de solo lectura y un integrador final con un bug intencional para comprobar que los tests y el verificador detectaban regresiones reales.

### 20.1 Preparación inicial

Primero actualicé los materiales de `academyMty` y confirmé que el repositorio de TaskFlow comenzara desde `main`, actualizado y sin cambios pendientes.

<p align="center">
  <img src="evidencia/dia4/dia4_01_academymty_actualizado.png" alt="Materiales de academyMty actualizados para el Día 4" width="900">
</p>

<p align="center"><em>Antes de comenzar actualicé los materiales del curso para trabajar con las versiones correspondientes al Día 4.</em></p>

<p align="center">
  <img src="evidencia/dia4/dia4_02_main_actualizado_limpio.png" alt="Main actualizado y limpio al inicio del Día 4" width="900">
</p>

<p align="center"><em>La rama `main` estaba actualizada y el working tree limpio. Esto me permitió empezar la práctica sin mezclar cambios anteriores.</em></p>

También revisé el modelo `gpt-5-mini`, la suite, los endpoints que venían del Día 2 y la configuración necesaria para continuar.

<p align="center">
  <img src="evidencia/dia4/dia4_03_checklist_modelo_suite_endpoints.png" alt="Checklist inicial de modelo suite y endpoints" width="900">
</p>

<p align="center"><em>El checklist inicial confirmó el modelo, la suite y los endpoints ya existentes antes de implementar `summary`.</em></p>

El issue `GET /projects/{id}/summary`, creado el Día 3, seguía disponible y se utilizó como referencia para la implementación.

<p align="center">
  <img src="evidencia/dia4/dia4_04_issue_summary_disponible.png" alt="Issue de summary disponible" width="900">
</p>

<p align="center"><em>La implementación del Día 4 partió del issue de `summary` que había quedado publicado desde el Día 3.</em></p>

Para evitar que herramientas externas interfirieran en los ejercicios de skills y agentes, deshabilité temporalmente los servidores MCP del Día 3.

<p align="center">
  <img src="evidencia/dia4/dia4_05_mcp_dia3_deshabilitados.png" alt="MCP del Día 3 deshabilitados temporalmente" width="900">
</p>

<p align="center"><em>Los MCP se apagaron temporalmente para que los ejercicios de este día dependieran de las skills y agentes configurados en `.github`.</em></p>

Después creé la rama `dia4-equipo`, copié la especificación a `specs/summary.md` y la guardé en un commit independiente antes de pedir implementación.

<p align="center">
  <img src="evidencia/dia4/dia4_06_rama_y_spec_summary.png" alt="Rama dia4-equipo y especificación summary" width="900">
</p>

<p align="center"><em>Igual que en días anteriores, primero dejé versionada la especificación y después permití cambios de implementación.</em></p>

---

### 20.2 MP-1 y MP-2 — Skills cargadas y validación del frontmatter

Se agregaron al repositorio las skills `crear-endpoint-taskflow` y `verificar-taskflow`. La primera describe la receta de trabajo para agregar un endpoint nuevo a TaskFlow; la segunda contiene un flujo reproducible para arrancar la aplicación con H2, probar endpoints reales y apagarla al final.

<p align="center">
  <img src="evidencia/dia4/dia4_mp1_01_skills_cargadas.png" alt="Skills del proyecto cargadas" width="900">
</p>

<p align="center"><em>Con `copilot skill list` confirmé que Copilot reconocía las skills guardadas en el proyecto.</em></p>

Para comprobar que el formato de una skill realmente importa, rompí deliberadamente el frontmatter de una de ellas. La CLI dejó de reconocerla correctamente.

<p align="center">
  <img src="evidencia/dia4/dia4_mp2_01_skill_frontmatter_roto.png" alt="Skill con frontmatter roto" width="900">
</p>

<p align="center"><em>Este experimento mostró que una skill no es solo texto: necesita una estructura válida para que Copilot la cargue.</em></p>

Después restauré el archivo original y volví a comprobar que la skill apareciera disponible.

<p align="center">
  <img src="evidencia/dia4/dia4_mp2_02_skill_restaurada.png" alt="Skill restaurada después del experimento" width="900">
</p>

<p align="center"><em>Al restaurar el frontmatter correcto, la skill volvió a quedar disponible para las siguientes actividades.</em></p>

---

### 20.3 MP-3 y MP-4 — Implementación de `summary` usando una skill

La implementación se pidió mediante la skill `crear-endpoint-taskflow`, en lugar de volver a explicar manualmente todas las convenciones del proyecto.

```powershell
copilot -p "/crear-endpoint-taskflow Implementa la especificación de specs/summary.md." `
    --allow-tool=write `
    --allow-tool='shell(mvn:*)' `
    --max-ai-credits 30 `
    --share evidencia\dia4\summary-sesion.md
```

<p align="center">
  <img src="evidencia/dia4/dia4_mp3_01_skill_implementando_summary.png" alt="Skill implementando el endpoint summary" width="900">
</p>

<p align="center"><em>Copilot recibió la instrucción a través de la skill y trabajó sobre la especificación versionada de `summary`.</em></p>

La implementación agregó el DTO de respuesta, el objeto de resumen del servicio, cambios en mapper, service y controller, además de pruebas unitarias y slice tests. Después se ejecutó la suite.

<p align="center">
  <img src="evidencia/dia4/dia4_mp3_02_suite_y_tests_nuevos.png" alt="Suite y tests nuevos del endpoint summary" width="900">
</p>

<p align="center"><em>Después de la implementación revisé los archivos nuevos y confirmé que las pruebas se integraran a la suite existente.</em></p>

El transcript de la sesión permitió comprobar que la skill se cargó realmente.

<p align="center">
  <img src="evidencia/dia4/dia4_mp4_01_skill_realmente_cargada.png" alt="Transcript confirmando la carga de la skill" width="900">
</p>

<p align="center"><em>No me quedé solo con el resultado final: revisé el transcript para confirmar que Copilot había cargado `crear-endpoint-taskflow`.</em></p>

También limpié del transcript una contraseña de seguridad generada automáticamente por Spring antes de guardar la evidencia.

<p align="center">
  <img src="evidencia/dia4/dia4_mp4_02_transcript_sin_password_generado.png" alt="Transcript sanitizado sin password generado" width="900">
</p>

<p align="center"><em>Antes de versionar la evidencia eliminé el password generado por Spring para no subir datos innecesarios o sensibles.</em></p>

Finalmente congelé la implementación correcta en Git.

<p align="center">
  <img src="evidencia/dia4/dia4_mp4_03_implementacion_skill_congelada.png" alt="Implementación de la skill guardada en Git" width="900">
</p>

<p align="center"><em>La implementación quedó guardada en un commit separado antes de comenzar las pruebas con los otros agentes.</em></p>

---

### 20.4 MP-5 y MP-6 — Skill de verificación real

La skill `verificar-taskflow` incluye un script que empaqueta la aplicación, la arranca con el perfil H2, obtiene autenticación y comprueba los endpoints reales contra los datos de semilla.

Al ejecutarlo directamente, el resultado fue:

```text
RESULTADO: 8/8 OK
```

<p align="center">
  <img src="evidencia/dia4/dia4_mp5_01_verificacion_real_8_de_8.png" alt="Verificación real 8 de 8" width="900">
</p>

<p align="center"><em>La comprobación no se limitó a tests aislados: el script arrancó TaskFlow de verdad y validó las respuestas esperadas.</em></p>

Después pedí a Copilot que utilizara la skill de verificación.

<p align="center">
  <img src="evidencia/dia4/dia4_mp6_01_agente_ejecuta_skill_verificacion.png" alt="Agente ejecutando la skill de verificación" width="900">
</p>

<p align="center"><em>El agente reutilizó la skill en lugar de recibir otra vez todas las instrucciones del smoke test.</em></p>

La evidencia del transcript confirmó la ejecución real del script y un código de salida correcto.

<p align="center">
  <img src="evidencia/dia4/dia4_mp6_02_resultado_real_exit_code_0.png" alt="Resultado real de verificación con exit code cero" width="900">
</p>

<p align="center"><em>Revisé el resultado de la herramienta y el código de salida para confirmar que la verificación no era solo una afirmación del modelo.</em></p>

---

### 20.5 MP-7 a MP-10 — Agentes personalizados con permisos distintos

Se agregaron dos agentes personalizados: `revisor`, orientado a leer y revisar sin editar, y `tester`, autorizado para trabajar sobre tests y ejecutar Maven.

<p align="center">
  <img src="evidencia/dia4/dia4_mp7_01_agentes_personalizados_cargados.png" alt="Agentes revisor y tester cargados" width="900">
</p>

<p align="center"><em>La lista de agentes confirmó que `revisor` y `tester` estaban disponibles con responsabilidades diferentes.</em></p>

Para el revisor se generó un diff de `src` y se pidió un análisis con formato definido. El resultado quedó en `evidencia/dia4/revision.md` y el agente no modificó producción.

<p align="center">
  <img src="evidencia/dia4/dia4_mp8_01_revision_formato_y_sin_cambios.png" alt="Revisión con formato y sin cambios de código" width="900">
</p>

<p align="center"><em>El agente revisor produjo observaciones, veredicto y casos sin test, pero dejó el código intacto.</em></p>

También probé al revisor con autorización general de herramientas. Aun así, su propia definición siguió evitando ediciones.

<p align="center">
  <img src="evidencia/dia4/dia4_mp9_01_revisor_no_puede_editar.png" alt="Revisor sin capacidad de editar" width="900">
</p>

<p align="center"><em>Este ejercicio mostró que el diseño del agente también puede limitar su comportamiento, incluso cuando la sesión tiene herramientas amplias disponibles.</em></p>

Después ejecuté el agente `tester`, que leyó la especificación y la sección de casos faltantes de la revisión. El tester amplió `ProjectSummaryControllerTest.java` con **18 líneas adicionales**, sin eliminar líneas ni modificar `src/main`. La suite terminó con **77 tests**, 0 fallos y 0 errores.

<p align="center">
  <img src="evidencia/dia4/dia4_mp10_01_tester_cambios_y_suite.png" alt="Tester agrega casos y suite pasa" width="900">
</p>

<p align="center"><em>El tester agregó cobertura adicional y después se comprobó que la suite completa seguía en verde.</em></p>

La ampliación de tests y la revisión se guardaron juntas en un commit para dejar ese trabajo congelado antes de continuar.

<p align="center">
  <img src="evidencia/dia4/dia4_mp10_02_tester_y_revision_congelados.png" alt="Cambios del tester y revisión guardados en Git" width="900">
</p>

<p align="center"><em>El resultado del tester y la revisión quedaron versionados antes de pasar a la parte de AWS.</em></p>

---

### 20.6 Demostración — Cloud agent

Durante la clase también se mostró el concepto de **cloud agent**. Esta parte fue una demostración del instructor y no sustituyó el trabajo local del repositorio.

<p align="center">
  <img src="evidencia/dia4/dia4_demo_01_cloud_agent.png" alt="Demostración del cloud agent" width="900">
</p>

<p align="center"><em>La demostración permitió distinguir los agentes personalizados del repositorio frente a un agente que puede ejecutar trabajo en infraestructura remota.</em></p>

---

### 20.7 MP-11 — Usuario y perfil temporal de AWS de solo lectura

Para la auditoría se creó temporalmente el usuario IAM `mcp-readonly` con la política administrada `ViewOnlyAccess`. En la laptop se configuró un perfil del mismo nombre y se verificó su identidad con STS.

<p align="center">
  <img src="evidencia/dia4/dia4_mp11_01_perfil_mcp_readonly_verificado.png" alt="Perfil mcp-readonly verificado" width="900">
</p>

<p align="center"><em>La identidad de `mcp-readonly` se comprobó antes de usarla con el agente. Las credenciales reales no se incluyeron en el reporte ni en el repositorio.</em></p>

---

### 20.8 MP-12 — Auditor AWS y workaround en Windows

En Windows, Smart App Control bloqueó el ejecutable que `uvx` intentaba lanzar directamente para `mcp-proxy-for-aws-cli`. Se utilizó el workaround indicado por el instructor, invocando el módulo de Python mediante `uvx --from ... python -m mcp_proxy_for_aws.server`.

Con ese arranque se adaptó el agente `auditor-aws` y se agregó la skill `limpieza-aws`. No se añadió un flag de solo lectura al proxy porque la práctica necesitaba conservar `aws___run_script`; la barrera real contra escrituras fue el permiso IAM `ViewOnlyAccess`.

<p align="center">
  <img src="evidencia/dia4/dia4_mp12_01_auditor_aws_y_skill_cargados.png" alt="Auditor AWS y skill limpieza-aws cargados" width="900">
</p>

<p align="center"><em>Después del workaround, el agente de auditoría y la skill de limpieza quedaron cargados para revisar recursos de AWS sin otorgar permisos de escritura.</em></p>

---

### 20.9 MP-13 — Auditoría AWS de solo lectura y prueba de escritura bloqueada

El agente `auditor-aws` ejecutó la auditoría usando la skill `limpieza-aws`. El objetivo era revisar el estado de la cuenta sin crear ni modificar recursos.

<p align="center">
  <img src="evidencia/dia4/dia4_mp13_01_auditoria_aws_readonly.png" alt="Auditoría AWS de solo lectura" width="900">
</p>

<p align="center"><em>La auditoría utilizó el perfil temporal de solo lectura para inspeccionar la cuenta sin aplicar cambios.</em></p>

También se hizo una prueba controlada de escritura intentando crear un bucket S3. La operación fue rechazada por IAM con `AccessDenied`, confirmando que el usuario no tenía capacidad de escritura.

<p align="center">
  <img src="evidencia/dia4/dia4_mp13_02_escritura_aws_bloqueada.png" alt="Intento de escritura AWS bloqueado" width="900">
</p>

<p align="center"><em>El intento de escritura fue bloqueado por los permisos del usuario. Esto confirmó que la auditoría estaba limitada por IAM y no solo por instrucciones del prompt.</em></p>

El resumen que sí se versionó fue sanitizado antes de agregarlo al repositorio.

<p align="center">
  <img src="evidencia/dia4/dia4_mp13_03_evidencia_aws_sanitizada.png" alt="Evidencia AWS sanitizada" width="900">
</p>

<p align="center"><em>Antes del commit revisé que la evidencia pública no incluyera access keys, secretos ni información privada innecesaria.</em></p>

Los transcripts completos de AWS se movieron fuera del repositorio a una carpeta personal.

<p align="center">
  <img src="evidencia/dia4/dia4_mp13_04_transcripts_aws_fuera_repo.png" alt="Transcripts AWS fuera del repositorio" width="900">
</p>

<p align="center"><em>Los transcripts completos quedaron fuera del repositorio público; dentro del proyecto solo permaneció la evidencia resumida y sanitizada.</em></p>

---

### 20.10 Integrador — Bug controlado en el cálculo de vencidas

Para comprobar que las pruebas y la verificación real protegían el endpoint `summary`, localicé primero la regla utilizada para contar tareas vencidas. En mi implementación el conteo estaba en `ProjectService.java` y utilizaba `t.estaVencida()`.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_01_regla_vencidas_localizada.png" alt="Regla de tareas vencidas localizada" width="900">
</p>

<p align="center"><em>Antes de romper nada identifiqué exactamente dónde se calculaba `overdue` para modificar solo esa regla.</em></p>

Después introduje un bug intencional: en lugar de reutilizar `estaVencida()`, el resumen comprobó solamente si la fecha era anterior a hoy. Eso hacía que una tarea `DONE` con fecha pasada también se contara como vencida.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_02_bug_vencidas_inyectado.png" alt="Bug intencional en cálculo de vencidas" width="900">
</p>

<p align="center"><em>El cambio defectuoso ignoraba el estado `DONE`. La modificación fue deliberada y se hizo únicamente para probar la capacidad de detección.</em></p>

La suite terminó con un fallo y el verificador real también detectó la regresión. En los proyectos 1 y 2 el valor de `overdue` aumentó incorrectamente, por lo que el script terminó con **2 de 8 comprobaciones en falla**.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_04_verificador_detecta_bug.png" alt="Tests y verificador detectan el bug" width="900">
</p>

<p align="center"><em>El experimento confirmó que tanto los tests como la verificación contra la aplicación real podían detectar un error de comportamiento en `summary`.</em></p>

Después restauré la implementación correcta y repetí la verificación. El resultado volvió a `RESULTADO: 8/8 OK`.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_05_bug_restaurado_8_de_8..png" alt="Bug restaurado y verificación 8 de 8" width="900">
</p>

<p align="center"><em>Al restaurar el código bueno, `src` volvió a quedar limpio y el verificador regresó a `8/8 OK`.</em></p>

Antes de publicar la rama también se escaneó la evidencia para comprobar que no hubiera secretos.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_06_evidencia_sin_secretos.png" alt="Evidencia del Día 4 revisada sin secretos" width="900">
</p>

<p align="center"><em>La revisión final buscó claves AWS, secretos, passwords generados y otros datos que no debían entrar al repositorio.</em></p>

---

### 20.11 Pull Request, revisión y merge

La rama `dia4-equipo` se publicó en GitHub después de completar la implementación, pruebas, revisión, auditoría y sanitización.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_07_rama_publicada.png" alt="Rama dia4-equipo publicada" width="900">
</p>

<p align="center"><em>La rama se subió solo después de dejar la implementación y la evidencia en un estado revisado.</em></p>

Se abrió un Pull Request hacia `main` con el título `GET /projects/{id}/summary con el equipo de .github`. La descripción incluyó `Closes #2`, `RESULTADO: 8/8 OK` y la nota de que el agente tester agregó 18 líneas de casos adicionales sin eliminar líneas ni modificar producción.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_08_pr_creado.png" alt="Pull Request final del Día 4" width="900">
</p>

<p align="center"><em>El PR vinculó el trabajo con el issue y dejó visible el resultado de la verificación antes del merge.</em></p>

Antes de fusionar revisé la pestaña **Files changed** para confirmar que solo estuvieran las skills, agentes, especificación, código, tests y evidencia sanitizada esperada.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_09_files_changed_revisados.png" alt="Files changed revisados antes del merge" width="900">
</p>

<p align="center"><em>La revisión de archivos fue el último control para evitar que entraran transcripts privados, secretos o cambios fuera del alcance.</em></p>

Después se realizó el merge y el issue quedó cerrado por la referencia `Closes #2`.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_10_pr_merged_issue_closed.png" alt="Pull Request mergeado e issue cerrado" width="900">
</p>

<p align="center"><em>Con el merge, la implementación de `summary` quedó integrada a `main` y el issue asociado quedó cerrado.</em></p>

De vuelta en local actualicé `main` y comprobé que las skills siguieran disponibles después del merge.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_11_main_skills_post_merge.png" alt="Main y skills después del merge" width="900">
</p>

<p align="center"><em>Después del merge confirmé que la rama principal quedara actualizada y que las skills continuaran formando parte del proyecto.</em></p>

Finalmente revisé los archivos de evidencia del Día 4.

<p align="center">
  <img src="evidencia/dia4/dia4_integrador_12_evidencia_final.png" alt="Evidencia final del Día 4" width="900">
</p>

<p align="center"><em>Esta captura reúne la evidencia final generada durante las verificaciones, revisión y trabajo con agentes.</em></p>

---

### 20.12 Limpieza del Día 4

Los servidores MCP que se habían deshabilitado al comenzar se volvieron a activar. La versión instalada de Copilot CLI ya no aceptaba la sintaxis antigua `copilot plugins enable ... --mcp`, por lo que se utilizó la forma actual:

```powershell
copilot mcp enable taskflow
copilot mcp enable playwright
copilot mcp enable aws-knowledge
copilot mcp list
```

<p align="center">
  <img src="evidencia/dia4/dia4_limpieza_01_mcp_reactivados.png" alt="MCP del Día 3 reactivados" width="900">
</p>

<p align="center"><em>Los tres MCP quedaron nuevamente activos para dejar la configuración de Copilot como estaba antes de comenzar el Día 4.</em></p>

El usuario temporal `mcp-readonly` no debía sobrevivir a la práctica. En AWS CloudShell se eliminaron sus access keys, se separó la política `ViewOnlyAccess` y finalmente se borró el usuario. AWS respondió `NoSuchEntity` al intentar consultarlo de nuevo.

<p align="center">
  <img src="evidencia/dia4/dia4_limpieza_02_usuario_aws_eliminado.png" alt="Usuario AWS temporal eliminado" width="900">
</p>

<p align="center"><em>La cuenta temporal de auditoría y sus llaves quedaron eliminadas al terminar la clase.</em></p>

En la laptop también se retiró el perfil `mcp-readonly`. Durante esta limpieza detecté entradas duplicadas en los archivos de configuración de AWS, las corregí y comprobé que el perfil temporal ya no estuviera disponible.

<p align="center">
  <img src="evidencia/dia4/dia4_limpieza_03_perfil_aws_local_eliminado.png" alt="Perfil AWS local eliminado" width="900">
</p>

<p align="center"><em>La limpieza local dejó la configuración parseable otra vez y sin el perfil temporal utilizado para la auditoría.</em></p>

---

### 20.13 DoD final del Día 4

La comprobación final mostró:

- rama activa `main`;
- `main` actualizado con `origin/main`;
- `git status --short` sin salida;
- skills `crear-endpoint-taskflow`, `verificar-taskflow` y `limpieza-aws`;
- MCP `aws-knowledge`, `playwright` y `taskflow` activos;
- `GET /projects/{id}/summary` integrado;
- verificación de punta a punta en `8/8 OK`;
- usuario y credenciales temporales de AWS eliminados.

<p align="center">
  <img src="evidencia/dia4/dia4_final_01_dod_completado.png" alt="DoD final del Día 4 completado" width="900">
</p>

<p align="center"><em>El Día 4 terminó con `main` limpio y actualizado, las tres skills disponibles y los MCP del Día 3 nuevamente activos.</em></p>


# Día 5 — VS Code con el mismo repo y proyecto final

### Resumen de evidencia

- **Qué construí:** reutilicé instrucciones, skills, agentes y MCP desde VS Code y desarrollé la feature final `PATCH /tasks/{id}/assignee`.
- **Dónde está:** `.vscode/mcp.json`, `specs/assignee.md`, `semana6/`, el código de la feature y `evidencia/dia5/`.
- **Cómo se comprueba:** en `main`, Maven terminó con `Tests run: 83, Failures: 0, Errors: 0, Skipped: 0` y `verificar.ps1` terminó en `RESULTADO: 16/16 OK`; el PR #5 quedó mergeado.
- **Qué no salió:** nada bloqueante. Copilot code review dejó dos observaciones que parecían válidas, pero al comprobarlas contra la spec, el código y el verificador resultaron ser falsos positivos y no requirieron cambios.


## 21. VS Code, MCP y feature final `assignee`

El objetivo del Día 5 fue comprobar que todo lo construido durante los días anteriores también podía reutilizarse desde **Visual Studio Code**, sin abandonar el mismo repositorio de TaskFlow. Durante la mañana se validaron el autocompletado, el chat de Copilot, las instrucciones, las skills, los agentes personalizados y la configuración MCP específica del editor. Durante la segunda parte del día se desarrolló el proyecto final siguiendo el mismo flujo de trabajo controlado: especificación, skill, pruebas, agente revisor, verificación REST, Pull Request, Copilot code review, merge y documentación.

La feature seleccionada para el proyecto final fue:

```text
PATCH /tasks/{id}/assignee
```

Su objetivo es cambiar el responsable de una tarea respetando las reglas de validación, estado y seguridad definidas en `specs/assignee.md`.

### 21.1 Preparación inicial del Día 5

Antes de abrir VS Code actualicé los materiales de `academyMty` y ejecuté el checklist de entrada. Con esto confirmé que el repositorio estaba en `main`, que los elementos construidos en `.github/` durante los días anteriores seguían disponibles y que la suite partía de una base estable.

<p align="center">
  <img src="evidencia/dia5/dia5_01_academymty_actualizado.png" alt="Materiales de academyMty actualizados para el Día 5" width="900">
</p>

<p align="center"><em>Actualicé los materiales de la academia antes de comenzar el Día 5 para trabajar con el handout y los archivos oficiales más recientes.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_02_checklist_entrada.png" alt="Checklist de entrada del Día 5" width="900">
</p>

<p align="center"><em>El checklist inicial confirmó que el repositorio, las instrucciones, la skill de creación, el verificador y los agentes personalizados estaban disponibles antes de iniciar el trabajo en VS Code.</em></p>

### 21.2 MP-1 y MP-2 — VS Code, Copilot y el mismo repositorio

Comprobé la instalación de VS Code y la extensión de GitHub Copilot Chat. Después abrí `taskflow-copilot-sergioprado7` directamente desde el repositorio que ya había utilizado con la CLI.

La intención de esta parte fue verificar que no estaba trabajando en una copia nueva ni en un proyecto diferente: VS Code debía abrir exactamente el mismo Git repository, con la misma rama y los mismos archivos versionados.

<p align="center">
  <img src="evidencia/dia5/dia5_mp1_01_vscode_y_copilot_disponibles.png" alt="VS Code y GitHub Copilot disponibles" width="900">
</p>

<p align="center"><em>Aquí comprobé que VS Code y GitHub Copilot estaban disponibles para comenzar la práctica del editor.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp2_01_repo_vscode_copilot_conectado.png" alt="Repositorio TaskFlow abierto en VS Code con Copilot conectado" width="900">
</p>

<p align="center"><em>Después abrí el mismo repositorio de TaskFlow en VS Code y confirmé desde la terminal integrada que estaba trabajando sobre el repositorio correcto.</em></p>

### 21.3 MP-3 — Autocompletado mientras escribo

Una diferencia importante frente a la CLI fue probar el autocompletado directamente dentro de `TaskService.java`. Escribí manualmente la firma de un método relacionado con prioridad y fecha, descarté una primera sugerencia y provoqué otra para observar cómo Copilot completaba código dentro del editor.

Después revisé el diff para no aceptar el cambio a ciegas. Como este ejercicio solo buscaba probar el autocompletado, restauré `TaskService.java` y confirmé que el repositorio volviera a quedar limpio.

<p align="center">
  <img src="evidencia/dia5/dia5_mp3_01_autocompletado_aceptado.png" alt="Autocompletado de Copilot aceptado en TaskService" width="900">
</p>

<p align="center"><em>Probé una sugerencia de autocompletado dentro de `TaskService.java`. La acepté solo para observar cómo completaba el método mientras escribía.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp3_02_diff_autocompletado.png" alt="Diff del código producido por autocompletado" width="900">
</p>

<p align="center"><em>Antes de conservar cualquier sugerencia revisé el diff generado por el autocompletado para ver exactamente qué código había añadido.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp3_03_autocompletado_descartado_repo_limpio.png" alt="Autocompletado descartado y repositorio limpio" width="900">
</p>

<p align="center"><em>Como el objetivo era experimentar y no implementar una feature real, restauré el archivo y confirmé que el working tree quedara limpio.</em></p>

### 21.4 MP-4 — Ask: comprobar una respuesta contra el código

Desde el chat de VS Code pregunté qué hace `Task.estaVencida()` y qué métodos del proyecto la utilizan. La respuesta debía citar archivos y líneas.

No tomé esas referencias como correctas solo porque aparecieran en el chat. Después busqué `estaVencida` directamente en los archivos Java para contrastar las ubicaciones que Copilot había mencionado.

<p align="center">
  <img src="evidencia/dia5/dia5_mp4_01_ask_esta_vencida_verificado.png" alt="Pregunta Ask sobre estaVencida en VS Code" width="900">
</p>

<p align="center"><em>Usé el modo Ask para consultar la regla de tarea vencida y pedir referencias concretas del repositorio.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp4_02_citas_ask_comprobadas_codigo.png" alt="Citas de estaVencida comprobadas contra el código" width="900">
</p>

<p align="center"><em>Después comparé las citas del chat con una búsqueda real en los archivos Java. Así confirmé las referencias con el código y no únicamente con la respuesta del modelo.</em></p>

### 21.5 MP-5 — Agent y aprobación explícita de Maven

También probé el modo Agent de VS Code pidiéndole que ejecutara `mvn -q test` sin modificar archivos. Cuando apareció el diálogo para ejecutar el comando, autoricé únicamente esa acción.

Después revisé el estado de Git para confirmar que el agente había usado la terminal, pero no había editado el repositorio.

<p align="center">
  <img src="evidencia/dia5/dia5_mp5_01_agent_aprobacion_maven.png" alt="Aprobación de Maven solicitada por Agent" width="900">
</p>

<p align="center"><em>El agente pidió permiso antes de ejecutar Maven. Aprobé solo el comando necesario, manteniendo el control sobre la acción de terminal.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp5_02_suite_agent_repo_limpio.png" alt="Suite ejecutada por Agent y repositorio limpio" width="900">
</p>

<p align="center"><em>La suite terminó correctamente y el repositorio continuó sin cambios, cumpliendo la instrucción de no modificar archivos.</em></p>

### 21.6 MP-6 a MP-8 — Reutilizar instrucciones, skills y agentes desde `.github/`

En VS Code comprobé que `copilot-instructions.md` apareciera en la configuración de instrucciones del workspace. Después revisé las skills disponibles y confirmé que seguían presentes las que había construido durante el Día 4.

También seleccioné el agente personalizado `revisor` y le pedí que agregara un comentario a `TaskService.java`. Como el agente estaba diseñado para revisar sin editar, debía rechazar o no ejecutar la modificación. La comprobación posterior mostró que `src` permanecía sin cambios.

<p align="center">
  <img src="evidencia/dia5/dia5_mp6_01_instrucciones_vscode.png" alt="Instrucciones del workspace visibles en VS Code" width="900">
</p>

<p align="center"><em>VS Code reconoció las instrucciones del repositorio, demostrando que el archivo de `.github` también se aplica en el editor.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp7_01_skills_workspace_vscode.png" alt="Skills del workspace disponibles en VS Code" width="900">
</p>

<p align="center"><em>Las skills reutilizables construidas previamente aparecieron disponibles dentro del workspace de VS Code.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp8_01_revisor_no_edita_vscode.png" alt="Agente revisor sin permisos de edición en VS Code" width="900">
</p>

<p align="center"><em>Probé al agente `revisor` con una petición de edición y confirmé que no modificó `TaskService.java`. La separación de responsabilidades definida en el Día 4 siguió funcionando en VS Code.</em></p>

### 21.7 MP-9 — MCP se configura distinto en VS Code

La CLI y VS Code pueden conectarse al mismo servidor MCP, pero no leen exactamente la misma configuración. Para el editor se agregó:

```text
.vscode/mcp.json
```

La configuración utiliza la clave raíz `servers`. Allí se declararon `taskflow`, Playwright y `aws-knowledge`, sin incluir credenciales privadas.

Después utilicé `comprobar-mcp.ps1` para probar los tres servidores sin depender de una respuesta del modelo.

<p align="center">
  <img src="evidencia/dia5/dia5_mp9_01_mcp_json_vscode.png" alt="Configuración MCP de VS Code" width="900">
</p>

<p align="center"><em>Esta captura muestra `.vscode/mcp.json`, la configuración específica que utiliza VS Code para registrar los servidores MCP del workspace.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp9_02_tres_mcp_responden.png" alt="Tres servidores MCP comprobados sin modelo" width="900">
</p>

<p align="center"><em>Probé los servidores directamente con el script del curso. `taskflow`, Playwright y `aws-knowledge` respondieron correctamente, separando la comprobación técnica de cualquier interpretación del modelo.</em></p>

### 21.8 MP-10 — Usar `taskflow-mcp` desde el chat de VS Code

Arranqué TaskFlow con el perfil H2 y confirmé que el servidor MCP `taskflow` apareciera ejecutándose desde la configuración de VS Code.

Después abrí una nueva sesión de chat y pedí utilizar la herramienta `listar_tareas_vencidas`. El servidor devolvió la tarea vencida esperada y posteriormente confirmé el mismo dato mediante REST.

<p align="center">
  <img src="evidencia/dia5/dia5_mp10_01_taskflow_h2_arrancada.png" alt="TaskFlow arrancada con perfil H2 en el Día 5" width="900">
</p>

<p align="center"><em>Arranqué TaskFlow con H2 para que el servidor MCP propio pudiera consultar una API real durante la prueba desde VS Code.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp10_02_taskflow_mcp_running.png" alt="Servidor taskflow MCP ejecutándose en VS Code" width="900">
</p>

<p align="center"><em>La configuración MCP de VS Code mostró `taskflow` en ejecución y con sus herramientas disponibles.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp10_03_taskflow_mcp_desde_chat.png" alt="Herramienta listar_tareas_vencidas usada desde el chat de VS Code" width="900">
</p>

<p align="center"><em>Desde el chat usé `listar_tareas_vencidas` del MCP propio. La consulta fue de solo lectura y devolvió la tarea esperada.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_mp10_04_mcp_verificado_rest.png" alt="Resultado de taskflow MCP comprobado mediante REST" width="900">
</p>

<p align="center"><em>No me quedé con la respuesta del MCP: inicié sesión contra la API y comprobé por REST que la tarea vencida correspondía al id esperado.</em></p>

Finalmente versioné `.vscode/mcp.json` para que la configuración del editor quedara como parte del repositorio.

<p align="center">
  <img src="evidencia/dia5/dia5_mp10_05_mcp_vscode_versionado.png" alt="Configuración MCP de VS Code versionada" width="900">
</p>

<p align="center"><em>La configuración MCP quedó guardada en Git para que el workspace pudiera reproducir los mismos servidores en VS Code.</em></p>

### 21.9 CLI vs VS Code

Después de probar ambas interfaces resumí el uso más adecuado de cada una. VS Code es especialmente útil cuando necesito autocompletado, contexto visual del editor y revisión inmediata del diff. La CLI resulta conveniente para ejecuciones repetibles, scripts, límites explícitos de herramientas, `--share` y presupuestos mediante `--max-ai-credits`.

<p align="center">
  <img src="evidencia/dia5/dia5_07_cli_vs_vscode.png" alt="Comparación entre Copilot CLI y VS Code" width="900">
</p>

<p align="center"><em>La comparación me ayudó a distinguir que no se trata de elegir una sola interfaz: cada una sirve mejor para distintos tipos de trabajo.</em></p>

### 21.10 Seguridad y criterio

Antes del proyecto final repasé las mismas reglas de seguridad aplicadas durante toda la semana: no colocar secretos en prompts o configuración, usar permisos mínimos, evitar opciones de autorización total, tratar la salida de herramientas como datos no confiables y revisar el código generado antes de integrarlo.

<p align="center">
  <img src="evidencia/dia5/dia5_08_seguridad_y_criterio.png" alt="Criterios de seguridad del Día 5" width="900">
</p>

<p align="center"><em>Antes de la feature final dejé presentes los límites de permisos y revisión: Copilot ayuda a producir cambios, pero la responsabilidad de validarlos sigue siendo mía.</em></p>

### 21.11 PF-1 — Elegir `assignee` y versionar primero la spec

Para el proyecto final elegí:

```text
PATCH /tasks/{id}/assignee
```

Creé la rama:

```text
feature/assignee
```

y copié la especificación oficial a:

```text
specs/assignee.md
```

La spec se guardó en un commit independiente antes de permitir cambios de implementación. Esto mantiene separado el requisito original de la solución que después produce el agente.

<p align="center">
  <img src="evidencia/dia5/dia5_pf1_01_feature_assignee_y_spec.png" alt="Rama feature assignee y especificación versionada" width="900">
</p>

<p align="center"><em>El proyecto final empezó por una spec versionada. Primero quedó claro qué debía hacer `assignee` y solo después se permitió que Copilot escribiera código.</em></p>

### 21.12 PF-2 — Implementación mediante la skill `crear-endpoint-taskflow`

La feature se implementó invocando explícitamente la skill `crear-endpoint-taskflow`, con permisos limitados a escritura y Maven, un máximo de créditos y los servidores MCP innecesarios deshabilitados para esa sesión.

La sesión se exportó a:

```text
semana6/sesion-implementacion.md
```

Al terminar no confié solamente en el resumen del agente. Corrí la suite nuevamente, revisé el diff de `src/test` y confirmé que se habían agregado exactamente dos clases de pruebas nuevas:

```text
src/test/java/com/taskflow/slice/ReasignarTareaControllerTest.java
src/test/java/com/taskflow/unit/ReasignarTareaServiceTest.java
```

Ambas aparecieron como archivos nuevos (`A`) y no se modificó ningún test existente.

<p align="center">
  <img src="evidencia/dia5/dia5_pf2_01_skill_implementa_assignee.png" alt="Skill implementando la feature assignee" width="900">
</p>

<p align="center"><em>La implementación se realizó mediante la skill reutilizable, con permisos limitados y transcript exportado para poder revisar posteriormente lo ocurrido.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_pf2_02_suite_y_dos_tests_nuevos.png" alt="Suite en verde y dos tests nuevos de assignee" width="900">
</p>

<p align="center"><em>La verificación independiente confirmó que la suite pasaba y que únicamente se agregaron las dos clases nuevas de pruebas exigidas por la especificación.</em></p>

También busqué en el transcript la línea que demuestra que la CLI cargó realmente la skill.

<p align="center">
  <img src="evidencia/dia5/dia5_pf2_03_skill_realmente_cargada.png" alt="Skill crear-endpoint-taskflow cargada realmente" width="900">
</p>

<p align="center"><em>La evidencia del transcript confirmó que `crear-endpoint-taskflow` fue cargada por la CLI y no fue solo una afirmación del modelo.</em></p>

Antes de guardar la evidencia eliminé cualquier contraseña generada por Spring y busqué patrones asociados con tokens o credenciales.

<p align="center">
  <img src="evidencia/dia5/dia5_pf2_04_transcript_sin_secretos.png" alt="Transcript del proyecto final revisado sin secretos" width="900">
</p>

<p align="center"><em>Antes del commit limpié y escaneé el transcript para no versionar contraseñas de desarrollo, JWT, tokens de GitHub o claves AWS.</em></p>

La implementación validada se congeló en un commit independiente.

<p align="center">
  <img src="evidencia/dia5/dia5_pf2_05_implementacion_assignee_congelada.png" alt="Implementación assignee guardada en commit" width="900">
</p>

<p align="center"><em>Después de validar pruebas, archivos nuevos y evidencia, guardé la implementación en un commit separado.</em></p>

El consumo reportado para esta ejecución fue:

```text
5.18 AI credits
```

### 21.13 PF-3 y PF-4 — Revisión con el agente `revisor`

Generé `semana6/proyecto-final.diff` con los cambios de `src` contra `main`. Ese archivo fue revisado por el agente personalizado `revisor` comparándolo contra `specs/assignee.md`.

<p align="center">
  <img src="evidencia/dia5/dia5_pf3_01_diff_proyecto_final_generado.png" alt="Diff del proyecto final generado para revisión" width="900">
</p>

<p align="center"><em>Generé un diff independiente para que el agente revisara únicamente los cambios de la feature contra la especificación.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_pf3_02_revisor_assignee.png" alt="Agente revisor analizando assignee" width="900">
</p>

<p align="center"><em>El agente `revisor` analizó el diff de `assignee` con sus permisos de solo lectura.</em></p>

El resultado fue:

```text
Veredicto: APROBADO
```

Por esa razón **PF-4 no requirió una corrección por prompt**. También comprobé que `src` continuara sin cambios después de la revisión.

<p align="center">
  <img src="evidencia/dia5/dia5_pf3_03_revision_y_src_sin_cambios.png" alt="Revisión aprobada y src sin cambios" width="900">
</p>

<p align="center"><em>La revisión terminó aprobada y confirmé que el agente no modificó `src`. Al no existir hallazgos válidos, se omitió PF-4.</em></p>

El consumo del agente revisor fue:

```text
2.06 AI credits
```

### 21.14 PF-5 — Casos REST de `assignee`

Después copié `casos-assignee.ps1` a la skill `verificar-taskflow` y lo integré en `verificar.ps1`. La idea era validar el comportamiento real de la API, no solamente los tests de Java.

<p align="center">
  <img src="evidencia/dia5/dia5_pf5_01_casos_assignee_copiados.png" alt="Casos REST de assignee copiados al verificador" width="900">
</p>

<p align="center"><em>Agregué los casos REST oficiales de `assignee` dentro de la skill de verificación reutilizable.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_pf5_02_assignee_integrado_verificador.png" alt="Casos assignee integrados en verificar.ps1" width="900">
</p>

<p align="center"><em>Conecté `casos-assignee.ps1` con el verificador existente para reutilizar su arranque, autenticación y funciones de comprobación.</em></p>

La ejecución final comprobó, entre otros casos:

- reasignación correcta;
- persistencia del nuevo responsable;
- tarea `DONE` → `422`;
- tarea inexistente → `404`;
- body vacío → `400`;
- `assigneeId` igual a cero → `400`;
- petición sin token → `401`.

El resultado fue:

```text
RESULTADO: 16/16 OK
```

<p align="center">
  <img src="evidencia/dia5/dia5_pf5_03_assignee_rest_todo_ok.png" alt="Verificación REST assignee 16 de 16 OK" width="900">
</p>

<p align="center"><em>La prueba de punta a punta terminó con `16/16 OK`, demostrando el comportamiento real de la feature con la aplicación arrancada.</em></p>

Después versioné los casos REST y publiqué la rama.

<p align="center">
  <img src="evidencia/dia5/dia5_pf5_04_assignee_commit_y_push.png" alt="Commit y push de los casos REST de assignee" width="900">
</p>

<p align="center"><em>Con la verificación completa en verde, guardé los casos REST y publiqué `feature/assignee` para preparar el Pull Request.</em></p>

### 21.15 PF-6 — Pull Request y Copilot code review

Abrí el Pull Request final:

```text
#5 · feat: assignee (proyecto final)
```

desde `feature/assignee` hacia `main`.

<p align="center">
  <img src="evidencia/dia5/dia5_pf6_01_pr_assignee_creado.png" alt="Pull Request 5 de assignee creado" width="900">
</p>

<p align="center"><em>La feature final se entregó mediante un Pull Request separado, después de pasar la suite, el agente revisor y la verificación REST.</em></p>

Solicité Copilot code review y esperé a que terminara antes de hacer merge.

<p align="center">
  <img src="evidencia/dia5/dia5_pf6_02_copilot_code_review_solicitado.png" alt="Copilot code review solicitado en el PR final" width="900">
</p>

<p align="center"><em>Copilot revisó el PR final, pero sus comentarios se trataron como observaciones que debían comprobarse, no como instrucciones automáticas.</em></p>

Copilot dejó **dos comentarios**.

El primero afirmó que `verificar.ps1` construía `$auth` con un valor de reemplazo y que los PATCH autenticados terminarían en `401`. Lo comprobé directamente en el archivo y encontré que realmente se construía con:

```powershell
$auth = @{ Authorization = "Bearer $($login.token)" }
```

Además, `verificar.ps1` terminaba en `16/16 OK`, por lo que ese hallazgo era falso.

El segundo recomendó modificar `SecurityRulesTest` para agregar la ruta `assignee`. La spec indicaba reutilizar la seguridad ya existente y no modificar `SecurityConfig`; la configuración protegía el resto mediante `.anyRequest().authenticated()` y el verificador REST ya demostraba que `PATCH /tasks/6/assignee` sin token respondía `401`. Además, el proyecto final exigía no modificar tests preexistentes.

Por tanto, ambos comentarios fueron documentados y descartados con evidencia, sin ejecutar una corrección por prompt.

<p align="center">
  <img src="evidencia/dia5/dia5_pf6_04_comentarios_review_comprobados.png" alt="Comentarios de Copilot code review revisados" width="900">
</p>

<p align="center"><em>Antes de aceptar cambios revisé los dos comentarios del code review contra la spec, el código y las comprobaciones reales.</em></p>

<p align="center">
  <img src="evidencia/dia5/dia5_pf6_04_comentarios_review_comprobados (2).png" alt="Comprobación adicional de los comentarios del code review" width="900">
</p>

<p align="center"><em>La segunda evidencia muestra la comprobación adicional utilizada para justificar por qué los comentarios no requerían cambios en la implementación.</em></p>

Una vez resueltas las conversaciones realicé el merge normal del Pull Request, sin squash.

<p align="center">
  <img src="evidencia/dia5/dia5_pf6_05_pr_merged.png" alt="Pull Request final de assignee fusionado" width="900">
</p>

<p align="center"><em>Después de revisar los comentarios, el PR #5 quedó fusionado a `main`.</em></p>

El merge quedó registrado en:

```text
f6522e3 Merge pull request #5 from SergioPrado7/feature/assignee
```

### 21.16 PF-7 — Verificación final y documentación de `semana6`

Ya en `main`, repetí la suite y el verificador para comprobar el estado después del merge.

El resultado final de Maven fue:

```text
Tests run: 83, Failures: 0, Errors: 0, Skipped: 0
```

y la comprobación REST terminó nuevamente en:

```text
RESULTADO: 16/16 OK
```

<p align="center">
  <img src="evidencia/dia5/dia5_pf7_01_verificacion_final_main.png" alt="Verificación final de main después del merge" width="900">
</p>

<p align="center"><em>Después del merge repetí las verificaciones directamente sobre `main`: 83 tests sin fallos ni errores y el verificador REST completo en verde.</em></p>

También completé `semana6/README.md` con:

- feature elegida;
- URL y merge del PR;
- resultados de tests;
- salida completa del verificador;
- revisión del agente;
- los dos comentarios falsos del code review y su comprobación;
- créditos del proyecto final.

Los valores registrados fueron:

```text
Implementación PF-2: 5.18 AI credits
Revisión PF-3:       2.06 AI credits
Correcciones:        0 AI credits
Uso mensual:         147 / 1500 AI credits
```

Finalmente publiqué la documentación.

<p align="center">
  <img src="evidencia/dia5/dia5_pf7_03_documentacion_final_publicada.png" alt="Documentación final del proyecto publicada" width="900">
</p>

<p align="center"><em>La carpeta `semana6` quedó publicada con el README final, el diff del proyecto, la revisión y el transcript sanitizado de implementación.</em></p>

### 21.17 Demo y entrega

Para la demo final preparé un recorrido de cinco minutos mostrando:

1. la feature `assignee` y su regla principal;
2. el PR y el code review;
3. `RESULTADO: 16/16 OK`;
4. un ejemplo de un hallazgo incorrecto de Copilot y cómo lo comprobé;
5. el consumo de créditos y qué haría para gastar menos.

<p align="center">
  <img src="evidencia/dia5/dia5_10_demo_proyecto_final.png" alt="Demo del proyecto final preparada" width="900">
</p>

<p align="center"><em>La demo resume el flujo completo: especificación, implementación, revisión, comprobación REST, Pull Request y consumo de créditos.</em></p>

También revisé la URL del repositorio para asegurarme de que pudiera utilizarse como entrega y portafolio.

<p align="center">
  <img src="evidencia/dia5/dia5_10_repo_publico_y_entrega_lista.png" alt="Repositorio accesible y entrega preparada" width="900">
</p>

<p align="center"><em>Antes de entregar comprobé que el repositorio y la carpeta `semana6` estuvieran disponibles para revisión.</em></p>

### 21.18 DoD final del Día 5

La comprobación de terminado confirmó:

- VS Code y Copilot funcionando sobre el mismo repositorio;
- autocompletado probado y cambio experimental descartado;
- instrucciones, skills y agentes disponibles;
- `revisor` sin capacidad de editar;
- `.vscode/mcp.json` versionado;
- servidores MCP comprobados;
- `assignee` integrada mediante PR;
- ningún test anterior modificado;
- **83 tests**, 0 fallos y 0 errores;
- `verificar.ps1` en **16/16 OK**;
- `semana6/` completa;
- PR #5 fusionado a `main`;
- working tree final limpio.

<p align="center">
  <img src="evidencia/dia5/dia5_final_01_dod_completado.png" alt="DoD final del Día 5 completado" width="900">
</p>

<p align="center"><em>El cierre técnico del Día 5 dejó `main` actualizado, el proyecto final integrado, las pruebas en verde y toda la documentación del proyecto publicada.</em></p>

---

---

## Cierre

- **Créditos:** al cierre GitHub mostraba `147 / 1500` AI credits usados en septiembre. Del proyecto final medí `5.18` para la implementación con la skill y `2.06` para la revisión del agente `revisor`, sin créditos adicionales de corrección. Para gastar menos, evitaría repetir prompts solo para obtener evidencia y comprobaría primero con Git, Maven, REST y scripts locales.
- **Una cosa que el agente hizo mal:** en el Día 2 Copilot generó un slice test de `overdue` que comprobaba el segundo elemento de una lista cuyo orden ya venía preparado por el mock. Lo detecté yo durante el checklist, corregí el test para que no afirmara una propiedad que no estaba comprobando y volví a ejecutar la suite.

---

## 22. Conclusión general

Durante estos cinco días aprendí a usar GitHub Copilot de una forma más completa y controlada dentro de un proyecto real. Trabajé con especificaciones, pruebas, Pull Requests, MCP, skills, agentes personalizados y VS Code, siempre verificando los resultados antes de aceptar cambios. Lo principal fue que Copilot puede ahorrar tiempo y ayudar mucho durante el desarrollo, pero es importante revisar el código, limitar los permisos y comprobar todo con pruebas y herramientas independientes. Al final, TaskFlow quedó como evidencia de todo el trabajo realizado durante la academia.