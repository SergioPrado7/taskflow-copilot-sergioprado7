package com.taskflow.unit;

import com.taskflow.exception.TaskStateException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReasignarTareaServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void reasignar_tareaTODO_sinResponsable_seGuardaConNuevoAssignee() throws TaskValidationException {
        Task tarea = new Task(10L, "Titulo", "desc", TaskStatus.TODO, Priority.MED, 1L, null, null);

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Task resultado = taskService.reasignar(tarea, 5L);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(repository).save(captor.capture());
        assertEquals(5L, captor.getValue().getAssigneeId());
        assertEquals(5L, resultado.getAssigneeId());
    }

    @Test
    void reasignar_tareaDONE_lanzaTaskStateException_y_noSave() throws TaskValidationException {
        Task tarea = new Task(2L, "Hecho", "desc", TaskStatus.DONE, Priority.MED, 1L, 1L, null);

        assertThrows(TaskStateException.class, () -> taskService.reasignar(tarea, 3L));
        verify(repository, never()).save(any());
    }
}
