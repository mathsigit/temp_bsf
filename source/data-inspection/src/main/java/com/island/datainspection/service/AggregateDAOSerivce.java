package com.island.datainspection.service;

import com.island.datainspection.repository.hive.HiveAggregateDao;
import com.island.datainspection.repository.oracle.OracleAggregateDao;
import com.island.datainspection.model.DBType;
import org.datanucleus.store.rdbms.exceptions.NullValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "AggregateDAOSerivce")
public class AggregateDAOSerivce {
    final String SELECT_AGGREGATE_STATEMENT = " select @agg from @table ";
    Logger logger = LoggerFactory.getLogger(AggregateDAOSerivce.class);

    @Autowired
    @Qualifier("HiveDBDao")
    private HiveAggregateDao hiveDBDao;
    @Autowired
    @Qualifier("OracleDBDao")
    private OracleAggregateDao oracleDBDao;

    public Map<DBType,String> execute(String tableName, String condition, String condition_column) throws Exception {
        if(condition_column.trim().isEmpty() || condition_column == null) {
            throw new NullValueException("Condition Column is null or empty value!");
        }
        Map<DBType, String> resultMap = new HashMap<DBType, String>();

        String aggColumnStatment = condition +
                "(" +
                condition_column +
                ")";
        String sqlStatment = SELECT_AGGREGATE_STATEMENT
                .replace("@agg", aggColumnStatment)
                .replace("@table", tableName);
        try{
            List<Map<String, Object>> oracleAGGResultSet = oracleDBDao.query(sqlStatment);
            List<Map<String, Object>> hiveAGGResultSet = hiveDBDao.query(sqlStatment);
            logger.info("Beginning Oracle aggregate : "+ sqlStatment);
            String oracleAGGResult = oracleAGGResultSet.get(0).entrySet().stream().findFirst().get().getValue().toString();
            logger.info("Oracle aggregate "+ aggColumnStatment +" finished! And result : "+ oracleAGGResult);
            logger.info("Beginning Hive aggregate : "+ sqlStatment);
            String hiveAGGResult = hiveAGGResultSet.get(0).entrySet().stream().findFirst().get().getValue().toString();
            logger.info("Hive aggregate "+ aggColumnStatment +" finished: "+ hiveAGGResult);
            resultMap.put(DBType.HIVE, hiveAGGResult);
            resultMap.put(DBType.ORACLE, oracleAGGResult);
        }catch(NullPointerException ex){
            throw new NullPointerException("Return null when aggregating from Oracle/Hive with sql statement: " + sqlStatment +
                    " ,checking the result of the sql statement.");
        }catch(Exception ex){
            throw new Exception(ex);
        }
        return resultMap;
    }
}
