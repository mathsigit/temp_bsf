package com.island.datainspection.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiHistoryResult {

    private String tableOwner;
    private String tableName;
    private String condition;
    private String conditionColumn;
    private String hiveResult;
    private String oracleResult;
    /*
     * Y: Aggregate result between hive and oracle are the same.
     * N: Does not meet expectations.
     */
    @Builder.Default
    private String inspection = "N";
    private String diDate;
    private String diTime;
    private String jobStreamStatus;
}
