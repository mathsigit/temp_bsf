package com.island.datainspection.repository.oracle;

import com.island.datainspection.model.DiCurrentResult;
import com.island.datainspection.model.DiHistoryResult;
import com.island.datainspection.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository(value = "UpdateOrInsertResultDao")
public class UpdateOrInsertResultDao {

    final String UPDATE_OR_INSERT_CURRENT_RESULT_SQLSTATEMENT = new StringBuilder()
            .append("Merge into DI_CURRENT_RESULT A ")
            .append("using ( select * from  di_tables ")
            .append("where table_name = ? and table_owner = ? ) B ")
            .append("On (A.table_name = B.table_name and A.table_owner = B.table_owner ) ")
            .append("WHEN MATCHED THEN UPDATE SET ")
            .append("A.INSPECTION = ? , ")
            .append("A.DI_DATETIME = TO_TIMESTAMP ( ? , 'RR-mm-DD HH24.MI.SS.FF'), ")
            .append("A.JOB_STREAM_STATUS = ? ")
            .append("WHEN NOT MATCHED THEN ")
            .append("INSERT (table_owner,table_name,inspection,di_datetime,job_stream_status) ")
            .append("VALUES (B.table_owner, B.table_name, ? , TO_TIMESTAMP ( ? , 'RR-mm-DD HH24.MI.SS.FF'), ? ) ")
            .toString();

    final String INSERT_HISTORY_RESULT_STATEMENT = new StringBuilder()
            .append("INSERT INTO DI_HISTORY_RESULT ")
            .append("(TABLE_OWNER, TABLE_NAME, CONDITION, CONDITION_COLUMN, HIVE_RESULT, ORACLE_RESULT, INSPECTION, DI_DATE, DI_TIME, JOB_STREAM_STATUS) ")
            .append("VALUES(? , ? , ? , ? , ? , ? , ? , TO_CHAR(TO_TIMESTAMP ( ? , 'RR-mm-DD HH24.MI.SS.FF'),'YYYYMMDD') , TO_CHAR(TO_TIMESTAMP ( ? , 'RR-mm-DD HH24.MI.SS.FF'),'HH24MI') , ?)")
            .toString();

    Logger logger = LoggerFactory.getLogger(UpdateOrInsertResultDao.class);

    @Autowired
    @Qualifier("OracleDataSource")
    protected DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Init Oracle JDBC Connection");
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrInsertResult(
            List<DiHistoryResult> diHistoryResultList,
            Map<String, DiCurrentResult> diCurrentResultList) {
        String currentDatetime = TimeUtils.getCurrentDatetime();
        //Update DI_CURRENT_RESULT
        for(DiCurrentResult cr: diCurrentResultList.values()) {
            jdbcTemplate.update(UPDATE_OR_INSERT_CURRENT_RESULT_SQLSTATEMENT, ps -> {
                ps.setString(1, cr.getTableName());
                ps.setString(2, cr.getTableOwner());
                ps.setString(3, cr.getInspection());
                ps.setString(4, currentDatetime);
                ps.setString(5, cr.getJobStreamStatus());
                ps.setString(6, cr.getInspection());
                ps.setString(7, currentDatetime);
                ps.setString(8, cr.getJobStreamStatus());
            });
        }
        //Update DI_HISTORY_RESULT
        for(DiHistoryResult hr: diHistoryResultList) {
            List<Object> list = new ArrayList<>();
            list.add(hr.getTableOwner());
            list.add(hr.getTableName());
            list.add(hr.getCondition());
            list.add(hr.getConditionColumn());
            list.add(hr.getHiveResult());
            list.add(hr.getOracleResult());
            list.add(hr.getInspection());
            list.add(currentDatetime);
            list.add(currentDatetime);
            list.add(hr.getJobStreamStatus());
            jdbcTemplate.update(INSERT_HISTORY_RESULT_STATEMENT, list.toArray());
        }
    }
}
