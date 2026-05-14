package com.hospital.repository;

import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repository for Block entity.
 * POST, PUT, DELETE, PATCH handled by BlockController.
 */
@RepositoryRestResource(path = "blocks")
public interface BlockRepository extends JpaRepository<Block, BlockId> {

    // ── By Floor ─────────────────────────────────────────────────────────────

    /**
     * Find all blocks on a given floor.
     */
    List<Block> findByIdBlockFloor(Integer blockFloor);

    /**
     * Count blocks on a given floor.
     */
    long countByIdBlockFloor(Integer blockFloor);

    // ── By Code ───────────────────────────────────────────────────────────────

    /**
     * Find all blocks with a given block code.
     */
    List<Block> findByIdBlockCode(Integer blockCode);

    /**
     * Check if a block with a specific code exists on any floor.
     */
    boolean existsByIdBlockCode(Integer blockCode);

}