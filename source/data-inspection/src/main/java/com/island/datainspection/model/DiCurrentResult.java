package com.island.datainspection.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiCurrentResult {

    private String tableOwner;
    private String tableName;
    /*
    * Y: Aggregate result between hive and oracle are the same.
    * N: Does not meet expectations.
    */
    @Builder.Default
    private String inspection = "N";
    private String diTimestamp;
    private String jobStreamStatus;

}
