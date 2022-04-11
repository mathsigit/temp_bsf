package com.island.datainspection.service;

import com.island.datainspection.model.DiConfigAndTables;
import com.island.datainspection.repository.oracle.DiConfigAndTablesDao;
import org.datanucleus.store.rdbms.exceptions.NullValueException;
import org.hibernate.jdbc.TooManyRowsAffectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service(value = "ConfigAndTablesService")
public class ConfigAndTablesService {

    @Autowired
    @Qualifier("DiConfigAndTablesDao")
    private DiConfigAndTablesDao diConfigAndTablesDao;

    public List<DiConfigAndTables> findDiConfigAndTable(String tableName) throws Exception {
        Optional<String> opt = Optional.ofNullable(tableName);
        if(!opt.isPresent()) {
            throw new NullValueException("Table name is null or empty!");
        }

        List<DiConfigAndTables> diConfigAndTables = diConfigAndTablesDao.findDiConfigAndTableByTableName(tableName);
        if(diConfigAndTables.size() == 0) {
            throw new NoSuchElementException("No result of table : " + tableName + ", please confirm the table name!");
        }
        if(!checkSingleTableRecord(diConfigAndTables, tableName))
            throw new TooManyRowsAffectedException("There should be only one row data in table_name: "+ tableName +", please confirm view: tws_job_streams_v.", 1, -99999);

        return diConfigAndTables;
    }

    public List<DiConfigAndTables> findAllDiConfigAndTables() {
        List<DiConfigAndTables> diConfigAndTables = diConfigAndTablesDao.findAllConfigAndTables();
        if(!checkSingleTableRecord(diConfigAndTables))
            throw new TooManyRowsAffectedException("There should be only one row data in each table_name, please confirm view: tws_job_streams_v.", 1, -99999);
        return diConfigAndTables;
    }

    @SafeVarargs
    private static <T> Predicate<T> distinctByKeys(final Function<? super T, ?>... keyExtractors)
    {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    private boolean checkSingleTableRecord(List<DiConfigAndTables> diConfigAndTablesList) {
        Map<String, Long> tableNameCountingMap = diConfigAndTablesList.stream()
                .filter(distinctByKeys(DiConfigAndTables::getTableName, DiConfigAndTables::getStatus))
                .collect(Collectors.groupingBy(DiConfigAndTables::getTableName, Collectors.counting()));
        Map<String, Long> moreThanOneMap = tableNameCountingMap.entrySet().stream()
                .filter(m -> m.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return moreThanOneMap.size() == 0;
    }

    private boolean checkSingleTableRecord(List<DiConfigAndTables> diConfigAndTablesList, String tableName) {
        Map<String, Long> tableNameCountingMap = diConfigAndTablesList.stream()
                .filter(l -> l.getTableName().equals(tableName))
                .filter(distinctByKeys(DiConfigAndTables::getTableName, DiConfigAndTables::getStatus))
                .collect(Collectors.groupingBy(DiConfigAndTables::getTableName, Collectors.counting()));
        Long conutingResult = tableNameCountingMap.get(tableName);
        return conutingResult == 1;
    }
}
