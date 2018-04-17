package com.example.mqdemo.serverConfig;

import com.example.mqdemo.dao.DataSourceType;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Configuration
public class DbConfig {

    @Autowired
    private Environment env;

    @Bean
    public MultiDataSource buildDataSource() throws  Exception{
        DataSource test1DataSource =  buildBasicDataSoruce("test1");
        DataSource test2DataSource = buildBasicDataSoruce("test2");
        DataSource test3DataSource = buildBasicDataSoruce("test3");

        DataSource slave1Test1DataSource = buildBasicDataSoruce("slave1_test1");
        DataSource slave1Test2DataSource = buildBasicDataSoruce("slave1_test2");
        DataSource slave1Test3DataSource = buildBasicDataSoruce("slave1_test3");

        DataSource slave2Test1DataSource = buildBasicDataSoruce("slave2_test1");
        DataSource slave2Test2DataSource = buildBasicDataSoruce("slave2_test2");
        DataSource slave2Test3DataSource = buildBasicDataSoruce("slave2_test3");

        Map<Object,Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.test1,test1DataSource);
        dataSourceMap.put(DataSourceType.test2,test2DataSource);
        dataSourceMap.put(DataSourceType.test3,test3DataSource);
        dataSourceMap.put(DataSourceType.slave1Test1, slave1Test1DataSource);
        dataSourceMap.put(DataSourceType.slave1Test2, slave1Test2DataSource);
        dataSourceMap.put(DataSourceType.slave1Test3, slave1Test3DataSource);

        dataSourceMap.put(DataSourceType.slave2Test1, slave2Test1DataSource);
        dataSourceMap.put(DataSourceType.slave2Test2, slave2Test2DataSource);
        dataSourceMap.put(DataSourceType.slave2Test3, slave2Test3DataSource);

        MultiDataSource dataSource = new MultiDataSource();
        dataSource.setTargetDataSources(dataSourceMap);
        dataSource.setDefaultTargetDataSource(test1DataSource);
        return dataSource;
    }


    public DataSource buildBasicDataSoruce(String dataSourceName) throws  Exception{
        Properties properties = new Properties();
        String prefix = "jdbc.";
        properties.put("url",env.getProperty(prefix+dataSourceName+".url"));
        properties.put("username",env.getProperty(prefix+dataSourceName+".username"));
        properties.put("password",env.getProperty(prefix+dataSourceName+".password"));
     //   properties.put("driverClassName",env.getProperty(prefix+dataSourceName+".driver-class-name"));
        properties.put("maxActive",Integer.valueOf(env.getProperty((prefix+"pool"+".maxActive"))));
        properties.put("maxIdle",Integer.valueOf(env.getProperty(prefix+"pool"+".maxIdle")));
        properties.put("maxWait",Long.valueOf(env.getProperty(prefix+"pool"+".maxWait")));
        properties.put("minIdle",Integer.valueOf(env.getProperty(prefix+"pool"+".minIdle")));
        properties.put("testOnBorrow",true);
        properties.put("minIdle",Integer.valueOf(env.getProperty(prefix+"pool"+".minIdle")));
        properties.put("minEvictableIdleTimeMillis",Long.valueOf(env.getProperty(prefix+"pool"+".minEvictableIdleTimeMillis")));
        DataSource basicDataSource = BasicDataSourceFactory.createDataSource(properties);
        return basicDataSource;
    }

}
