package com.example.mqdemo.serverConfig;

import lombok.Data;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Data
public class MultiDataSource extends AbstractRoutingDataSource {

    @Override
    protected  Object determineCurrentLookupKey() {
        return MultiDataSourceHold.getDataSourceKey();
    }

}
