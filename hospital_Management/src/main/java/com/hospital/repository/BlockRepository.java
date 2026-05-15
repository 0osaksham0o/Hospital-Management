package com.hospital.repository;

import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import com.hospital.projection.BlockProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repository for Block entity.
 * POST, PUT, DELETE, PATCH handled by BlockController.
 */
@RepositoryRestResource(path = "blocks")
public interface BlockRepository extends JpaRepository<Block, BlockId> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<BlockProjection> findAllProjectedBy(Pageable pageable);
    java.util.Optional<BlockProjection> findProjectedByIdBlockFloorAndIdBlockCode(Integer blockFloor, Integer blockCode);

    // ── By Floor ─────────────────────────────────────────────────────────────

    List<Block> findByIdBlockFloor(Integer blockFloor);

    long countByIdBlockFloor(Integer blockFloor);

    // ── By Code ───────────────────────────────────────────────────────────────

    List<Block> findByIdBlockCode(Integer blockCode);

    boolean existsByIdBlockCode(Integer blockCode);

}
