package org.example.component;

import lombok.Data;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface SqlSession {


   <T> List<T> selectAll(String statementId,Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, IntrospectionException, InvocationTargetException;


   <T> T selectOne(String statementId, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException;


   <T> T getMapper(Class<T> clazz);


}
