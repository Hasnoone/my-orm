package org.example.component;


import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() throws SQLException {
        return new DefaultSqlSession(configuration);
    }
}
