package com.island.datainspection.model.mapper;

import com.island.datainspection.model.DiCurrentResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiCurrentResultMapper implements RowMapper<DiCurrentResult> {
    @Nullable
    @Override
    public DiCurrentResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DiCurrentResult.builder()
                .tableName(rs.getString("TABLE_NAME"))
                .tableOwner(rs.getString("TABLE_OWNER"))
                .inspection(rs.getString("INSPECTION"))
                .diTimestamp(rs.getString("DI_TIMESTAMP"))
                .jobStreamStatus(rs.getString("JOB_STREAM_STATUS"))
                .build();
    }
}
