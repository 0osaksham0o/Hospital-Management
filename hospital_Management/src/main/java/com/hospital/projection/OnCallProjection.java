package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/** Spring Data projection for OnCall — flattens the embedded composite key. */
public interface OnCallProjection {
    @Value("#{target.id.nurseId}")
    Integer getNurseId();

    @Value("#{target.id.blockFloor}")
    Integer getBlockFloor();

    @Value("#{target.id.blockCode}")
    Integer getBlockCode();

    LocalDateTime getOnCallStart();
    LocalDateTime getOnCallEnd();
}
