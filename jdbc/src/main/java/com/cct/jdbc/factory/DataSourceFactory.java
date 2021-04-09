package com.cct.jdbc.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.cct.jdbc.config.DataSourceProperties;
import com.cct.jdbc.config.DatasourceConfig;
import com.cct.jdbc.config.DbType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DataSourceFactory {

    private Map<Integer, DataSource> dataSources;

    public Map<Integer, DataSource> getDataSources(){
        return dataSources;
    }

    private boolean isInit = false;
    @Resource
    private DataSourceProperties dataSourceProperties;

    public synchronized void init(){
        if(isInit){
            return;
        }
        dataSources = new HashMap<>();
        for (DatasourceConfig config:dataSourceProperties.getConfigs()){
            DataSource dataSource =createDataSource(config);
            dataSources.put(config.getNo(),dataSource);
        }
        isInit =true;
    }

    public DataSource createDataSource(DatasourceConfig config){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUserName());
        dataSource.setPassword(config.getPassWord());
        dataSource.setUseUnfairLock(true);
        dataSource.setNotFullTimeoutRetryCount(2);
        dataSource.setInitialSize(config.getInitialSize());
        dataSource.setMinIdle(config.getMinIdle());
        dataSource.setMaxActive(config.getMaxActive());
        dataSource.setMaxWait(config.getMaxWait());
        dataSource.setDriverClassName(config.getDriverClassName());
        dataSource.setTestWhileIdle(false);

        DbType dbType = DbType.getDbTypeByDriver(config.getDriverClassName());

        if (dbType.isOracle()) {
            dataSource.addConnectionProperty("restrictGetTables", "true");
            dataSource.addConnectionProperty("oracle.jdbc.V8Compatible", "true");
            dataSource.setValidationQuery("select 1 from dual");
//            dataSource.setExceptionSorter("com.alibaba.druid.pool.vendor.OracleExceptionSorter");
        }

        return dataSource;
    }
}
