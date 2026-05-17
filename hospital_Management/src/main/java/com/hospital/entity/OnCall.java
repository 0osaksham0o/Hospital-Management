package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a nurse's on-call schedule for a hospital block.
 * Maps to the 'on_call' table.
 */
@Entity
@Table(name = "On_Call")
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Nurse", referencedColumnName = "EmployeeID", insertable = false, updatable = false)
    private Nurse nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor", insertable = false, updatable = false),
            @JoinColumn(name = "BlockCode",  referencedColumnName = "BlockCode",  insertable = false, updatable = false)
    })
    private Block block;

    @Column(name = "OnCallStart", nullable = false)
    private LocalDateTime onCallStart;

    @Column(name = "OnCallEnd", nullable = false)
    private LocalDateTime onCallEnd;

    public OnCall() {}

    public OnCall(OnCallId id, Nurse nurse, Block block, LocalDateTime onCallStart, LocalDateTime onCallEnd) {
        this.id = id;
        this.nurse = nurse;
        this.block = block;
        this.onCallStart = onCallStart;
        this.onCallEnd = onCallEnd;
    }

    public OnCallId getId() { return id; }
    public void setId(OnCallId id) { this.id = id; }

    public Nurse getNurse() { return nurse; }
    public void setNurse(Nurse nurse) { this.nurse = nurse; }

    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }

    public LocalDateTime getOnCallStart() { return onCallStart; }
    public void setOnCallStart(LocalDateTime onCallStart) { this.onCallStart = onCallStart; }

    public LocalDateTime getOnCallEnd() { return onCallEnd; }
    public void setOnCallEnd(LocalDateTime onCallEnd) { this.onCallEnd = onCallEnd; }

    @Override
    public String toString() {
        return "OnCall{id=" + id + "}";
    }
}
