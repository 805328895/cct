package com.cct.rpc.model;

import lombok.Data;

@Data
public class Request {
    private String methodName;
    private String className;
    private String paramTypes;
    private String params;
}
