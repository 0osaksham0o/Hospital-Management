package com.hospital.UndergoesTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.UndergoesController;
import com.hospital.entity.Undergoes;
import com.hospital.entity.UndergoesId;
import com.hospital.projection.UndergoesProjection;
import com.hospital.repository.UndergoesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UndergoesController.class)
class UndergoesControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean  private UndergoesRepository undergoesRepository;
    @Autowired private ObjectMapper objectMapper;

    // ── Concrete projection (Jackson-serializable) ─────────────────────────────
    private UndergoesProjection projection(int patientSsn, int procedureCode, int stayId) {
        return new UndergoesProjection() {
            public Integer       getPatientSsn()       { return patientSsn;    }
            public Integer       getProcedureCode()    { return procedureCode; }
            public Integer       getStayId()           { return stayId;        }
            public LocalDateTime getDateUndergoes()    { return null;          }
            public Integer       getPhysicianId()      { return null;          }
            public Integer       getAssistingNurseId() { return null;          }
        };
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated undergoes records")
    void testGetAll() throws Exception {

        Page<UndergoesProjection> page = new PageImpl<>(
                List.of(projection(101, 1, 1)), PageRequest.of(0, 5), 1);

        when(undergoesRepository.findAllProjectedBy(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/undergoes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return undergoes by composite id")
    void testGetById() throws Exception {

        UndergoesId id = new UndergoesId(101, 1, 1);

        when(undergoesRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection(101, 1, 1)));

        mockMvc.perform(get("/api/undergoes/101/1/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when undergoes not found")
    void testGetByIdNotFound() throws Exception {

        UndergoesId id = new UndergoesId(999, 9, 9);

        when(undergoesRepository.findProjectedById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/undergoes/999/9/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create undergoes record")
    void testCreateUndergoes() throws Exception {

        UndergoesId id = new UndergoesId(101, 1, 1);
        Undergoes undergoes = new Undergoes();
        undergoes.setId(id);

        when(undergoesRepository.save(any(Undergoes.class))).thenReturn(undergoes);
        when(undergoesRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection(101, 1, 1)));

        mockMvc.perform(post("/api/undergoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(undergoes)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update undergoes record")
    void testUpdateUndergoes() throws Exception {

        UndergoesId id = new UndergoesId(101, 1, 1);
        Undergoes undergoes = new Undergoes();
        undergoes.setId(id);

        when(undergoesRepository.existsById(id)).thenReturn(true);
        when(undergoesRepository.save(any(Undergoes.class))).thenReturn(undergoes);
        when(undergoesRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection(101, 1, 1)));

        mockMvc.perform(put("/api/undergoes/101/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(undergoes)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing undergoes")
    void testUpdateNotFound() throws Exception {

        UndergoesId id = new UndergoesId(999, 9, 9);
        Undergoes undergoes = new Undergoes();

        when(undergoesRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/api/undergoes/999/9/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(undergoes)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete undergoes record")
    void testDeleteUndergoes() throws Exception {

        UndergoesId id = new UndergoesId(101, 1, 1);

        when(undergoesRepository.existsById(id)).thenReturn(true);
        doNothing().when(undergoesRepository).deleteById(id);

        mockMvc.perform(delete("/api/undergoes/101/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Undergoes record [patient=101, procedure=1, stay=1] deleted successfully."));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing undergoes")
    void testDeleteNotFound() throws Exception {

        UndergoesId id = new UndergoesId(999, 9, 9);

        when(undergoesRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/undergoes/999/9/9"))
                .andExpect(status().isNotFound());
    }
}
