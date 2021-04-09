package com.cct.rpc.local;


public class CctTransactionalFactory {

    private static ThreadLocal<CctTransactionModel>  local = new ThreadLocal<>();

    public static void setTransactional(CctTransactionModel model){
        if(local.get() == null){
            local.set(model);
        }
    }

    public static CctTransactionModel getTranactional(){
        return local.get();
    }

    public static void remove(){
        local.remove();
    }
}
