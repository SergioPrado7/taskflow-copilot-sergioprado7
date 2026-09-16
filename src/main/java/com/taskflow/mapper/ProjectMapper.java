package com.taskflow.mapper;

import com.taskflow.dto.ProjectResponse;
import com.taskflow.dto.ProjectSummaryResponse;
import com.taskflow.model.Project;
import com.taskflow.model.TaskStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * ProjectMapper — puente DTO <-> dominio del lado Project. Estático, a mano, sin MapStruct.
 */
public final class ProjectMapper {

    private ProjectMapper() {
        // no instanciable
    }

    /** Entidad -> DTO de salida. Ahora ownerId sale directo del campo (sin puente por objeto). */
    public static ProjectResponse aResponse(Project p) {
        return new ProjectResponse(p.getId(), p.getName(), p.getDescription(),
                p.getOwnerId(), p.getCreatedAt());
    }

    /** Crea el DTO de resumen del proyecto a partir de los conteos calculados. */
    public static ProjectSummaryResponse aSummary(Project p, long totalTasks,
                                                  Map<TaskStatus, Long> byStatusLong, long overdue) {
        Map<String, Integer> byStatus = new HashMap<>();
        for (TaskStatus s : TaskStatus.values()) {
            byStatus.put(s.name(), byStatusLong.getOrDefault(s, 0L).intValue());
        }
        return new ProjectSummaryResponse(p.getId(), p.getName(), (int) totalTasks, byStatus, (int) overdue);
    }
}
