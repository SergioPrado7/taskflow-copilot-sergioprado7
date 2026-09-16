package com.taskflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para actualizar solo el responsable (assigneeId) de una tarea.
 */
public record TaskAssigneeUpdateRequest(@NotNull @Positive Long assigneeId) {
}
