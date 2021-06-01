package org.example.component;

import java.sql.SQLException;

public interface SqlSessionFactory {


    SqlSession openSession() throws SQLException;

}
