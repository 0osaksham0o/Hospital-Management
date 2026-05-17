package com.hospital.DepartmentTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.DepartmentController;
import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import com.hospital.projection.DepartmentProjection;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;

import com.hospital.service.DepartmentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ===== MOCK DEPENDENCIES =====
    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private PhysicianRepository physicianRepository;

    // =====================================================
    // ✅ GET ALL
    // =====================================================
    @Test
    void shouldReturnAllDepartments() throws Exception {

        DepartmentProjection projection = new DepartmentProjection() {
            public Integer getDepartmentId() { return 1; }
            public String getName() { return "Cardiology"; }
            public Integer getHeadId() { return 10; }
            public String getHeadName() { return "Dr. Sharma"; }
        };

        Page<DepartmentProjection> page =
                new PageImpl<>(List.of(projection));

        when(departmentRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].departmentId").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Cardiology"));
    }

    // =====================================================
    // ✅ GET BY ID
    // =====================================================
    @Test
    void shouldReturnDepartmentById() throws Exception {

        DepartmentProjection projection = new DepartmentProjection() {
            public Integer getDepartmentId() { return 1; }
            public String getName() { return "Cardiology"; }
            public Integer getHeadId() { return 10; }
            public String getHeadName() { return "Dr. Sharma"; }
        };

        when(departmentRepository.findProjectedByDepartmentId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }

    // =====================================================
    // ✅ CREATE
    // =====================================================
    @Test
    void shouldCreateDepartment() throws Exception {

        Physician physician = new Physician();
        physician.setEmployeeId(10);

        when(physicianRepository.findById(10))
                .thenReturn(Optional.of(physician));

        when(departmentService.existsById(any()))
                .thenReturn(false);

        Department saved = new Department();
        saved.setDepartmentId(1);

        when(departmentService.save(any()))
                .thenReturn(saved);

        DepartmentProjection projection = new DepartmentProjection() {
            public Integer getDepartmentId() { return 1; }
            public String getName() { return "Cardiology"; }
            public Integer getHeadId() { return 10; }
            public String getHeadName() { return "Dr. Sharma"; }
        };

        when(departmentRepository.findProjectedByDepartmentId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "departmentId",1,
                                        "name","Cardiology",
                                        "headId",10
                                ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departmentId").value(1));
    }

    // =====================================================
    // ✅ UPDATE
    // =====================================================
    @Test
    void shouldUpdateDepartment() throws Exception {

        Physician physician = new Physician();
        physician.setEmployeeId(10);

        when(physicianRepository.findById(10))
                .thenReturn(Optional.of(physician));

        when(departmentService.getById(1))
                .thenReturn(new Department());

        when(departmentService.save(any()))
                .thenReturn(new Department());

        DepartmentProjection projection = new DepartmentProjection() {
            public Integer getDepartmentId() { return 1; }
            public String getName() { return "Updated"; }
            public Integer getHeadId() { return 10; }
            public String getHeadName() { return "Dr. Sharma"; }
        };

        when(departmentRepository.findProjectedByDepartmentId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "name","Updated",
                                        "headId",10
                                ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    // =====================================================
    // ✅ DELETE
    // =====================================================
    @Test
    void shouldDeleteDepartment() throws Exception {

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Department 1 deleted successfully."));
    }
}
