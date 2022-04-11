package com.island.datainspection.utils;

import com.island.datainspection.model.DBType;
import com.island.datainspection.model.DiConfigAndTables;
import com.island.datainspection.model.DiHistoryResult;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class InspectionCalculation {

    public static String findInspectionByConditions(String tableName, List<DiHistoryResult> diHistoryResultList) {
        long findNInspectionSize = diHistoryResultList.stream()
                .filter(t -> t.getTableName().equals(tableName))
                .filter(t -> t.getInspection().equals("N")).count();
        if (findNInspectionSize > 0)
            return "N";
        else
            return "Y";
    }

    public static String findInspectionBySingleCondition(DiConfigAndTables diConfigAndTables,
                                                         Map<DBType,String> aggregateResult) {
        if(diConfigAndTables.getCondition().toLowerCase().equals("max"))
            return maxCalculate(aggregateResult);
        else if(diConfigAndTables.getCondition().toLowerCase().equals("count")) {
            if (diConfigAndTables.getThreshold() == null || diConfigAndTables.getThreshold().isEmpty()) {
                throw new NullPointerException("Threshold of table: " + diConfigAndTables.getTableName() +
                        " is null, please confirm the value of Table di_config.");
            }
            return countCalculate(aggregateResult, diConfigAndTables.getThreshold());
        }
        else
            throw new NoSuchElementException("No such condition: " + diConfigAndTables.getCondition());
    }

    private static String countCalculate(Map<DBType,String> aggregateResult, String threshold) {
        double oResult = Double.parseDouble(aggregateResult.get(DBType.ORACLE));
        double hResult = Double.parseDouble(aggregateResult.get(DBType.HIVE));
        double actualThreshold = ( 1 + (hResult - oResult)/oResult) * 100;
        if(actualThreshold > Double.parseDouble(threshold))
            return "Y";
        else
            return  "N";
    }

    private static String maxCalculate(Map<DBType,String> aggregateResult) {
        if(aggregateResult.get(DBType.HIVE).equals(aggregateResult.get(DBType.ORACLE)))
            return "Y";
        else
            return "N";
    }
}
