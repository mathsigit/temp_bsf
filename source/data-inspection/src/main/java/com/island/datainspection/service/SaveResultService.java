package com.island.datainspection.service;

import com.island.datainspection.model.DiCurrentResult;
import com.island.datainspection.model.DiHistoryResult;
import com.island.datainspection.repository.oracle.UpdateOrInsertResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "SaveResultService")
public class SaveResultService {

    @Autowired
    @Qualifier("UpdateOrInsertResultDao")
    private UpdateOrInsertResultDao updateOrInsertResultDao;

    public void saveResultToDB(List<DiHistoryResult> diHistoryResultList,
                               Map<String, DiCurrentResult> diCurrentResultList) {
        updateOrInsertResultDao.updateOrInsertResult(diHistoryResultList, diCurrentResultList);
    }
}
