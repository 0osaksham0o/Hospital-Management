package com.hospital.PhysicianTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.PhysicianController;
import com.hospital.entity.Physician;
import com.hospital.projection.PhysicianProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.AppointmentService;
import com.hospital.service.PhysicianService;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhysicianController.class)
class PhysicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhysicianService physicianService;

    @MockBean
    private PhysicianRepository physicianRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return paginated physicians")
    void testGetAllPhysicians() throws Exception {
        PhysicianProjection projection =
                new TestPhysicianProjection(1, "Dr John Smith", "Cardiologist", 111111111);

        Page<PhysicianProjection> page =
                new PageImpl<>(
                        List.of(projection),
                        PageRequest.of(0, 5),
                        1
                );

        when(physicianRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/physicians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employeeId").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Dr John Smith"))
                .andExpect(jsonPath("$.content[0].position").value("Cardiologist"))
                .andExpect(jsonPath("$.content[0].ssn").value(111111111));
    }

    @Test
    @DisplayName("Should search physicians by name")
    void testSearchPhysiciansByName() throws Exception {
        PhysicianProjection projection =
                new TestPhysicianProjection(2, "Dr Gregory House", "Diagnostician", 222222222);

        Page<PhysicianProjection> page =
                new PageImpl<>(
                        List.of(projection),
                        PageRequest.of(0, 5),
                        1
                );

        when(physicianRepository.findByNameContainingIgnoreCase(eq("house"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/physicians/search").param("name", "house"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employeeId").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Dr Gregory House"));
    }

    @Test
    @DisplayName("Should return all physicians when search name is blank")
    void testSearchPhysiciansWithBlankName() throws Exception {
        Page<PhysicianProjection> page =
                new PageImpl<>(
                        List.of(new TestPhysicianProjection(1, "Dr John Smith", "Cardiologist", 111111111)),
                        PageRequest.of(0, 5),
                        1
                );

        when(physicianRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/physicians/search").param("name", " "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employeeId").value(1));
    }

    @Test
    @DisplayName("Should return physician by employee id")
    void testGetPhysicianById() throws Exception {
        PhysicianProjection projection =
                new TestPhysicianProjection(1, "Dr John Smith", "Cardiologist", 111111111);

        when(physicianRepository.findProjectedByEmployeeId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(get("/api/physicians/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.name").value("Dr John Smith"));
    }

    @Test
    @DisplayName("Should return 404 when physician is not found")
    void testGetPhysicianByIdNotFound() throws Exception {
        when(physicianRepository.findProjectedByEmployeeId(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/physicians/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create physician")
    void testCreatePhysician() throws Exception {
        Physician physician =
                new Physician(3, "Dr Meredith Grey", "Surgeon", 333333333);
        PhysicianProjection projection =
                new TestPhysicianProjection(3, "Dr Meredith Grey", "Surgeon", 333333333);

        when(physicianService.existsById(3))
                .thenReturn(false);
        when(physicianService.save(any(Physician.class)))
                .thenReturn(physician);
        when(physicianRepository.findProjectedByEmployeeId(3))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        post("/api/physicians")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(physician))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(3))
                .andExpect(jsonPath("$.name").value("Dr Meredith Grey"));
    }

    @Test
    @DisplayName("Should return bad request when employee id is missing")
    void testCreatePhysicianWithoutEmployeeId() throws Exception {
        Physician physician =
                new Physician(null, "Dr Alex Carter", "Surgeon", 444444444);

        mockMvc.perform(
                        post("/api/physicians")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(physician))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when name is blank")
    void testCreatePhysicianWithoutName() throws Exception {
        Physician physician =
                new Physician(4, "", "Neurologist", 444444444);

        mockMvc.perform(
                        post("/api/physicians")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(physician))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return conflict when physician already exists")
    void testCreateExistingPhysician() throws Exception {
        Physician physician =
                new Physician(5, "Dr Existing", "Dermatologist", 555555555);

        when(physicianService.existsById(5))
                .thenReturn(true);

        mockMvc.perform(
                        post("/api/physicians")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(physician))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should update physician")
    void testUpdatePhysician() throws Exception {
        Physician request =
                new Physician(99, "Dr Updated", "Oncologist", 666666666);
        Physician existing =
                new Physician(6, "Dr Old", "Pathologist", 777777777);
        PhysicianProjection projection =
                new TestPhysicianProjection(6, "Dr Updated", "Oncologist", 666666666);

        when(physicianService.getById(6))
                .thenReturn(existing);
        when(physicianService.save(any(Physician.class)))
                .thenReturn(request);
        when(physicianRepository.findProjectedByEmployeeId(6))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        put("/api/physicians/6")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(6))
                .andExpect(jsonPath("$.name").value("Dr Updated"));

        ArgumentCaptor<Physician> physicianCaptor =
                ArgumentCaptor.forClass(Physician.class);
        verify(physicianService).save(physicianCaptor.capture());
        assertEquals(6, physicianCaptor.getValue().getEmployeeId());
    }

    @Test
    @DisplayName("Should delete physician")
    void testDeletePhysician() throws Exception {
        Physician existing = new Physician(7, "Dr Old", "Pathologist", 777777777);
        when(physicianService.getById(7)).thenReturn(existing);
        when(patientRepository.countByPrimaryCarePhysician_EmployeeId(7)).thenReturn(0L);
        when(appointmentService.countByPhysician(7)).thenReturn(0L);

        mockMvc.perform(delete("/api/physicians/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Physician 7 deleted successfully."));

        verify(physicianService).delete(7);
    }

    private static class TestPhysicianProjection implements PhysicianProjection {

        private final Integer employeeId;
        private final String name;
        private final String position;
        private final Integer ssn;

        private TestPhysicianProjection(Integer employeeId, String name, String position, Integer ssn) {
            this.employeeId = employeeId;
            this.name = name;
            this.position = position;
            this.ssn = ssn;
        }

        @Override
        public Integer getEmployeeId() {
            return employeeId;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPosition() {
            return position;
        }

        @Override
        public Integer getSsn() {
            return ssn;
        }
    }
}
