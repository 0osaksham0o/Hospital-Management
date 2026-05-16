package com.hospital.service.impl;

import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.BlockRepository;
import com.hospital.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockRepository repo;

    @Override public List<Block> getAll()                                { return repo.findAll(); }
    @Override public Page<Block> getAll(Pageable p)                      { return repo.findAll(p); }
    @Override public Block getById(BlockId id)                           { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Block not found: floor=" + id.getBlockFloor() + ", code=" + id.getBlockCode())); }
    @Override public boolean existsById(BlockId id)                      { return repo.existsById(id); }
    @Override public Block save(Block b)                                 { return repo.save(b); }
    @Override public void delete(BlockId id)                             { getById(id); repo.deleteById(id); }

    @Override public List<Block> getByFloor(Integer floor)               { return repo.findByIdBlockFloor(floor); }
    @Override public long countByFloor(Integer floor)                    { return repo.countByIdBlockFloor(floor); }

    @Override public List<Block> getByCode(Integer code)                 { return repo.findByIdBlockCode(code); }
    @Override public boolean existsByCode(Integer code)                  { return repo.existsByIdBlockCode(code); }

    @Override public List<Integer> getAllDistinctFloors() {
        return repo.findAll().stream()
                .map(b -> b.getId().getBlockFloor())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    @Override public List<Integer> getAllDistinctCodes() {
        return repo.findAll().stream()
                .map(b -> b.getId().getBlockCode())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
