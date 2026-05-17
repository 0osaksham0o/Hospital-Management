package com.hospital.AffiliatedWithTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.AffiliatedWithController;
import com.hospital.entity.*;
import com.hospital.projection.AffiliatedWithProjection;
import com.hospital.repository.AffiliatedWithRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.AffiliatedWithService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AffiliatedWithController.class)
class AffiliatedWithControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ===== MOCK DEPENDENCIES =====
    @MockBean
    private AffiliatedWithService service;

    @MockBean
    private AffiliatedWithRepository repository;

    @MockBean
    private PhysicianRepository physicianRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    // =====================================================
    // ✅ GET ALL
    // =====================================================
    @Test
    void shouldReturnAllAffiliations() throws Exception {

        AffiliatedWithProjection projection = new AffiliatedWithProjection() {
            public Integer getPhysicianId() { return 1; }
            public Integer getDepartmentId() { return 2; }
            public Boolean getPrimaryAffiliation() { return true; }
        };

        Page<AffiliatedWithProjection> page =
                new PageImpl<>(List.of(projection));

        when(repository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/affiliations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].physicianId").value(1))
                .andExpect(jsonPath("$.content[0].departmentId").value(2));
    }

    // =====================================================
    // ✅ GET BY ID
    // =====================================================
    @Test
    void shouldReturnAffiliationById() throws Exception {

        AffiliatedWithProjection projection = new AffiliatedWithProjection() {
            public Integer getPhysicianId() { return 1; }
            public Integer getDepartmentId() { return 2; }
            public Boolean getPrimaryAffiliation() { return true; }
        };

        when(repository.findProjectedById(any()))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(get("/api/affiliations/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.physicianId").value(1));
    }

    // =====================================================
    // ✅ CREATE
    // =====================================================
    @Test
    void shouldCreateAffiliation() throws Exception {

        Physician physician = new Physician();
        Department department = new Department();

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(departmentRepository.findById(2))
                .thenReturn(Optional.of(department));

        AffiliatedWith saved =
                new AffiliatedWith(
                        new AffiliatedWithId(1, 2),
                        physician,
                        department,
                        true
                );

        when(service.save(any())).thenReturn(saved);

        AffiliatedWithProjection projection = new AffiliatedWithProjection() {
            public Integer getPhysicianId() { return 1; }
            public Integer getDepartmentId() { return 2; }
            public Boolean getPrimaryAffiliation() { return true; }
        };

        when(repository.findProjectedById(any()))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(post("/api/affiliations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "physicianId", 1,
                                        "departmentId", 2,
                                        "primaryAffiliation", true
                                ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.physicianId").value(1));
    }

    // =====================================================
    // ✅ UPDATE
    // =====================================================
    @Test
    void shouldUpdateAffiliation() throws Exception {

        Physician physician = new Physician();
        Department department = new Department();

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(departmentRepository.findById(2))
                .thenReturn(Optional.of(department));

        when(service.getById(any()))
                .thenReturn(new AffiliatedWith());

        when(service.save(any()))
                .thenReturn(new AffiliatedWith(
                        new AffiliatedWithId(1,2),
                        physician,
                        department,
                        true
                ));

        AffiliatedWithProjection projection = new AffiliatedWithProjection() {
            public Integer getPhysicianId() { return 1; }
            public Integer getDepartmentId() { return 2; }
            public Boolean getPrimaryAffiliation() { return true; }
        };

        when(repository.findProjectedById(any()))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(put("/api/affiliations/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("primaryAffiliation", true))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.physicianId").value(1));
    }

    // =====================================================
    // ✅ DELETE
    // =====================================================
    @Test
    void shouldDeleteAffiliation() throws Exception {

        mockMvc.perform(delete("/api/affiliations/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Affiliation deleted."));
    }
}