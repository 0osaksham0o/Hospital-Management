package com.hospital.entity;

import jakarta.persistence.*;

/**
 * Entity representing a Block (physical wing/unit) in the hospital.
 * Maps to the 'block' table.
 */
@Entity
@Table(name = "block")
public class Block {

    @EmbeddedId
    private BlockId id;

    public Block() {}

    public Block(BlockId id) {
        this.id = id;
    }

    public BlockId getId() { return id; }
    public void setId(BlockId id) { this.id = id; }

    @Override
    public String toString() {
        return "Block{id=" + id + "}";
    }
}
