package com.island.datainspection.repository.oracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository(value = "OracleDBDao")
public class OracleAggregateDao {
    Logger logger = LoggerFactory.getLogger(OracleAggregateDao.class);

    @Autowired
    @Qualifier("OracleDataSource")
    protected DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Init Oracle JDBC Connection");
    }

    public List<Map<String, Object>> query(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
