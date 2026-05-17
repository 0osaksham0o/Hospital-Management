package com.hospital.BlockTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.BlockController;
import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import com.hospital.projection.BlockProjection;
import com.hospital.repository.BlockRepository;
import com.hospital.service.BlockService;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlockController.class)
class BlockControllerTest {

    
    @Autowired private MockMvc mockMvc;
    @MockBean  private BlockService blockService;
    @MockBean  private BlockRepository blockRepository;
    @Autowired private ObjectMapper objectMapper;

    // ── Concrete projection (Jackson-serializable) ─────────────────────────────
    private BlockProjection projection(int floor, int code) {
        return new BlockProjection() {
            public Integer getBlockFloor() { return floor; }
            public Integer getBlockCode()  { return code;  }
        };
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated blocks")
    void testGetAll() throws Exception {

        Page<BlockProjection> page =
                new PageImpl<>(List.of(projection(1, 1)), PageRequest.of(0, 5), 1);

        when(blockRepository.findAllProjectedBy(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return block by floor and code")
    void testGetById() throws Exception {

        when(blockRepository.findProjectedByIdBlockFloorAndIdBlockCode(1, 1))
                .thenReturn(Optional.of(projection(1, 1)));

        mockMvc.perform(get("/api/blocks/1/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when block not found")
    void testGetByIdNotFound() throws Exception {

        when(blockRepository.findProjectedByIdBlockFloorAndIdBlockCode(9, 9))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/blocks/9/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create block")
    void testCreateBlock() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("blockFloor", 2);
        request.put("blockCode", 3);

        when(blockService.save(any(Block.class)))
                .thenReturn(new Block(new BlockId(2, 3)));
        when(blockRepository.findProjectedByIdBlockFloorAndIdBlockCode(2, 3))
                .thenReturn(Optional.of(projection(2, 3)));

        mockMvc.perform(post("/api/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blockFloor").value(2))
                .andExpect(jsonPath("$.blockCode").value(3));
    }

    @Test
    @DisplayName("Should delete block")
    void testDeleteBlock() throws Exception {

        doNothing().when(blockService).delete(any(BlockId.class));

        mockMvc.perform(delete("/api/blocks/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Block [floor=1, code=1] deleted."));
    }
}
