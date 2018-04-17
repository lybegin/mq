package com.example.mqdemo.serverConfig;

import com.example.mqdemo.dao.DataSourceType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class MultiDataSourceHold implements InitializingBean{

  private final static ThreadLocal<DataSourceType> dataSourceKey = new ThreadLocal<>();

  private static Map<DataSourceType, List<DataSourceType>> dataSourceTypeListMap = Maps.newHashMap();

  private static AtomicInteger test1SlaveCount = new AtomicInteger(-1);

  private static AtomicInteger test2SlaveCount = new AtomicInteger(-1);

  private static AtomicInteger test3SlaveCount = new AtomicInteger(-1);

  private static Map<DataSourceType, AtomicInteger> dataSourceCountMap = Maps.newHashMap();

  public static void setDataSourceKey(DataSourceType dataSource){
    dataSourceKey.set(dataSource);
  }

  public static Object getDataSourceKey(){
    return dataSourceKey.get();
  }

  public static void clearDataSourceKey(){
    dataSourceKey.remove();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    dataSourceTypeListMap.put(DataSourceType.test1, Lists.newArrayList(DataSourceType.slave1Test1, DataSourceType.slave2Test1));
    dataSourceTypeListMap.put(DataSourceType.test2, Lists.newArrayList(DataSourceType.slave1Test2, DataSourceType.slave2Test2));
    dataSourceTypeListMap.put(DataSourceType.test3, Lists.newArrayList(DataSourceType.slave1Test3, DataSourceType.slave2Test3));

    dataSourceCountMap.put(DataSourceType.test1, test1SlaveCount);

    dataSourceCountMap.put(DataSourceType.test2, test2SlaveCount);

    dataSourceCountMap.put(DataSourceType.test3, test3SlaveCount);
  }


  public static  DataSourceType getSlaveDataSource(DataSourceType dataSourceType){
    List<DataSourceType> slaveDataSourceList = dataSourceTypeListMap.get(dataSourceType);
    Integer index = dataSourceCountMap.get(dataSourceType).incrementAndGet() % slaveDataSourceList.size();
    if(dataSourceCountMap.get(dataSourceType).get() >= Integer.MAX_VALUE-1){
      dataSourceCountMap.get(dataSourceType).set(-1);
    }
    return slaveDataSourceList.get(index);
  }

}
