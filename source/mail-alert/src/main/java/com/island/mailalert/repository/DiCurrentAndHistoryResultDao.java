package com.island.mailalert.repository;

import com.island.mailalert.config.Constant;
import com.island.mailalert.model.DiCurrentAndHistoryResult;
import com.island.mailalert.model.mapper.DiCurrentAndHistoryResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "DiCurrentAndHistoryResultDao")
public class DiCurrentAndHistoryResultDao {

    final StringBuilder FIND_CURRENT_AND_HISTORY_RESULT_SQL_STATEMENT = new StringBuilder()
            .append("select ")
            .append("* ")
            .append("from di_current_result c join di_history_result h ")
            .append("on c.table_name = h.table_name and c.table_owner = h.table_owner ")
            .append("and TO_CHAR(c.di_datetime, 'YYYYMMDD') = h.di_date ")
            .append("and TO_CHAR(c.di_datetime, 'HH24MI') = h.di_time ");

    private String getSqlStatement(Constant.TableType tableType) {
        if(tableType.equals(Constant.TableType.ALL))
            return FIND_CURRENT_AND_HISTORY_RESULT_SQL_STATEMENT.toString();
        else
            return FIND_CURRENT_AND_HISTORY_RESULT_SQL_STATEMENT.append("where c.table_name = ? and c.table_owner = ? ").toString();
    }

    Logger logger = LoggerFactory.getLogger(DiCurrentAndHistoryResultDao.class);

    @Autowired
    @Qualifier("OracleDataSource")
    protected DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Init Oracle JDBC Connection");
    }

    public List<DiCurrentAndHistoryResult> findByTableName(String tableName, String tableOwner) {
        List<Object> paramList  = new ArrayList<>();
        paramList.add(tableName);
        paramList.add(tableOwner);
        logger.info("Beginning query current and history result By Name: "+ tableName + " and Owner: " + tableOwner);
        return this.jdbcTemplate.query(this.getSqlStatement(Constant.TableType.SINGLE)
                , paramList.toArray(), new DiCurrentAndHistoryResultMapper());
    }

    public List<DiCurrentAndHistoryResult> findAll() {
        logger.info("Beginning query all current and history result ");
        return this.jdbcTemplate.query(this.getSqlStatement(Constant.TableType.ALL)
                , new DiCurrentAndHistoryResultMapper());
    }
}
