package com.example.mqdemo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mqdemo.dao.entity.TestTable;
import com.example.mqdemo.mapper.Test1Dao;
import com.example.mqdemo.mapper.Test2Dao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
    @Autowired
    private Test1Dao test1Dao;
    @Autowired
    private Test2Dao test2Dao;

    @Transactional
    public void insert(TestTable table){
        if(table != null){
            test1Dao.insert(table.getName(),table.getSex());
            test2Dao.insert(table.getName(),table.getSex());
        }
    }

    @Transactional
    public void testInsertFind(){
        TestTable testTable = new TestTable();
        testTable.setName("test");
        testTable.setSex(1);
        List<TestTable> testTableList = test1Dao.getByName("liuyang");
        test1Dao.getByName("liu");
        List<TestTable> testTableList1 = test2Dao.getByName("lyly");
        test2Dao.getByName("ly");
        test1Dao.insert(testTable.getName(), testTable.getSex());
        test2Dao.insert(testTable.getName(), testTable.getSex());
        System.out.println(JSONObject.toJSONString(testTableList));
        System.out.println(JSONObject.toJSONString(testTableList1));
    }

}
