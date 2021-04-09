package com.cct.rpc.server;

import java.io.Serializable;

public class CctRpcResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Throwable error;
    private Object result;
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }


}
