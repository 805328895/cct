package com.cct.jdbc.exec;

import com.cct.jdbc.config.TransactionConfig;
import com.cct.jdbc.factory.DataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import model.CctKeyValue;
import model.InsertResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Configuration
public class DbExecFactory {

    private ConcurrentHashMap<String, List<TransactionConfig>> connectionHashMap = new ConcurrentHashMap();

    @Resource
    private DataSourceFactory dataSourceFactory;

    public List select(Integer no,String transactionId, String sql) throws Exception{
        PreparedStatement preparedStatement= initPrepare(no,transactionId,sql,null);
        log.info("select id:"+transactionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List list = new ArrayList();//new一个新的List
        ResultSetMetaData md =  resultSet.getMetaData();//将传进来的结果集用getMetaData方法用于获取数据集的数据（如列数）
        int columnCount = md.getColumnCount();//得到数据集的列数
        while (resultSet.next()) {//数据集不为空
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    public Integer update(Integer no,String transactionId, String sql) throws Exception{
        PreparedStatement preparedStatement= initPrepare(no,transactionId,sql,null);
        log.info("update id:"+transactionId);
        Integer count = preparedStatement.executeUpdate();
        return count;
    }

    public InsertResponse insert(Integer no, String transactionId, String sql) throws Exception{
        PreparedStatement preparedStatement= initPrepare(no,transactionId,sql, Statement.RETURN_GENERATED_KEYS);
        log.info("insert id:"+transactionId);
        Integer count = preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();//得到数据集的列数
        InsertResponse insertResponse = new InsertResponse();
        if (rs.next()) {
            Object id = rs.getObject(1);
//            log.info("数据主键：" + id);
            List<CctKeyValue> list = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                CctKeyValue cctKeyValue = new CctKeyValue();
                cctKeyValue.setName(md.getColumnName(i));
                cctKeyValue.setValue(rs.getObject(i));
                list.add(cctKeyValue);
            }
            insertResponse.getKeyValues().add(list);
        }
        insertResponse.setCount(count);
        return insertResponse;
    }

    public PreparedStatement initPrepare(Integer no,String transactionId,String sql,Integer statement) throws Exception{
        Map<Integer, DataSource> dataSourceMap= dataSourceFactory.getDataSources();
        if(!dataSourceMap.containsKey(no)){
            return null;
        }
        DataSource dataSource = dataSourceMap.get(no);
        Connection connection= getConnetcion(no,transactionId,dataSource);
        PreparedStatement preparedStatement=null;
        if(statement == null) {
            preparedStatement =connection.prepareStatement(sql);
        }else {
            preparedStatement = connection.prepareStatement(sql,statement);
        }
        return preparedStatement;
    }

    /**
     * 获取事物连接
     * @param no
     * @param transactionId
     * @param dataSource
     * @return
     */
    private synchronized Connection getConnetcion(Integer no, String transactionId,DataSource dataSource) {
        Connection connection =null;
        List<TransactionConfig> configs = connectionHashMap.get(transactionId);
        if (configs == null) {
            TransactionConfig config = createConnection(no, dataSource);
            log.info("create id:"+transactionId);
            connectionHashMap.put(transactionId, Arrays.asList(config));
            connection = config.getConnection();
        } else {
            Optional<TransactionConfig> collect = configs.stream().filter(x -> x.getNo().intValue() == no).findFirst();
            if (!collect.isPresent()) {
                //不存在
                TransactionConfig config = createConnection(no, dataSource);
                log.info("create id:"+transactionId);
                configs.add(config);
                connection = config.getConnection();
            } else {
                connection = collect.get().getConnection();
            }
        }
        return connection;
    }

    private TransactionConfig createConnection(Integer no, DataSource dataSource){
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            TransactionConfig config = new TransactionConfig();
            config.setNo(no);
            config.setConnection(connection);
            return config;
        }catch (Exception e){
            log.error("create connection Error",e);
            throw  new CctException(-101,"create connection error",e);
        }
    }


    /**
     * 提交事物
     * @param transactionId
     */
    public void commit(String transactionId){
        log.info("commit id:"+transactionId);
        List<TransactionConfig> truncationConfigs = connectionHashMap.get(transactionId);
        try {
            for (TransactionConfig config : truncationConfigs) {
                try {
                    config.getConnection().commit();
                    config.getConnection().close();
                } catch (Exception e) {
                    //数据库记录的回滚
                    log.error("commit send error",e);
                    throw  new CctException(-102,"commit data error",e);
                }
            }
        } catch (Exception e){
            log.info("commit error",e);
            throw  new CctException(-102,"commit data error",e);
        }finally {
            if(connectionHashMap.containsKey(transactionId)) {
                connectionHashMap.remove(transactionId);
            }
        }
    }

    public  void rollback(String transactionId) {
        log.info("rollback id:"+transactionId);
        List<TransactionConfig> transactionConfigs = connectionHashMap.get(transactionId);
        try {
            if(transactionConfigs == null){
                return;
            }
            for (TransactionConfig config : transactionConfigs) {
                try {
                    if(config.getConnection() !=null){
                        config.getConnection().rollback();
                        config.getConnection().close();
                    }
                } catch (Exception e) {
                    //数据库记录的回滚
                    log.error("roll back send error",e);
                    throw e;
                }
            }
        } catch (Exception e){
            log.error("roll back error",e);
            throw  new CctException(-103,"roll back data error",e);
        }finally {
            if(connectionHashMap.containsKey(transactionId)) {
                connectionHashMap.remove(transactionId);
            }
        }
    }
}
