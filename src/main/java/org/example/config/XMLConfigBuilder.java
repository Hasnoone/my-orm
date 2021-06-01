package org.example.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.component.Configuration;
import org.example.component.Resource;
import org.example.component.XMLMapperBuilder;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;


    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        //解析jdbc的配置文件
        Document read = new SAXReader().read(in);

        //解析SQL Mapper.xml的
        // <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
//        <property name="jdbcUrl" value="jdbc:mysql:///practice"></property>
//        <property name="username" value="root"></property>
//        <property name="password" value="root"></property>
        Element rootElement = read.getRootElement();
        Properties properties = new Properties();
        List<Element> propertyElement = rootElement.selectNodes("//property");
        for (Element element : propertyElement) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.put(name, value);
        }
        //解析 jdbc 连接参数
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setComboPooledDataSource(comboPooledDataSource);

        List<Element> mapperElement = rootElement.selectNodes("//mapper");
        //解析具体的xml配置文件
        for (Element element : mapperElement) {
            String resource = element.attributeValue("resource");
            InputStream inputStream = Resource.getInputStream(resource);

            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(inputStream);

        }
        return configuration;
    }



}
