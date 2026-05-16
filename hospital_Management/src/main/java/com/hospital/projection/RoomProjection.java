package com.hospital.projection;

/** Spring Data projection for Room — exposes all scalar fields (blockFloor and blockCode are already flat). */
public interface RoomProjection {
    Integer getRoomNumber();
    String getRoomType();
    Integer getBlockFloor();
    Integer getBlockCode();
    Boolean getUnavailable();
}
