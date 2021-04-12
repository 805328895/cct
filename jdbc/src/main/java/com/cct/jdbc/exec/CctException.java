package com.cct.jdbc.exec;

import lombok.Data;

@Data
public class CctException extends RuntimeException {
    private static final long serialVersionUID = 5540484171361000892L;
    Integer code;
    private String message;
    private Exception exception;

    public CctException(Integer code,String message,Exception ex ){
        super(message,ex);
        this.code = code;
        this.message = message;
        this.exception =ex;
    }

}
