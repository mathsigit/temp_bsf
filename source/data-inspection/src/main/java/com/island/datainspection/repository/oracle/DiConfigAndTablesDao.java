package com.island.datainspection.repository.oracle;

import com.island.datainspection.model.DiConfigAndTables;
import com.island.datainspection.model.mapper.DiConfigAndTablesMapper;
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

@Repository(value = "DiConfigAndTablesDao")
public class DiConfigAndTablesDao {

    enum TableType {
        SINGLE,
        ALL
    }

    final StringBuilder FIND_DICONFIG_AND_TABLE_SQL_STATEMENT = new StringBuilder()
            .append("select ")
            .append("t.table_owner, t.table_name, t.job_stream_name, t.owner_email, ")
            .append("t.etl_method, t.run_cycle, c.condition, c.condition_column, c.threshold, tws.status ")
            .append("from di_config c ")
            .append("left join di_tables t on c.table_owner = t.table_owner and c.table_name = t.table_name ")
            .append("left join tws_job_streams_v tws on t.job_stream_name = tws.job_stream_name ");

    private String getSqlStatement(TableType tableType) {
        if(tableType.equals(TableType.ALL))
            return FIND_DICONFIG_AND_TABLE_SQL_STATEMENT.toString();
        else
            return FIND_DICONFIG_AND_TABLE_SQL_STATEMENT.append("where c.table_name = ? and c.table_owner = ? ").toString();
    }

    Logger logger = LoggerFactory.getLogger(DiConfigAndTablesDao.class);

    @Autowired
    @Qualifier("OracleDataSource")
    protected DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Init Oracle JDBC Connection");
    }

    public List<DiConfigAndTables> findDiConfigAndTableByTableName(String tableName, String tableOwner) {
        List<Object> paramList  = new ArrayList<>();
        paramList.add(tableName);
        paramList.add(tableOwner);
        logger.info("Beginning query DiConfig And DiTable By Name: {} , and Owner : {}", tableName, tableOwner);
        return this.jdbcTemplate.query(this.getSqlStatement(TableType.SINGLE)
                , paramList.toArray(), new DiConfigAndTablesMapper());
    }

    public List<DiConfigAndTables> findAllConfigAndTables() {
        logger.info("Beginning query all DiConfig And DiTable ");
        return this.jdbcTemplate.query(this.getSqlStatement(TableType.ALL)
                , new DiConfigAndTablesMapper());
    }

}
