package org.example.component;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.pojo.MapperStatement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析文件流
     * @param in
     */
    public void parse(InputStream in) throws DocumentException {
        Document read = new SAXReader().read(in);
        Element rootElement = read.getRootElement();
        //获取所有 select 标签
        List<Element> selectNodes = rootElement.selectNodes("//select");
        //获取 namespase 的属性值
        String namespaces = rootElement.attributeValue("namespase");
        for (Element selectNode : selectNodes) {
            //遍历每一条select语句
            String id = selectNode.attributeValue("id");
            String resultType = selectNode.attributeValue("resultType");
            String parameterType = selectNode.attributeValue("parameterType");
            String sqlStatement = selectNode.getTextTrim();
            //组装sql语句
            MapperStatement statement = new MapperStatement();
            statement.setId(id);
            statement.setParamType(parameterType);
            statement.setResultType(resultType);
            statement.setSqlStatement(sqlStatement);
            configuration.getMapperStatementMap().put(namespaces + "." + id, statement);
        }






    }



}
