package com.hospital.AppointmentsTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.AppointmentController;
import com.hospital.entity.Appointment;
import com.hospital.entity.Nurse;
import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.projection.AppointmentProjection;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.AppointmentService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

       
    @Autowired private MockMvc mockMvc;
    @MockBean  private AppointmentService appointmentService;
    @MockBean  private AppointmentRepository appointmentRepository;
    @MockBean  private PatientRepository patientRepository;
    @MockBean  private PhysicianRepository physicianRepository;
    @MockBean  private NurseRepository nurseRepository;
    @Autowired private ObjectMapper objectMapper;

    // ── Concrete projection (Jackson-serializable) ─────────────────────────────
    private AppointmentProjection projection(int id) {
        return new AppointmentProjection() {
            public Integer       getAppointmentId()   { return id;           }
            public Integer       getPatientSsn()      { return 101;          }
            public String        getPatientName()     { return "Tony Stark"; }
            public Integer       getPrepNurseId()     { return null;         }
            public String        getPrepNurseName()   { return null;         }
            public Integer       getPhysicianId()     { return 1;            }
            public String        getPhysicianName()   { return "Dr Strange"; }
            public LocalDateTime getStart()           { return null;         }
            public LocalDateTime getEnd()             { return null;         }
            public String        getExaminationRoom() { return "Room A";     }
        };
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated appointments")
    void testGetAll() throws Exception {

        Page<AppointmentProjection> page =
                new PageImpl<>(List.of(projection(1)), PageRequest.of(0, 5), 1);

        when(appointmentRepository.findAllProjectedBy(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return appointment by id")
    void testGetById() throws Exception {

        when(appointmentRepository.findProjectedByAppointmentId(1))
                .thenReturn(Optional.of(projection(1)));

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when appointment not found")
    void testGetByIdNotFound() throws Exception {

        when(appointmentRepository.findProjectedByAppointmentId(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create appointment")
    void testCreateAppointment() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("appointmentId", 1);
        request.put("patientSsn", 101);
        request.put("physicianId", 1);
        request.put("start", "2026-05-16T10:00");
        request.put("end", "2026-05-16T11:00");
        request.put("examinationRoom", "Room A");

        Patient patient = new Patient(); patient.setSsn(101);
        Physician physician = new Physician(); physician.setEmployeeId(1);
        Appointment saved = new Appointment(); saved.setAppointmentId(1);

        when(appointmentService.existsById(1)).thenReturn(false);
        when(patientRepository.findById(101)).thenReturn(Optional.of(patient));
        when(physicianRepository.findById(1)).thenReturn(Optional.of(physician));
        when(appointmentService.save(any(Appointment.class))).thenReturn(saved);
        when(appointmentRepository.findProjectedByAppointmentId(1))
                .thenReturn(Optional.of(projection(1)));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 409 when appointment already exists")
    void testCreateConflict() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("appointmentId", 1);
        request.put("patientSsn", 101);
        request.put("physicianId", 1);
        request.put("start", "2026-05-16T10:00");
        request.put("end", "2026-05-16T11:00");
        request.put("examinationRoom", "Room A");

        when(appointmentService.existsById(1)).thenReturn(true);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 400 when required field is missing")
    void testCreateBadRequest() throws Exception {

        // missing patientSsn
        Map<String, Object> request = new HashMap<>();
        request.put("appointmentId", 1);
        request.put("physicianId", 1);
        request.put("start", "2026-05-16T10:00");
        request.put("end", "2026-05-16T11:00");
        request.put("examinationRoom", "Room A");

        when(appointmentService.existsById(1)).thenReturn(false);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update appointment")
    void testUpdateAppointment() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("patientSsn", 101);
        request.put("physicianId", 1);
        request.put("start", "2026-05-16T10:00");
        request.put("end", "2026-05-16T12:00");
        request.put("examinationRoom", "Room B");

        Patient patient = new Patient(); patient.setSsn(101);
        Physician physician = new Physician(); physician.setEmployeeId(1);
        Appointment existing = new Appointment(); existing.setAppointmentId(1);

        when(appointmentService.getById(1)).thenReturn(existing);
        when(patientRepository.findById(101)).thenReturn(Optional.of(patient));
        when(physicianRepository.findById(1)).thenReturn(Optional.of(physician));
        when(appointmentService.save(any(Appointment.class))).thenReturn(existing);
        when(appointmentRepository.findProjectedByAppointmentId(1))
                .thenReturn(Optional.of(projection(1)));

        mockMvc.perform(put("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete appointment")
    void testDeleteAppointment() throws Exception {

        doNothing().when(appointmentService).delete(1);

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment 1 deleted successfully."));
    }

    @Test
    @DisplayName("Should return 404 when patient not found during create")
    void testCreatePatientNotFound() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("appointmentId", 1);
        request.put("patientSsn", 999);
        request.put("physicianId", 1);
        request.put("start", "2026-05-16T10:00");
        request.put("end", "2026-05-16T11:00");
        request.put("examinationRoom", "Room A");

        when(appointmentService.existsById(1)).thenReturn(false);
        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
