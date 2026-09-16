# Proyecto final · Semana 6 · GitHub Copilot

**Alumno:** `Sergio Servando Prado Lozano` · **Usuario de GitHub:** `SergioPrado7`

## 1. Qué construí

| | Feature | Especificación |
|---|---|---|
| [x] | `PATCH /tasks/{id}/assignee` — cambiar el responsable | [`specs/assignee.md`](../specs/assignee.md) |

## 2. El pull request

- **URL del PR (mergeado):** `https://github.com/SergioPrado7/taskflow-copilot-sergioprado7/pull/5`
- **Commit del merge en `main`:** `f6522e3 (HEAD -> main, origin/main, origin/HEAD) Merge pull request #5 from SergioPrado7/feature/assignee`
- **Comentarios de Copilot code review:** `2`

## 3. Cómo lo hice

| Paso | Qué hice | Evidencia |
|---|---|---|
| Rama y spec | `git switch -c feature/assignee` y copié la spec a `specs/` | `specs/assignee.md` y el historial de commits de la rama |
| Implementación | `copilot -p "/crear-endpoint-taskflow …"` con `gpt-5-mini` | `semana6/sesion-implementacion.md` contiene `Skill "crear-endpoint-taskflow" loaded successfully` |
| Revisión | agente `revisor` sobre `semana6/proyecto-final.diff` | `semana6/revision.md` terminó con `Veredicto: APROBADO` |
| Tests | `mvn test` en verde | `Tests run: 83, Failures: 0, Errors: 0, Skipped: 0` |
| Comprobación REST | `verificar.ps1` con `casos-assignee.ps1` | sección 5 de este documento: `RESULTADO: 16/16 OK` |
| Code review | Copilot en el PR | Pull Request #5, pestaña **Files changed**; Copilot dejó 2 comentarios que fueron comprobados manualmente |

## 4. Qué hizo el agente y qué corregí yo

| # | Qué hizo mal el agente (archivo) | Quién lo detectó | Cómo quedó corregido |
|---|---|---|---|
| 1 | Copilot Code Review afirmó que `.github/skills/verificar-taskflow/verificar.ps1` construía `$auth` con un token de reemplazo `******`, por lo que los PATCH recibirían `401`. | Copilot review; comprobado manualmente | El hallazgo resultó falso. `verificar.ps1` construye `$auth` con `Authorization = "Bearer $($login.token)"` después del login real. Además, la verificación completa terminó en `RESULTADO: 16/16 OK`, incluyendo los casos autenticados de `assignee`. No fue necesario cambiar código. |
| 2 | Copilot Code Review pidió agregar `/tasks/1/assignee` a `SecurityRulesTest` para comprobar su autenticación. | Copilot review; comprobado manualmente | El hallazgo no requería cambios para la spec. `specs/assignee.md` indica reutilizar la seguridad existente y no modificar `SecurityConfig`; esta usa `.anyRequest().authenticated()`. Además, el verificador REST comprobó que `PATCH /tasks/6/assignee` sin token responde `401`. No se modificaron tests preexistentes. |

**Lo que el agente hizo bien a la primera:** La implementación de `PATCH /tasks/{id}/assignee` cumplió la especificación, agregó exactamente las dos clases nuevas de test requeridas y la suite quedó en verde. El agente `revisor` aprobó la implementación sin solicitar cambios.

## 5. Comprobaciones REST

```text
[OK]    GET /tasks/overdue devuelve solo la tarea 7
[OK]    GET /tasks/unassigned devuelve las tareas 4 y 6
[OK]    GET /projects/1/summary
[OK]    GET /projects/2/summary
[OK]    GET /projects/3/summary
[OK]    GET /projects/99/summary responde 404
[OK]    GET /projects/1/summary sin token responde 401
[OK]    PATCH /tasks/4/assignee asigna a luis
[OK]    GET /tasks/4 conserva el responsable nuevo
[OK]    PATCH /tasks/4/status a DONE ahora responde 200
[OK]    PATCH /tasks/2/assignee (DONE) responde 422
[OK]    PATCH /tasks/99/assignee responde 404
[OK]    PATCH /tasks/6/assignee con {} responde 400 y nombra assigneeId
[OK]    PATCH /tasks/6/assignee con 0 responde 400
[OK]    PATCH /tasks/6/assignee sin token responde 401
App detenida (PID 18336).
[OK]    App apagada: el puerto 8080 ya no responde
RESULTADO: 16/16 OK
```

## 6. Créditos de la semana

| Qué | AI credits |
|---|---:|
| Usados en septiembre según github.com (incluye semanas anteriores si usaste Copilot antes) | `147 / 1500` |
| Implementación con la skill (`AI Credits` del PF-2) | `5.18` |
| Revisión del `revisor` (`AI Credits` del PF-3) | `2.06` |
| Correcciones del PF-4 y del PF-6, si hubo (`AI Credits`) | `0` |

> El contador mensual mostrado por GitHub al cierre fue `147 / 1500 AI credits`, con reinicio indicado para el 30 de septiembre de 2026.
