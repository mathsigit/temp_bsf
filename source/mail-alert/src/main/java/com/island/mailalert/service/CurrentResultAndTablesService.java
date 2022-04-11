package com.island.mailalert.service;

import com.island.mailalert.model.DiCurrentResultAndTables;
import com.island.mailalert.repository.DiCurrentResultAndTablesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service(value = "CurrentResultAndTablesService")
public class CurrentResultAndTablesService {

    @Autowired
    @Qualifier("DiCurrentResultAndTablesDao")
    private DiCurrentResultAndTablesDao diCurrentResultAndTablesDao;

    public List<DiCurrentResultAndTables> findByName(String tableName) throws Exception {
        Optional<String> opt = Optional.ofNullable(tableName);
        if(!opt.isPresent()) {
            throw new Exception("Table name is null or empty!");
        }
        List<DiCurrentResultAndTables> diCurrentResultAndTablesList =
                diCurrentResultAndTablesDao.findByTableName(tableName);
        if( diCurrentResultAndTablesList.size() == 0 ) {
            throw new NoSuchElementException("No result of table : " + tableName + ", please confirm the table name!");
        }

        return diCurrentResultAndTablesList;
    }

    public List<DiCurrentResultAndTables> findALL() {
        return diCurrentResultAndTablesDao.findAll();
    }
}
