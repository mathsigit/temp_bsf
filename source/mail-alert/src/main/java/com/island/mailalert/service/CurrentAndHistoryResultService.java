package com.island.mailalert.service;

import com.island.mailalert.model.DiCurrentAndHistoryResult;
import com.island.mailalert.repository.DiCurrentAndHistoryResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service(value = "CurrentAndHistoryResultService")
public class CurrentAndHistoryResultService {

    @Autowired
    @Qualifier("DiCurrentAndHistoryResultDao")
    private DiCurrentAndHistoryResultDao diCurrentAndHistoryResultDao;

    public List<DiCurrentAndHistoryResult> findByName(String tableName) throws Exception {
        Optional<String> opt = Optional.ofNullable(tableName);
        if(!opt.isPresent()) {
            throw new Exception("Table name is null or empty!");
        }
        List<DiCurrentAndHistoryResult> diCurrentAndHistoryResultList =
                diCurrentAndHistoryResultDao.findByTableName(tableName);
        if( diCurrentAndHistoryResultList.size() == 0 ) {
            throw new NoSuchElementException("No result of table : " + tableName + ", please confirm the table name!");
        }

        return diCurrentAndHistoryResultList;
    }

    public List<DiCurrentAndHistoryResult> findALL() {
        return diCurrentAndHistoryResultDao.findAll();
    }
}
