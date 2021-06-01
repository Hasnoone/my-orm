package org.example.config;


import lombok.Data;
import org.example.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoundSql {


    private String parseSql;
    private List<ParameterMapping> parameterMappingList=new ArrayList<>();



    public BoundSql(String parseSql, List<ParameterMapping> parameterMappingList) {
        this.parseSql = parseSql;
        this.parameterMappingList = parameterMappingList;
    }
}
