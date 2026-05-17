package com.hospital.Nurse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.NurseController;
import com.hospital.entity.Nurse;
import com.hospital.projection.NurseProjection;
import com.hospital.repository.NurseRepository;
import com.hospital.service.AppointmentService;
import com.hospital.service.NurseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NurseController.class)
class NurseControllerTest {


    @Autowired private MockMvc mockMvc;
    @MockBean  private NurseService nurseService;
    @MockBean  private NurseRepository nurseRepository;
    @MockBean  private AppointmentService appointmentService;
    @Autowired private ObjectMapper objectMapper;

    // ── Concrete projection (Jackson-serializable) ─────────────────────────────
    private NurseProjection projection(int id, String name, String position,
                                       boolean registered, int ssn) {
        return new NurseProjection() {
            public Integer getEmployeeId() { return id;         }
            public String  getName()       { return name;       }
            public String  getPosition()   { return position;   }
            public Boolean getRegistered() { return registered; }
            public Integer getSsn()        { return ssn;        }
        };
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated nurses")
    void testGetAll() throws Exception {

        Page<NurseProjection> page =
                new PageImpl<>(List.of(projection(1, "Carla", "Head Nurse", true, 12345)),
                        PageRequest.of(0, 5), 1);

        when(nurseRepository.findAllProjectedBy(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/nurses"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should search nurses by name")
    void testSearch() throws Exception {

        Page<NurseProjection> page =
                new PageImpl<>(List.of(projection(1, "Carla", "Head Nurse", true, 12345)),
                        PageRequest.of(0, 5), 1);

        when(nurseRepository.findByNameContainingIgnoreCase(eq("Carla"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/nurses/search?name=Carla"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return nurse by id")
    void testGetById() throws Exception {

        when(nurseRepository.findProjectedByEmployeeId(1))
                .thenReturn(Optional.of(projection(1, "Carla", "Head Nurse", true, 12345)));

        mockMvc.perform(get("/api/nurses/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when nurse not found")
    void testGetByIdNotFound() throws Exception {

        when(nurseRepository.findProjectedByEmployeeId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/nurses/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create nurse")
    void testCreateNurse() throws Exception {

        Nurse nurse = new Nurse(10, "Dr Nurse", "ICU Nurse", true, 999999);

        when(nurseService.existsById(10)).thenReturn(false);
        when(nurseService.save(any(Nurse.class))).thenReturn(nurse);
        when(nurseRepository.findProjectedByEmployeeId(10))
                .thenReturn(Optional.of(projection(10, "Dr Nurse", "ICU Nurse", true, 999999)));

        mockMvc.perform(post("/api/nurses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 400 when nurse name is missing")
    void testCreateMissingName() throws Exception {

        Nurse nurse = new Nurse(10, null, "ICU Nurse", true, 999999);

        mockMvc.perform(post("/api/nurses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when nurse already exists")
    void testCreateConflict() throws Exception {

        Nurse nurse = new Nurse(10, "Dr Nurse", "ICU Nurse", true, 999999);

        when(nurseService.existsById(10)).thenReturn(true);

        mockMvc.perform(post("/api/nurses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should update nurse")
    void testUpdateNurse() throws Exception {

        Nurse nurse = new Nurse(1, "Updated Nurse", "Senior Nurse", true, 11111);

        when(nurseService.getById(1)).thenReturn(nurse);
        when(nurseService.save(any(Nurse.class))).thenReturn(nurse);
        when(nurseRepository.findProjectedByEmployeeId(1))
                .thenReturn(Optional.of(projection(1, "Updated Nurse", "Senior Nurse", true, 11111)));

        mockMvc.perform(put("/api/nurses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete nurse")
    void testDeleteNurse() throws Exception {

        Nurse nurse = new Nurse(1, "Carla", "Head Nurse", true, 12345);
        when(nurseService.getById(1)).thenReturn(nurse);
        when(appointmentService.getByPrepNurse(1)).thenReturn(List.of());
        doNothing().when(nurseService).delete(1);

        mockMvc.perform(delete("/api/nurses/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Nurse 1 deleted successfully."));
    }
}
