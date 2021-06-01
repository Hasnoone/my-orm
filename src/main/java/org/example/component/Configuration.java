package org.example.component;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.Data;
import org.example.pojo.MapperStatement;

import java.util.HashMap;
import java.util.Map;


@Data
public class Configuration {

    Map<String, MapperStatement> mapperStatementMap = new HashMap<>();

    private ComboPooledDataSource comboPooledDataSource;

}
