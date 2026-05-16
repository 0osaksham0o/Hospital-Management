package com.hospital.PrescriptionTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.PrescriptionController;
import com.hospital.entity.Prescription;
import com.hospital.entity.PrescriptionId;
import com.hospital.projection.PrescriptionProjection;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.PrescriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
        import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrescriptionController.class)
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrescriptionRepository prescriptionRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return paginated prescriptions")
    void testGetAll() throws Exception {

        PrescriptionProjection projection =
                Mockito.mock(PrescriptionProjection.class);

        Page<PrescriptionProjection> page =
                new PageImpl<>(
                        java.util.List.of(projection),
                        PageRequest.of(0, 5),
                        1
                );

        when(prescriptionRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/prescriptions"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return prescription by id")
    void testGetById() throws Exception {

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        PrescriptionProjection projection =
                Mockito.mock(PrescriptionProjection.class);

        when(prescriptionRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        get("/api/prescriptions/1/101/1001")
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when prescription not found")
    void testGetByIdNotFound() throws Exception {

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        when(prescriptionRepository.findProjectedById(id))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/api/prescriptions/1/101/1001")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create prescription")
    void testCreatePrescription() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("physicianId", 1);
        request.put("patientSsn", 101);
        request.put("medicationCode", 1001);
        request.put("date", "2026-05-16");
        request.put("dose", "2 times daily");

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        Prescription prescription = new Prescription();
        prescription.setId(id);

        PrescriptionProjection projection =
                Mockito.mock(PrescriptionProjection.class);

        when(prescriptionRepository.save(any(Prescription.class)))
                .thenReturn(prescription);

        when(prescriptionRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        post("/api/prescriptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update prescription")
    void testUpdatePrescription() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("date", "2026-05-16");
        request.put("dose", "Updated dose");

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        PrescriptionProjection projection =
                Mockito.mock(PrescriptionProjection.class);

        when(prescriptionRepository.existsById(id))
                .thenReturn(true);

        when(prescriptionRepository.save(any(Prescription.class)))
                .thenReturn(new Prescription());

        when(prescriptionRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        put("/api/prescriptions/1/101/1001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing prescription")
    void testUpdateNotFound() throws Exception {

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        when(prescriptionRepository.existsById(id))
                .thenReturn(false);

        mockMvc.perform(
                        put("/api/prescriptions/1/101/1001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete prescription")
    void testDeletePrescription() throws Exception {

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        when(prescriptionRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(prescriptionRepository)
                .deleteById(id);

        mockMvc.perform(
                        delete("/api/prescriptions/1/101/1001")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Prescription deleted successfully."));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing prescription")
    void testDeleteNotFound() throws Exception {

        PrescriptionId id = new PrescriptionId(1, 101, 1001);

        when(prescriptionRepository.existsById(id))
                .thenReturn(false);

        mockMvc.perform(
                        delete("/api/prescriptions/1/101/1001")
                )
                .andExpect(status().isNotFound());
    }
}