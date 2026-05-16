package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

/** Spring Data projection for Block — flattens the embedded BlockId. */
public interface BlockProjection {
    @Value("#{target.id.blockFloor}")
    Integer getBlockFloor();

    @Value("#{target.id.blockCode}")
    Integer getBlockCode();
}
