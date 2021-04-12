package com.cct.rpc.service;


import model.InsertResponse;

import java.util.List;

public interface TranactionService {
    List select(Integer no, String transactionId, String sql ,Boolean isCreate) throws Exception;
    Integer update(Integer no, String transactionId, String sql,Boolean isCreate) throws Exception;
    InsertResponse insert(Integer no, String transactionId, String sql,Boolean isCreate) throws Exception;
    void commit(String id);
    void rollback(String id);
}
