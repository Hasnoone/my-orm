package org.example.component;

import org.dom4j.DocumentException;
import org.example.config.XMLConfigBuilder;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) throws PropertyVetoException, DocumentException {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);
        return new DefaultSqlSessionFactory(configuration);
    }

}
