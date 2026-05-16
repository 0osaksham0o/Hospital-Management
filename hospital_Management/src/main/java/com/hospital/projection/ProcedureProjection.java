package com.hospital.projection;

/** Spring Data projection for Procedure — exposes all scalar fields. */
public interface ProcedureProjection {
    Integer getCode();
    String getName();
    Double getCost();
}
