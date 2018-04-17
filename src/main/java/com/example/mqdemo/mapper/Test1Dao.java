package com.example.mqdemo.mapper;

import com.example.mqdemo.dao.Dao;
import com.example.mqdemo.dao.DataSourceType;
import com.example.mqdemo.dao.entity.TestTable;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Dao(dbName = DataSourceType.test1)
@Mapper
@Repository
public interface Test1Dao {

    @Insert("insert into test(name,sex) values(#{name},#{sex})")
    void insert(@Param("name") String name,@Param("sex") int sex);

    @Select("select * from test where name like #{0}" )
    List<TestTable> getByName(String name) ;
}
