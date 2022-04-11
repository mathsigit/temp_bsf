package com.island.datainspection.service;

import com.island.datainspection.model.*;
import com.island.datainspection.utils.InspectionCalculation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(value = "EntryService")
public class EntryService {

    @Autowired
    @Qualifier("AggregateDAOSerivce")
    private AggregateDAOSerivce dAOSerivceAggregate;

    @Autowired
    @Qualifier("ConfigAndTablesService")
    private ConfigAndTablesService configService;

    @Autowired
    @Qualifier("SaveResultService")
    private SaveResultService saveResultService;

    public void executed(String tableName) throws Exception {
        List<DiConfigAndTables> listDiConfig ;
        List<DiHistoryResult> diHistoryResultList = new ArrayList<>();
        Map<String, DiCurrentResult> diCurrentResultMap = new HashedMap();
        if (tableName.trim().toLowerCase().equals("all"))
            listDiConfig = configService.findAllDiConfigAndTables();
        else
            listDiConfig = configService.findDiConfigAndTable(tableName);

        for(DiConfigAndTables c : listDiConfig) {
            Map<DBType,String> result = dAOSerivceAggregate.execute(
                    c.getTableName()
                    ,c.getCondition()
                    ,c.getConditionColumn()
            );
            DiCurrentResult diCurrentResult = DiCurrentResult.builder()
                    .tableName(c.getTableName())
                    .tableOwner(c.getTableOwner())
                    .jobStreamStatus(c.getStatus())
                    .build();
            diCurrentResultMap.put(c.getTableOwner() + c.getTableName(), diCurrentResult);
            DiHistoryResult diHistoryResult = DiHistoryResult.builder()
                    .tableName(c.getTableName())
                    .tableOwner(c.getTableOwner())
                    .condition(c.getCondition())
                    .conditionColumn(c.getConditionColumn())
                    .hiveResult(result.get(DBType.HIVE))
                    .oracleResult(result.get(DBType.ORACLE))
                    .jobStreamStatus(c.getStatus())
                    .inspection(InspectionCalculation.findInspectionBySingleCondition(c, result))
                    .build();
            diHistoryResultList.add(diHistoryResult);
        }
        //Setting current inspection by all condition result for one single table
        diCurrentResultMap.forEach((t, d)
                -> d.setInspection(
                        InspectionCalculation.findInspectionByConditions(d.getTableName(), diHistoryResultList)
                )
        );
        saveResultService.saveResultToDB(diHistoryResultList, diCurrentResultMap);
    }
}
