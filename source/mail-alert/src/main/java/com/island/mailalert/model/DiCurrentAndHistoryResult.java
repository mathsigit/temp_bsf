package com.island.mailalert.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiCurrentAndHistoryResult {

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

    //Columns of DiHistoryResult
    private String condition;
    private String conditionColumn;
    private String hiveResult;
    private String oracleResult;
    private String diDate;
    private String diTime;
}
