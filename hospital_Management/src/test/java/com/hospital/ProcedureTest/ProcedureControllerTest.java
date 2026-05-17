package com.hospital.ProcedureTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.ProcedureController;
import com.hospital.entity.Procedure;
import com.hospital.projection.ProcedureProjection;
import com.hospital.repository.ProcedureRepository;
import com.hospital.service.ProcedureService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcedureController.class)
class ProcedureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcedureService procedureService;

    @MockBean
    private ProcedureRepository procedureRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return paginated procedures")
    void testGetAllProcedures() throws Exception {
        ProcedureProjection projection =
                new TestProcedureProjection(1, "Appendectomy", 1500.0);

        Page<ProcedureProjection> page =
                new PageImpl<>(
                        List.of(projection),
                        PageRequest.of(0, 5),
                        1
                );

        when(procedureRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/procedures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Appendectomy"))
                .andExpect(jsonPath("$.content[0].cost").value(1500.0));
    }

    @Test
    @DisplayName("Should return procedure by code")
    void testGetProcedureByCode() throws Exception {
        ProcedureProjection projection =
                new TestProcedureProjection(1, "Appendectomy", 1500.0);

        when(procedureRepository.findProjectedByCode(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(get("/api/procedures/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.name").value("Appendectomy"))
                .andExpect(jsonPath("$.cost").value(1500.0));
    }

    @Test
    @DisplayName("Should return 404 when procedure is not found")
    void testGetProcedureByCodeNotFound() throws Exception {
        when(procedureRepository.findProjectedByCode(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/procedures/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create procedure")
    void testCreateProcedure() throws Exception {
        Procedure procedure =
                new Procedure(2, "CT Scan", 800.0);
        ProcedureProjection projection =
                new TestProcedureProjection(2, "CT Scan", 800.0);

        when(procedureService.existsById(2))
                .thenReturn(false);
        when(procedureService.save(any(Procedure.class)))
                .thenReturn(procedure);
        when(procedureRepository.findProjectedByCode(2))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        post("/api/procedures")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(procedure))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(2))
                .andExpect(jsonPath("$.name").value("CT Scan"))
                .andExpect(jsonPath("$.cost").value(800.0));
    }

    @Test
    @DisplayName("Should return bad request when code is missing")
    void testCreateProcedureWithoutCode() throws Exception {
        Procedure procedure =
                new Procedure(null, "MRI", 1200.0);

        mockMvc.perform(
                        post("/api/procedures")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(procedure))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when name is blank")
    void testCreateProcedureWithoutName() throws Exception {
        Procedure procedure =
                new Procedure(3, "", 1200.0);

        mockMvc.perform(
                        post("/api/procedures")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(procedure))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return conflict when procedure already exists")
    void testCreateExistingProcedure() throws Exception {
        Procedure procedure =
                new Procedure(4, "X-Ray", 300.0);

        when(procedureService.existsById(4))
                .thenReturn(true);

        mockMvc.perform(
                        post("/api/procedures")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(procedure))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should update procedure")
    void testUpdateProcedure() throws Exception {
        Procedure request =
                new Procedure(99, "Updated Procedure", 2500.0);
        Procedure existing =
                new Procedure(5, "Old Procedure", 2000.0);
        ProcedureProjection projection =
                new TestProcedureProjection(5, "Updated Procedure", 2500.0);

        when(procedureService.getById(5))
                .thenReturn(existing);
        when(procedureService.save(any(Procedure.class)))
                .thenReturn(request);
        when(procedureRepository.findProjectedByCode(5))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        put("/api/procedures/5")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(5))
                .andExpect(jsonPath("$.name").value("Updated Procedure"))
                .andExpect(jsonPath("$.cost").value(2500.0));

        ArgumentCaptor<Procedure> procedureCaptor =
                ArgumentCaptor.forClass(Procedure.class);
        verify(procedureService).save(procedureCaptor.capture());
        assertEquals(5, procedureCaptor.getValue().getCode());
    }

    @Test
    @DisplayName("Should delete procedure")
    void testDeleteProcedure() throws Exception {
        mockMvc.perform(delete("/api/procedures/6"))
                .andExpect(status().isOk())
                .andExpect(content().string("Procedure 6 deleted successfully."));

        verify(procedureService).delete(6);
    }

    private static class TestProcedureProjection implements ProcedureProjection {

        private final Integer code;
        private final String name;
        private final Double cost;

        private TestProcedureProjection(Integer code, String name, Double cost) {
            this.code = code;
            this.name = name;
            this.cost = cost;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Double getCost() {
            return cost;
        }
    }
}
