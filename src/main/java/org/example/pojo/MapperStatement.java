package org.example.pojo;

import lombok.Data;

/**
 * 这个就是xml中的一个sql对象
 */
@Data
public class MapperStatement {


    private String id;
    private String paramType;
    private String resultType;
    private String sqlStatement;





}
