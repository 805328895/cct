package com.cct.mybatis.adv;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.local.CctTransactionModel;
import com.cct.rpc.local.CctTransactionalFactory;
import com.cct.rpc.service.TranactionService;
import lombok.extern.slf4j.Slf4j;
import model.InsertResponse;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
                Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                Object.class, RowBounds.class, ResultHandler.class})})
@SuppressWarnings({"unchecked", "rawtypes"})
@Component
public class CctInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        CctTransactionModel transactionModel = CctTransactionalFactory.getTranactional();

        Object parameter = null;
        // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
//            System.out.println("parameter = " + parameter);
        }
        String sqlId = mappedStatement.getId(); // 获取到节点的id,即sql语句的id
//        System.out.println("sqlId = " + sqlId);
        BoundSql boundSql = mappedStatement.getBoundSql(parameter); // BoundSql就是封装myBatis最终产生的sql类
        Object parameterObject = boundSql.getParameterObject();

        Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
        String sql = getSql(configuration, boundSql, sqlId); // 获取到最终的sql语句
        System.out.println("sql = " + sql);
        // 执行完上面的任务后，不改变原有的sql执行过程

        if(transactionModel !=null) {
            log.info("cct rpc");
            Boolean create = transactionModel.getHasCreate().stream().filter(x->x.intValue() == transactionModel.getNo()).count() ==0;
            TranactionService service = transactionModel.getService();
            //开启分布式事物，拦截数据
            SqlCommandType commandType = mappedStatement.getSqlCommandType();
            if (commandType.name().toUpperCase().equals("INSERT")) {
                InsertResponse response = service.insert(transactionModel.getNo(), transactionModel.getTransactionId(), sql,create);
                if(create) {
                    transactionModel.getHasCreate().add(transactionModel.getNo());
                }
                if (response.getKeyValues() != null && response.getKeyValues().size() == 1) {
                    //回写主键
                    setValue(parameter, response.getKeyValues().get(0).get(0).getName(), response.getKeyValues().get(0).get(0).getValue());
                }
                return response.getCount();
            } else if (commandType.name().toUpperCase().equals("SELECT")) {
                List list = service.select(transactionModel.getNo(), transactionModel.getTransactionId(), sql,create);
                if(create) {
                    transactionModel.getHasCreate().add(transactionModel.getNo());
                }
                //序列化
                List result = new ArrayList();
                for (Object o : list) {
                    Class cc = mappedStatement.getResultMaps().get(0).getType();
                    Object _object = cc.newInstance();
                    String json = JSON.toJSONString(o);
                    _object = JSON.parseObject(json, cc);
                    result.add(_object);
                }
                return result;
            } else if (commandType.name().toUpperCase().equals("UPDATE")) {
                Integer count = service.update(transactionModel.getNo(), transactionModel.getTransactionId(), sql,create);
                if(create) {
                    transactionModel.getHasCreate().add(transactionModel.getNo());
                }
                return count;
            }
        }

        return invocation.proceed();
    }

    // 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        String sql = showSql(configuration, boundSql);
        return sql;
    }

    // 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
    private static String getParameterValue(Object obj) {
        if(obj == null){
            return "NULL";
        }
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                    DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    // 进行？的替换
    public static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }



    public void setValue(Object obj,String key,Object value){
        Class jsonClass = obj.getClass();
        Field dataField = null;
        try {
            //获取object中的
            // data属性
            List<Field> ids = new ArrayList<>();
            for (Field field : jsonClass.getDeclaredFields()){
                TableId tableId = field.getAnnotation(TableId.class);
                if(tableId != null){
                    ids.add(field);
                }
            }
            if(ids.size() !=1){
                return;
            }


            dataField = ids.get(0);
            dataField.setAccessible(true);//设置data属性为可访问的
            try {
                //强转
                String typeName = dataField.getType().getSimpleName();

                if(typeName.toLowerCase().equals("integer")){
                    dataField.set(obj,((BigInteger) value).intValue());
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}