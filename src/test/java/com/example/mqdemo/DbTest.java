package com.example.mqdemo;

import com.example.mqdemo.dao.entity.TestTable;
import com.example.mqdemo.mapper.Test1Dao;
import com.example.mqdemo.mapper.Test2Dao;
import com.example.mqdemo.mapper.Test3Dao;
import com.example.mqdemo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StopWatch;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class DbTest {

    @Autowired
    private Test1Dao test1Dao;

    @Autowired
    private Test2Dao test2Dao;

    @Autowired
    private Test3Dao test3Dao;

    @Autowired
    private TestService testService;

    @Test
    public void test(){
        test1Dao.insert("test1",1);
       // test2Dao.insert("liuyang","w");
        //test3Dao.insert("liuyang1","s");
        List<TestTable> result = new ArrayList<>();
        result.addAll(test1Dao.getByName("test1"));
       // result.addAll(test2Dao.getByName("liuyang"));
        //result.addAll(test3Dao.getByName("liuyang1"));
        System.out.println(result);
    }

    @Test
    public void testGet(){
        List<TestTable> result = new ArrayList<>();
        result.addAll(test1Dao.getByName("test1"));
        result.addAll(test2Dao.getByName("liuyang"));
        result.addAll(test3Dao.getByName("liuyang1"));
        System.out.println(result);
    }

    @Test
    public void testH2(){
        org.h2.Driver.load();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", "root", "123456");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT 'Hello World'");
            while (rs.next())
            {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testService(){
        StopWatch watch = new StopWatch();
        watch.start();
        testService.testInsertFind();
        watch.stop();
        log.info("total cost :{}", watch.getTotalTimeSeconds());
    }

}
