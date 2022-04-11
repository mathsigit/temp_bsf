package com.island.mailalert.repository;

import com.island.mailalert.config.Constant;
import com.island.mailalert.model.DiCurrentResultAndTables;
import com.island.mailalert.model.mapper.DiCurrentResultAndTablesMapper;
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

@Repository(value = "DiCurrentResultAndTablesDao")
public class DiCurrentResultAndTablesDao {

    final StringBuilder FIND_CURRENTRESULT_AND_TABLE_SQL_STATEMENT = new StringBuilder()
            .append("select ")
            .append("* ")
            .append("from di_current_result c left join di_tables t ")
            .append("on c.table_name = t.table_name and c.table_owner = t.table_owner ");

    private String getSqlStatement(Constant.TableType tableType) {
        if(tableType.equals(Constant.TableType.ALL))
            return FIND_CURRENTRESULT_AND_TABLE_SQL_STATEMENT.toString();
        else
            return FIND_CURRENTRESULT_AND_TABLE_SQL_STATEMENT.append("where c.table_name = ? ").toString();
    }

    Logger logger = LoggerFactory.getLogger(DiCurrentResultAndTablesDao.class);

    @Autowired
    @Qualifier("OracleDataSource")
    protected DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Init Oracle JDBC Connection");
    }

    public List<DiCurrentResultAndTables> findByTableName(String tableName) {
        List<Object> paramList  = new ArrayList<>();
        paramList.add(tableName);
        logger.info("Beginning query current result and table By Name: "+ tableName);
        return this.jdbcTemplate.query(this.getSqlStatement(Constant.TableType.SINGLE)
                , paramList.toArray(), new DiCurrentResultAndTablesMapper());
    }

    public List<DiCurrentResultAndTables> findAll() {
        logger.info("Beginning query all current result and table ");
        return this.jdbcTemplate.query(this.getSqlStatement(Constant.TableType.ALL)
                , new DiCurrentResultAndTablesMapper());
    }
}
