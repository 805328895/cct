package com.cct.jdbc.config;


import java.util.Arrays;
import java.util.List;

public enum DbType {

    /**
     * mysql DB
     */
    MYSQL("com.mysql.jdbc.Driver,com.mysql.cj.jdbc.Driver"),
    /**
     * oracle DB
     */
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    /**
     * sqlsersver DB
     */
    SqlServer("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    private String driver;

    DbType(String driver){
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }

    public boolean isMysql() {
        return this.equals(DbType.MYSQL);
    }


    public boolean isOracle() {
        return this.equals(DbType.ORACLE);
    }

    public boolean isSqlServer() {
        return this.equals(DbType.SqlServer);
    }


    public static DbType getDbTypeByDriver(String driver) {
        if (driver == null || "".equalsIgnoreCase(driver)) {
            return null;
        }
        DbType[] dbTypes = values();

        for (DbType dbType : dbTypes) {
            List<String > ss = Arrays.asList(dbType.getDriver().split(",")) ;
            if (ss.contains(driver)) {
                return dbType;
            }
        }
        return null;
    }




}
