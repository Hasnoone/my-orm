package org.example.component;


import lombok.Data;
import org.example.pojo.MapperStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Data
public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;


    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectAll(String statementId, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        Executor executor = new SimpleExecutor();
        MapperStatement statement = configuration.getMapperStatementMap().get(statementId);
        Connection connection = configuration.getComboPooledDataSource().getConnection();
        return executor.executeQuery(connection, statement,params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
//        Executor executor = new SimpleExecutor();
//        MapperStatement statement = configuration.getMapperStatementMap().get(statementId);
//        Connection connection = configuration.getComboPooledDataSource().getConnection();
        List<Object> objects = selectAll(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        }
        else {
            throw new RuntimeException("查询结果为空或者返回结果大于一条");
        }
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(),new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                String namespace = method.getDeclaringClass().getName();
                String methodName = method.getName();
                String statementId = namespace + "." + methodName;
                Type genericReturnType = method.getGenericReturnType();
                //根据但会类型判断
                if (genericReturnType instanceof ParameterizedType) {
                    //判断 泛型类型参数化
                    return selectAll(statementId, args);
                }else {
                    return selectOne(statementId, args);
                }
            }
        });

    }

}
