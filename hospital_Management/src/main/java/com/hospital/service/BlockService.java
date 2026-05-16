package com.hospital.service;

import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlockService {
    List<Block> getAll();
    Page<Block> getAll(Pageable pageable);
    Block getById(BlockId id);
    boolean existsById(BlockId id);
    Block save(Block block);
    void delete(BlockId id);

    // By Floor
    List<Block> getByFloor(Integer blockFloor);
    long countByFloor(Integer blockFloor);

    // By Code
    List<Block> getByCode(Integer blockCode);
    boolean existsByCode(Integer blockCode);

    // Distinct
    List<Integer> getAllDistinctFloors();
    List<Integer> getAllDistinctCodes();
}
