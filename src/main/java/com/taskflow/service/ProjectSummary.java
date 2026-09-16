package com.taskflow.service;

import com.taskflow.model.TaskStatus;
import java.util.Map;

/**
 * DTO interno del service con los conteos necesarios para construir el ProjectSummaryResponse.
 */
public class ProjectSummary {
    private final long totalTasks;
    private final java.util.Map<TaskStatus, Long> byStatus;
    private final long overdue;

    public ProjectSummary(long totalTasks, java.util.Map<TaskStatus, Long> byStatus, long overdue) {
        this.totalTasks = totalTasks;
        this.byStatus = byStatus;
        this.overdue = overdue;
    }
    public long getTotalTasks() {
        return totalTasks;
    }
    public java.util.Map<TaskStatus, Long> getByStatus() {
        return byStatus;
    }

    public long getOverdue() {
        return overdue;
    }
}
