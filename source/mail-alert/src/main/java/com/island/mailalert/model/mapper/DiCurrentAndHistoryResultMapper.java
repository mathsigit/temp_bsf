package com.island.mailalert.model.mapper;

import com.island.mailalert.model.DiCurrentAndHistoryResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiCurrentAndHistoryResultMapper implements RowMapper<DiCurrentAndHistoryResult> {
    @Nullable
    @Override
    public DiCurrentAndHistoryResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DiCurrentAndHistoryResult.builder()
                .tableName(rs.getString("TABLE_NAME"))
                .tableOwner(rs.getString("TABLE_OWNER"))
                .inspection(rs.getString("INSPECTION"))
                .diDatetime(rs.getString("DI_DATETIME"))
                .jobStreamStatus(rs.getString("JOB_STREAM_STATUS"))
                .condition(rs.getString("CONDITION"))
                .conditionColumn(rs.getString("CONDITION_COLUMN"))
                .hiveResult(rs.getString("HIVE_RESULT"))
                .oracleResult(rs.getString("ORACLE_RESULT"))
                .diDate(rs.getString("DI_DATE"))
                .diTime(rs.getString("DI_TIME"))
                .build();
    }
}
