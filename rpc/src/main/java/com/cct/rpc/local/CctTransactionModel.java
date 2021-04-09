package com.cct.rpc.local;

import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.service.TranactionService;
import lombok.Data;

@Data
public class CctTransactionModel {
    /**
     * 序号
     */
    private Integer no;

    /**
     * 事物id
     */
    private String transactionId;

    /**
     * 事物级别
     */
    private Integer level;


//    /**
//     * 代理
//     */
//    private CctRpcClientProxy proxy ;
    /**
     * 远程rpc
     */
    private TranactionService service;
}
