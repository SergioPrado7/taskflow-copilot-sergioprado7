package com.taskflow.dto;

import java.util.Map;

/**
 * ProjectSummaryResponse — resumen compacto de un proyecto para vistas de tablero.
 */
public record ProjectSummaryResponse(
        Long projectId,
        String projectName,
        Integer totalTasks,
        Map<String, Integer> byStatus,
        Integer overdue
) {
}