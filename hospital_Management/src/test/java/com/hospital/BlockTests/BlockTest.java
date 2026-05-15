package com.hospital.BlockTests;


import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import com.hospital.repository.BlockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    // ───────────────── SAVE TEST ─────────────────

    @Test
    @DisplayName("Test Save Block")
    void testSaveBlock() {

        BlockId blockId = new BlockId(1, 101);
        Block block = new Block(blockId);

        Block savedBlock = blockRepository.save(block);

        assertNotNull(savedBlock);
        assertEquals(1, savedBlock.getId().getBlockFloor());
        assertEquals(101, savedBlock.getId().getBlockCode());
    }

    // ───────────────── FIND BY FLOOR ─────────────────

    @Test
    @DisplayName("Test Find By Block Floor Positive")
    void testFindByIdBlockFloorPositive() {

        Block block1 = new Block(new BlockId(2, 201));
        Block block2 = new Block(new BlockId(2, 202));

        blockRepository.save(block1);
        blockRepository.save(block2);

        List<Block> blocks = blockRepository.findByIdBlockFloor(2);

        assertNotNull(blocks);
        assertEquals(2, blocks.size());
    }

    @Test
    @DisplayName("Test Find By Block Floor Negative")
    void testFindByIdBlockFloorNegative() {

        List<Block> blocks = blockRepository.findByIdBlockFloor(99);

        assertTrue(blocks.isEmpty());
    }

    // ───────────────── COUNT BY FLOOR ─────────────────

    @Test
    @DisplayName("Test Count By Block Floor Positive")
    void testCountByIdBlockFloorPositive() {

        Block block1 = new Block(new BlockId(3, 301));
        Block block2 = new Block(new BlockId(3, 302));

        blockRepository.save(block1);
        blockRepository.save(block2);

        long count = blockRepository.countByIdBlockFloor(3);

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Test Count By Block Floor Negative")
    void testCountByIdBlockFloorNegative() {

        long count = blockRepository.countByIdBlockFloor(100);

        assertEquals(0, count);
    }

    // ───────────────── FIND BY CODE ─────────────────

    @Test
    @DisplayName("Test Find By Block Code Positive")
    void testFindByIdBlockCodePositive() {

        Block block = new Block(new BlockId(4, 401));

        blockRepository.save(block);

        List<Block> blocks = blockRepository.findByIdBlockCode(401);

        assertFalse(blocks.isEmpty());
        assertEquals(401, blocks.get(0).getId().getBlockCode());
    }

    @Test
    @DisplayName("Test Find By Block Code Negative")
    void testFindByIdBlockCodeNegative() {

        List<Block> blocks = blockRepository.findByIdBlockCode(999);

        assertTrue(blocks.isEmpty());
    }

    // ───────────────── EXISTS BY CODE ─────────────────

    @Test
    @DisplayName("Test Exists By Block Code Positive")
    void testExistsByIdBlockCodePositive() {

        Block block = new Block(new BlockId(5, 501));

        blockRepository.save(block);

        boolean exists = blockRepository.existsByIdBlockCode(501);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Test Exists By Block Code Negative")
    void testExistsByIdBlockCodeNegative() {

        boolean exists = blockRepository.existsByIdBlockCode(888);

        assertFalse(exists);
    }

    // ───────────────── FIND BY ID ─────────────────

    @Test
    @DisplayName("Test Find By Id Positive")
    void testFindByIdPositive() {

        BlockId blockId = new BlockId(6, 601);
        Block block = new Block(blockId);

        blockRepository.save(block);

        Block foundBlock = blockRepository.findById(blockId).orElse(null);

        assertNotNull(foundBlock);
        assertEquals(6, foundBlock.getId().getBlockFloor());
    }

    @Test
    @DisplayName("Test Find By Id Negative")
    void testFindByIdNegative() {

        BlockId blockId = new BlockId(99, 999);

        Block foundBlock = blockRepository.findById(blockId).orElse(null);

        assertNull(foundBlock);
    }

    // ───────────────── DELETE TEST ─────────────────

    @Test
    @DisplayName("Test Delete Block")
    void testDeleteBlock() {

        BlockId blockId = new BlockId(7, 701);
        Block block = new Block(blockId);

        blockRepository.save(block);

        blockRepository.delete(block);

        boolean exists = blockRepository.existsById(blockId);

        assertFalse(exists);
    }
}