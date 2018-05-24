package com.example.mqdemo;

import com.example.mqdemo.api.FirstApi;
import com.example.mqdemo.dao.entity.TestTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApiTest {

    @Autowired
    private FirstApi firstApi;

    @Test
    public void test(){
        TestTable table = new TestTable();
        table.setBirth(new Date());
        table.setId(12345L);
        table.setName("liuyang");
        table.setSex(1);
        firstApi.test(table);
    }
}
