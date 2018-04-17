package com.example.mqdemo.mapper;

import com.example.mqdemo.dao.Dao;
import com.example.mqdemo.dao.DataSourceType;
import com.example.mqdemo.dao.entity.TestTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Dao(dbName = DataSourceType.test2)
@Mapper
@Repository
public interface Test2Dao {

    @Insert("insert into test2.test(name,sex) values(#{name},#{sex})")
    void insert(@Param("name") String name, @Param("sex") int sex);

    @Select("select * from test2.test where name like #{0}" )
    List<TestTable> getByName(String name) ;
}
