package org.example.component;

import org.example.config.BoundSql;
import org.example.pojo.MapperStatement;
import org.example.utils.GenericTokenParser;
import org.example.utils.ParameterMapping;
import org.example.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {


    @Override
    public <T> List<T> executeQuery(Connection connection, MapperStatement statement, Object... params) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException, IntrospectionException, InvocationTargetException {
        // select * from table where id=#{id} and username=#{username}
        String sqlStatement = statement.getSqlStatement();
        BoundSql boundSql = getBoundSql(sqlStatement);
        // select * from table where id=? and username=?
        String parseSql = boundSql.getParseSql();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        PreparedStatement preparedStatement = connection.prepareStatement(parseSql);
        String paramType = statement.getParamType();
        String resultType = statement.getResultType();
        Class<T> paramClass = getClassType(paramType);
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            //获取#{id} 中的 id 作为content
            String content = parameterMapping.getContent();
            //这里很重要 这里就是通过反射来操作对象
            Field declaredField = paramClass.getDeclaredField(content);
            //暴力修改
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i + 1, o);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        Class<T> resultClass = getClassType(resultType);
        List<T> resultList = new ArrayList<>();

        //封装结果集
        while (resultSet.next()) {
            T t = resultClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //返回结果名
                String columnName = metaData.getColumnName(i);
                //返回值
                Object object = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(t, object);
            }
            resultList.add(t);
        }
        return resultList;
    }





    private BoundSql getBoundSql(String sqlStatement) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        //将#{} 解析成了 ? 占位符
        String parseSqlStatement = genericTokenParser.parse(sqlStatement);
        //解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parseSqlStatement, parameterMappings);
        return boundSql;
    }


    public <T> Class<T> getClassType(String clazPath) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(clazPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (Class<T>) clazz;
    }




}
