package com.cct.rpc.local;

import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.service.TranactionService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    private String host;

    private Integer port;


//    /**
//     * 代理
//     */
//    private CctRpcClientProxy proxy ;
    /**
     * 远程rpc
     */
    private TranactionService service;

//    //是否创建连接
//    private Boolean isCreate = true;

    //已经创建的连接  按 no 来
    List<Integer> hasCreate = new ArrayList<>();

}
