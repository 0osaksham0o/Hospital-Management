package com.hospital.OnCall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.OnCallController;
import com.hospital.entity.*;
import com.hospital.projection.OnCallProjection;
import com.hospital.repository.BlockRepository;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.OnCallRepository;
import com.hospital.service.OnCallService;

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

@WebMvcTest(OnCallController.class)
class OnCallControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean  private OnCallService onCallService;
    @MockBean  private OnCallRepository onCallRepository;
    @MockBean  private NurseRepository nurseRepository;
    @MockBean  private BlockRepository blockRepository;
    @Autowired private ObjectMapper objectMapper;

    // ── Concrete projection (Jackson-serializable) ─────────────────────────────
    private OnCallProjection projection(int nurseId, int floor, int code) {
        return new OnCallProjection() {
            public Integer       getNurseId()     { return nurseId; }
            public Integer       getBlockFloor()  { return floor;   }
            public Integer       getBlockCode()   { return code;    }
            public LocalDateTime getOnCallStart() { return null;    }
            public LocalDateTime getOnCallEnd()   { return null;    }
        };
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated on-call records")
    void testGetAll() throws Exception {

        Page<OnCallProjection> page =
                new PageImpl<>(List.of(projection(1, 1, 1)), PageRequest.of(0, 5), 1);

        when(onCallRepository.findAllProjectedBy(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/oncalls"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return on-call by composite id")
    void testGetById() throws Exception {

        OnCallId id = new OnCallId(1, 1, 1);

        when(onCallRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection(1, 1, 1)));

        mockMvc.perform(get("/api/oncalls/1/1/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when on-call record not found")
    void testGetByIdNotFound() throws Exception {

        OnCallId id = new OnCallId(999, 9, 9);

        when(onCallRepository.findProjectedById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/oncalls/999/9/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create on-call record")
    void testCreateOnCall() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("nurseId", 1);
        request.put("blockFloor", 1);
        request.put("blockCode", 1);
        request.put("onCallStart", "2026-05-16T08:00");
        request.put("onCallEnd", "2026-05-16T16:00");

        Nurse nurse = new Nurse(); nurse.setEmployeeId(1);
        Block block = new Block(new BlockId(1, 1));
        OnCall saved = new OnCall(new OnCallId(1, 1, 1), nurse, block, null, null);

        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));
        when(blockRepository.findById(new BlockId(1, 1))).thenReturn(Optional.of(block));
        when(onCallService.save(any(OnCall.class))).thenReturn(saved);
        when(onCallRepository.findProjectedById(any(OnCallId.class)))
                .thenReturn(Optional.of(projection(1, 1, 1)));

        mockMvc.perform(post("/api/oncalls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update on-call record")
    void testUpdateOnCall() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("onCallStart", "2026-05-16T08:00");
        request.put("onCallEnd", "2026-05-16T20:00");

        Nurse nurse = new Nurse(); nurse.setEmployeeId(1);
        Block block = new Block(new BlockId(1, 1));
        OnCallId id = new OnCallId(1, 1, 1);
        OnCall existing = new OnCall(id, nurse, block, null, null);

        when(onCallService.getById(id)).thenReturn(existing);
        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));
        when(blockRepository.findById(new BlockId(1, 1))).thenReturn(Optional.of(block));
        when(onCallService.save(any(OnCall.class))).thenReturn(existing);
        when(onCallRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection(1, 1, 1)));

        mockMvc.perform(put("/api/oncalls/1/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete on-call record")
    void testDeleteOnCall() throws Exception {

        doNothing().when(onCallService).delete(any(OnCallId.class));

        mockMvc.perform(delete("/api/oncalls/1/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("OnCall record deleted."));
    }
}
