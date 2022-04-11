package com.island.datainspection.model.mapper;

import com.island.datainspection.model.DiConfigAndTables;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiConfigAndTablesMapper implements RowMapper<DiConfigAndTables> {

    @Nullable
    @Override
    public DiConfigAndTables mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DiConfigAndTables.builder()
                .tableName(rs.getString("TABLE_NAME"))
                .tableOwner(rs.getString("TABLE_OWNER"))
                .condition(rs.getString("CONDITION"))
                .conditionColumn(rs.getString("CONDITION_COLUMN"))
                .threshold(rs.getString("THRESHOLD"))
                .etlMethod(rs.getString("ETL_METHOD"))
                .runCycle(rs.getString("RUN_CYCLE"))
                .jobStreamName(rs.getString("JOB_STREAM_NAME"))
                .ownerEmail(rs.getString("OWNER_EMAIL"))
                .status(rs.getString("STATUS"))
                .build();
    }
}
