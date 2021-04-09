package com.cct.server.service;

import com.cct.jdbc.exec.DbExecFactory;
import com.cct.rpc.server.CctService;
import com.cct.rpc.service.TranactionService;
import model.InsertResponse;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@CctService(TranactionService.class)
@Service
public class TranactionServiceImpl implements TranactionService {
    @Resource
    private DbExecFactory dbExecFactory;
    @Override
    public List select(Integer no, String transactionId, String sql) throws Exception {
        return dbExecFactory.select(no,transactionId,sql);
    }

    @Override
    public Integer update(Integer no, String transactionId, String sql) throws Exception {
        return dbExecFactory.update(no,transactionId,sql);

    }

    @Override
    public InsertResponse insert(Integer no, String transactionId, String sql) throws Exception {
        return dbExecFactory.insert(no,transactionId,sql);
    }

    @Override
    public void commit(String id) {
        dbExecFactory.commit(id);
    }

    @Override
    public void rollback(String id) {
        dbExecFactory.rollback(id);
    }
}
