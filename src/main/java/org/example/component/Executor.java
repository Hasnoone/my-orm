package org.example.component;


import org.example.pojo.MapperStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <T> List<T> executeQuery(Connection connection, MapperStatement statement,Object... params) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException, IntrospectionException, InvocationTargetException;


}
