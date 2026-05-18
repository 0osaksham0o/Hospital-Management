package com.hospital.BlockTests;

import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.BlockRepository;
import com.hospital.service.impl.BlockServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {

    @Mock
    private BlockRepository repo;

    @InjectMocks
    private BlockServiceImpl service;

    private Block block;
    private BlockId blockId;

    @BeforeEach
    void setUp() {
        blockId = new BlockId();
        blockId.setBlockFloor(1);
        blockId.setBlockCode(101);

        block = new Block(blockId);
    }

    // ✅ getAll
    @Test
    void testGetAllBlocks() {
        when(repo.findAll()).thenReturn(List.of(block));

        List<Block> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    // ✅ getAll Pageable
    @Test
    void testGetAllBlocksPaged() {
        Page<Block> page = new PageImpl<>(List.of(block));

        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Block> result =
                service.getAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    // ✅ getById success
    @Test
    void testGetByIdSuccess() {
        when(repo.findById(blockId))
                .thenReturn(Optional.of(block));

        Block result = service.getById(blockId);

        assertNotNull(result);
    }

    // ✅ getById fail
    @Test
    void testGetByIdNotFound() {
        when(repo.findById(blockId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(blockId));
    }

    // ✅ save
    @Test
    void testSaveBlock() {
        when(repo.save(block)).thenReturn(block);

        Block saved = service.save(block);

        assertEquals(block, saved);
        verify(repo).save(block);
    }

    // ✅ delete
    @Test
    void testDeleteBlock() {
        when(repo.findById(blockId))
                .thenReturn(Optional.of(block));

        service.delete(blockId);

        verify(repo).deleteById(blockId);
    }

    // ✅ getByFloor
    @Test
    void testGetByFloor() {
        when(repo.findByIdBlockFloor(1))
                .thenReturn(List.of(block));

        List<Block> result = service.getByFloor(1);

        assertEquals(1, result.size());
    }

    // ✅ countByFloor
    @Test
    void testCountByFloor() {
        when(repo.countByIdBlockFloor(1))
                .thenReturn(3L);

        long count = service.countByFloor(1);

        assertEquals(3L, count);
    }

    // ✅ existsByCode
    @Test
    void testExistsByCode() {
        when(repo.existsByIdBlockCode(101))
                .thenReturn(true);

        boolean exists = service.existsByCode(101);

        assertTrue(exists);
    }

    // ✅ distinct floors
    @Test
    void testGetAllDistinctFloors() {

        BlockId id2 = new BlockId();
        id2.setBlockFloor(2);
        id2.setBlockCode(102);

        Block block2 = new Block(id2);

        when(repo.findAll()).thenReturn(List.of(block, block2));

        List<Integer> floors = service.getAllDistinctFloors();

        assertEquals(2, floors.size());
    }

    // ✅ distinct codes
    @Test
    void testGetAllDistinctCodes() {

        when(repo.findAll()).thenReturn(List.of(block));

        List<Integer> codes = service.getAllDistinctCodes();

        assertEquals(1, codes.size());
    }
}