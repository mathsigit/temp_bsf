package com.island.mailalert.service;

import com.island.mailalert.model.DiCurrentAndHistoryResult;
import com.island.mailalert.model.DiCurrentResultAndTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "EntryService")
public class EntryService {

    @Autowired
    @Qualifier("CurrentResultAndTablesService")
    private CurrentResultAndTablesService currentResultAndTablesService;

    @Autowired
    @Qualifier("CurrentAndHistoryResultService")
    private CurrentAndHistoryResultService currentAndHistoryResultService;

    @Autowired
    @Qualifier("MailSenderService")
    private MailSenderService mailSenderService;

    public void executed(String tableName, String tableOwner) throws Exception {
        List<DiCurrentResultAndTables> diCurrentResultAndTablesList;
        List<DiCurrentAndHistoryResult> diCurrentAndHistoryResultList;
        if (tableName.trim().toLowerCase().equals("all") || tableOwner.trim().toLowerCase().equals("all")) {
            diCurrentResultAndTablesList = currentResultAndTablesService.findALL();
            diCurrentAndHistoryResultList = currentAndHistoryResultService.findALL();
        }
        else {
            diCurrentResultAndTablesList = currentResultAndTablesService.findByName(tableName, tableOwner);
            diCurrentAndHistoryResultList = currentAndHistoryResultService.findByName(tableName, tableOwner);
        }

        mailSenderService.send(diCurrentResultAndTablesList, diCurrentAndHistoryResultList);
    }
}
