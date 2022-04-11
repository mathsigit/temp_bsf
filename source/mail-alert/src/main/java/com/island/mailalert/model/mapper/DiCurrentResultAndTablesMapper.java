package com.island.mailalert.model.mapper;

import com.island.mailalert.model.DiCurrentResultAndTables;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DiCurrentResultAndTablesMapper implements RowMapper<DiCurrentResultAndTables> {
    @Nullable
    @Override
    public DiCurrentResultAndTables mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DiCurrentResultAndTables.builder()
                .tableName(rs.getString("TABLE_NAME"))
                .tableOwner(rs.getString("TABLE_OWNER"))
                .inspection(rs.getString("INSPECTION"))
                .diDatetime(rs.getString("DI_DATETIME"))
                .jobStreamStatus(rs.getString("JOB_STREAM_STATUS"))
                .etlMethod(rs.getString("ETL_METHOD"))
                .runCycle(rs.getString("RUN_CYCLE"))
                .jobStreamName(rs.getString("JOB_STREAM_NAME"))
                .ownerEmail(rs.getString("OWNER_EMAIL"))
                .build();
    }
}
