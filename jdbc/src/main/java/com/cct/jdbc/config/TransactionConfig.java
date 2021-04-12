package com.cct.jdbc.config;

import lombok.Data;

import java.sql.Connection;

@Data
public class TransactionConfig {
    private Integer no;
    private Connection connection;
}
