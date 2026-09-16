package com.taskflow.slice;

import com.taskflow.controller.ProjectController;
import com.taskflow.dto.ProjectSummaryResponse;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import com.taskflow.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getSummary_proyectoExistente_devuelveResumen() throws Exception {
        Project proyecto = new Project(2L, "App Móvil", "d", 2L, null);
        when(projectService.buscarPorId(2L)).thenReturn(Optional.of(proyecto));
        when(projectService.getSummary(proyecto)).thenReturn(new com.taskflow.service.ProjectSummary(4L, Map.of(
                TaskStatus.TODO, 1L, TaskStatus.IN_PROGRESS, 2L, TaskStatus.DONE, 1L), 1L));

        mockMvc.perform(get("/projects/2/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(2))
                .andExpect(jsonPath("$.projectName").value("App Móvil"))
                .andExpect(jsonPath("$.totalTasks").value(4))
                .andExpect(jsonPath("$.byStatus.TODO").value(1))
                .andExpect(jsonPath("$.byStatus.IN_PROGRESS").value(2))
                .andExpect(jsonPath("$.byStatus.DONE").value(1))
                .andExpect(jsonPath("$.overdue").value(1));
    }

    @Test
    void getSummary_proyectoInexistente_devuelve404() throws Exception {
        when(projectService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/projects/99/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSummary_proyectoSinTareas_devuelveCerosEnJSON() throws Exception {
        Project proyecto3 = new Project(3L, "P3", "d", 1L, null);
        when(projectService.buscarPorId(3L)).thenReturn(Optional.of(proyecto3));
        when(projectService.getSummary(proyecto3)).thenReturn(new com.taskflow.service.ProjectSummary(0L, Map.of(
                TaskStatus.TODO, 0L, TaskStatus.IN_PROGRESS, 0L, TaskStatus.DONE, 0L), 0L));

        mockMvc.perform(get("/projects/3/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(3))
                .andExpect(jsonPath("$.projectName").value("P3"))
                .andExpect(jsonPath("$.totalTasks").value(0))
                .andExpect(jsonPath("$.byStatus.TODO").value(0))
                .andExpect(jsonPath("$.byStatus.IN_PROGRESS").value(0))
                .andExpect(jsonPath("$.byStatus.DONE").value(0))
                .andExpect(jsonPath("$.overdue").value(0));
    }
}
