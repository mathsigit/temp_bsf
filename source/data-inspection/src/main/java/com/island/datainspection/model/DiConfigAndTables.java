package com.island.datainspection.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiConfigAndTables {

    //Columns of DiConfig
    private String tableOwner;
    private String tableName;
    private String condition;
    private String conditionColumn;
    private String threshold;

    //Columns of DiTables
    private String etlMethod;
    private String runCycle;
    private String jobStreamName;
    private String ownerEmail;

    //Column of TWS_JOB_STREAMS_V
    private String status;
}
