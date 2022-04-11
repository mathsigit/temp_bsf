package com.island.mailalert.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiCurrentResultAndTables {

    //Columns of DiCurrentResult
    private String tableOwner;
    private String tableName;
    /*
     * Y: Aggregate result between hive and oracle are the same.
     * N: Does not meet expectations.
     */
    @Builder.Default
    private String inspection = "N";
    private String diDatetime;
    private String jobStreamStatus;

    //Columns of DiTables
    @Builder.Default
    private String etlMethod = "";
    @Builder.Default
    private String runCycle = "";
    private String jobStreamName;
    private String ownerEmail;
}
