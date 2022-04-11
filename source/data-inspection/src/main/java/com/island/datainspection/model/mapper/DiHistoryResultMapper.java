package com.island.datainspection.model.mapper;

import com.island.datainspection.model.DiHistoryResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiHistoryResultMapper implements RowMapper<DiHistoryResult> {
    @Nullable
    @Override
    public DiHistoryResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DiHistoryResult.builder()
                .tableName(rs.getString("TABLE_NAME"))
                .tableOwner(rs.getString("TABLE_OWNER"))
                .condition(rs.getString("CONDITION"))
                .conditionColumn(rs.getString("CONDITION_COLUMN"))
                .hiveResult(rs.getString("HIVE_RESULT"))
                .oracleResult(rs.getString("ORACLE_RESULT"))
                .inspection(rs.getString("INSPECTION"))
                .diDate(rs.getString("DI_DATE"))
                .diTime(rs.getString("DI_TIME"))
                .jobStreamStatus(rs.getString("JOB_STREAM_STATUS"))
                .build();
    }
}
