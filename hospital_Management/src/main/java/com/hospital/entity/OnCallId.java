package com.hospital.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Primary Key for the OnCall entity.
 */
@Embeddable
public class OnCallId implements Serializable {

    @Column(name = "Nurse")
    private Integer nurseId;

    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Column(name = "BlockCode")
    private Integer blockCode;

    public OnCallId() {}

    public OnCallId(Integer nurseId, Integer blockFloor, Integer blockCode) {
        this.nurseId = nurseId;
        this.blockFloor = blockFloor;
        this.blockCode = blockCode;
    }

    public Integer getNurseId() { return nurseId; }
    public void setNurseId(Integer nurseId) { this.nurseId = nurseId; }

    public Integer getBlockFloor() { return blockFloor; }
    public void setBlockFloor(Integer blockFloor) { this.blockFloor = blockFloor; }

    public Integer getBlockCode() { return blockCode; }
    public void setBlockCode(Integer blockCode) { this.blockCode = blockCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OnCallId)) return false;
        OnCallId that = (OnCallId) o;
        return Objects.equals(nurseId, that.nurseId) &&
                Objects.equals(blockFloor, that.blockFloor) &&
                Objects.equals(blockCode, that.blockCode);
    }

    @Override
    public int hashCode() { return Objects.hash(nurseId, blockFloor, blockCode); }
}
