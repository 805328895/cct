package com.cct.jdbc.config;

import lombok.Data;

@Data
public class DatasourceConfig {

    private String            userName;
    private String            passWord;
    private String            url;
    private String driverClassName;
    private Integer maxWait = 100000;
    private Integer minIdle =10;
    private Integer initialSize =20;
    private Integer maxActive = 150;
    private Integer no; //序号

}
