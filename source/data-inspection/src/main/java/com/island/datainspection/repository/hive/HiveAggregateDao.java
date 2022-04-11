package com.island.datainspection.repository.hive;

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

@Repository(value = "HiveDBDao")
public class HiveAggregateDao {

    Logger logger = LoggerFactory.getLogger(HiveAggregateDao.class);

    @Autowired
    @Qualifier("HiveDataSource")
    protected DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Init Hive JDBC Connection");
    }

    public List<Map<String, Object>> query(String sql){
        return jdbcTemplate.queryForList(sql);
    }
}
