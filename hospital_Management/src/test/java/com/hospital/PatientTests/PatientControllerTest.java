package com.hospital.PatientTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.PatientController;
import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.projection.PatientProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.PatientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private PhysicianRepository physicianRepository;

    private PatientProjection projection() {
        return new PatientProjection() {
            @Override
            public Integer getSsn() {
                return 101;
            }

            @Override
            public String getName() {
                return "John Doe";
            }

            @Override
            public String getAddress() {
                return "Mumbai";
            }

            @Override
            public String getPhone() {
                return "9999999999";
            }

            @Override
            public Integer getInsuranceId() {
                return 555;
            }

            @Override
            public Integer getPrimaryCarePhysicianId() {
                return 1;
            }

            @Override
            public String getPrimaryCarePhysicianName() {
                return "Dr Smith";
            }
        };
    }

    @Test
    void getAllPatients_shouldReturnPage() throws Exception {

        when(patientRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(projection())));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].ssn").value(101))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[0].address").value("Mumbai"))
                .andExpect(jsonPath("$.content[0].phone").value("9999999999"))
                .andExpect(jsonPath("$.content[0].insuranceId").value(555))
                .andExpect(jsonPath("$.content[0].primaryCarePhysicianId").value(1))
                .andExpect(jsonPath("$.content[0].primaryCarePhysicianName").value("Dr Smith"));

        verify(patientRepository, times(1))
                .findAllProjectedBy(any(Pageable.class));
    }

    @Test
    void searchPatients_shouldReturnFilteredPatients() throws Exception {

        when(patientRepository.findByNameContainingIgnoreCase(eq("john"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(projection())));

        mockMvc.perform(get("/api/patients/search")
                        .param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].ssn").value(101))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));

        verify(patientRepository, times(1))
                .findByNameContainingIgnoreCase(eq("john"), any(Pageable.class));
    }

    @Test
    void searchPatients_whenNoMatch_shouldReturnEmptyPage() throws Exception {

        when(patientRepository.findByNameContainingIgnoreCase(eq("unknown"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/patients/search")
                        .param("name", "unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(patientRepository, times(1))
                .findByNameContainingIgnoreCase(eq("unknown"), any(Pageable.class));
    }

    @Test
    void getPatientById_shouldReturnPatient() throws Exception {

        when(patientRepository.findProjectedBySsn(101))
                .thenReturn(Optional.of(projection()));

        mockMvc.perform(get("/api/patients/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ssn").value(101))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.primaryCarePhysicianId").value(1));

        verify(patientRepository, times(1))
                .findProjectedBySsn(101);
    }

    @Test
    void getPatientById_whenNotFound_shouldReturn404() throws Exception {

        when(patientRepository.findProjectedBySsn(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());

        verify(patientRepository, times(1))
                .findProjectedBySsn(999);
    }

    @Test
    void createPatient_shouldReturnCreatedPatient() throws Exception {

        Physician physician = new Physician();

        Patient patient = new Patient();
        patient.setSsn(101);

        when(patientService.existsById(101))
                .thenReturn(false);

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(patientService.save(any(Patient.class)))
                .thenReturn(patient);

        when(patientRepository.findProjectedBySsn(101))
                .thenReturn(Optional.of(projection()));

        Map<String, Object> body = Map.of(
                "ssn", 101,
                "name", "John Doe",
                "address", "Mumbai",
                "phone", "9999999999",
                "insuranceId", 555,
                "pcpId", 1
        );

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ssn").value(101))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(patientService, times(1))
                .save(any(Patient.class));
    }

    @Test
    void createPatient_whenPatientAlreadyExists_shouldReturn409() throws Exception {

        when(patientService.existsById(101))
                .thenReturn(true);

        Map<String, Object> body = Map.of(
                "ssn", 101,
                "name", "John Doe",
                "address", "Mumbai",
                "phone", "9999999999",
                "insuranceId", 555,
                "pcpId", 1
        );

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Patient with ID 101 already exists in the database."))
                .andExpect(jsonPath("$.status").value(409));

        verify(patientService, never())
                .save(any(Patient.class));
    }

    @Test
    void createPatient_whenPhysicianNotFound_shouldReturn404() throws Exception {

        when(patientService.existsById(101))
                .thenReturn(false);

        when(physicianRepository.findById(1))
                .thenReturn(Optional.empty());

        Map<String, Object> body = Map.of(
                "ssn", 101,
                "name", "John Doe",
                "address", "Mumbai",
                "phone", "9999999999",
                "insuranceId", 555,
                "pcpId", 1
        );

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Physician not found with ID : '1'"))
                .andExpect(jsonPath("$.status").value(404));

        verify(patientService, never())
                .save(any(Patient.class));
    }

    @Test
    void createPatient_withMissingName_shouldReturnBadRequest() throws Exception {

        Map<String, Object> body = Map.of(
                "ssn", 101
        );

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatient_withInvalidSsn_shouldReturnBadRequest() throws Exception {

        Map<String, Object> body = Map.of(
                "ssn", "abc",
                "name", "John"
        );

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePatient_shouldReturnUpdatedPatient() throws Exception {

        Physician physician = new Physician();

        Patient patient = new Patient();
        patient.setSsn(101);

        when(patientService.getById(101))
                .thenReturn(patient);

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(patientService.save(any(Patient.class)))
                .thenReturn(patient);

        when(patientRepository.findProjectedBySsn(101))
                .thenReturn(Optional.of(projection()));

        Map<String, Object> body = Map.of(
                "name", "John Doe Updated",
                "address", "Delhi",
                "phone", "8888888888",
                "insuranceId", 999,
                "pcpId", 1
        );

        mockMvc.perform(put("/api/patients/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ssn").value(101))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(patientService, times(1))
                .getById(101);

        verify(patientService, times(1))
                .save(any(Patient.class));
    }

    @Test
    void updatePatient_whenPatientNotFound_shouldReturn404() throws Exception {

        when(patientService.getById(101))
                .thenThrow(new com.hospital.exception.ResourceNotFoundException("Patient", "ID", 101));

        Map<String, Object> body = Map.of(
                "name", "John Doe Updated",
                "address", "Delhi",
                "phone", "8888888888",
                "insuranceId", 999,
                "pcpId", 1
        );

        mockMvc.perform(put("/api/patients/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(patientService, never())
                .save(any(Patient.class));
    }

    @Test
    void updatePatient_whenPhysicianNotFound_shouldReturn404() throws Exception {

        Patient patient = new Patient();
        patient.setSsn(101);

        when(patientService.getById(101))
                .thenReturn(patient);

        when(physicianRepository.findById(1))
                .thenReturn(Optional.empty());

        Map<String, Object> body = Map.of(
                "name", "John Doe Updated",
                "address", "Delhi",
                "phone", "8888888888",
                "insuranceId", 999,
                "pcpId", 1
        );

        mockMvc.perform(put("/api/patients/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Physician not found with ID : '1'"))
                .andExpect(jsonPath("$.status").value(404));

        verify(patientService, never())
                .save(any(Patient.class));
    }

    @Test
    void updatePatient_withInvalidBody_shouldReturnBadRequest() throws Exception {

        Map<String, Object> body = Map.of(
                "insuranceId", "abc"
        );

        mockMvc.perform(put("/api/patients/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePatient_shouldReturnSuccessMessage() throws Exception {

        doNothing().when(patientService)
                .delete(101);

        mockMvc.perform(delete("/api/patients/101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient 101 deleted successfully."));

        verify(patientService, times(1))
                .delete(101);
    }

    @Test
    void deletePatient_whenPatientNotFound_shouldReturn404() throws Exception {

        doThrow(new com.hospital.exception.ResourceNotFoundException("Patient", "ID", 999))
                .when(patientService)
                .delete(999);

        mockMvc.perform(delete("/api/patients/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(patientService, times(1))
                .delete(999);
    }
}