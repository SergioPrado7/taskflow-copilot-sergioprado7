package com.taskflow.unit;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import com.taskflow.service.ProjectService;
import com.taskflow.service.ProjectSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ProjectService projectService;

    private Project proyecto;
    @BeforeEach
    void setup() {
        proyecto = new Project(2L, "App Móvil", "d", 2L, null);
    }

    @Test
    void getSummary_proyectoConTareas_calculaConteosCorrectos() throws Exception {
        Task t1 = new Task(5L, "T-1", "d", TaskStatus.TODO, Priority.MED, 2L, null, null);
        Task t2 = new Task(6L, "T-2", "d", TaskStatus.IN_PROGRESS, Priority.HIGH, 2L, 1L, LocalDate.now().minusDays(1));
        Task t3 = new Task(7L, "T-3", "d", TaskStatus.IN_PROGRESS, Priority.LOW, 2L, null, null);
        Task t4 = new Task(8L, "T-4", "d", TaskStatus.DONE, Priority.MED, 2L, 1L, LocalDate.now().minusDays(5));
        when(taskRepository.findByProjectId(2L)).thenReturn(List.of(t1, t2, t3, t4));
        ProjectSummary resumen = projectService.getSummary(proyecto);
        assertEquals(4, resumen.getTotalTasks());        Map<TaskStatus, Long> byStatus = resumen.getByStatus();
        assertEquals(1L, byStatus.get(TaskStatus.TODO));
        assertEquals(2L, byStatus.get(TaskStatus.IN_PROGRESS));
        assertEquals(1L, byStatus.get(TaskStatus.DONE));
        assertEquals(1L, resumen.getOverdue());
    }
    @Test    void getSummary_proyectoSinTareas_devuelveCeros() {
        when(taskRepository.findByProjectId(3L)).thenReturn(List.of());
        Project proyecto3 = new Project(3L, "P3", "d", 1L, null);
        ProjectSummary resumen = projectService.getSummary(proyecto3);
        assertEquals(0, resumen.getTotalTasks());
        assertEquals(0L, resumen.getOverdue());
        Map<TaskStatus, Long> byStatus = resumen.getByStatus();
        for (TaskStatus s : TaskStatus.values()) {
            assertEquals(0L, byStatus.get(s));
        }
    }
}
