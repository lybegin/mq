package com.example.mqdemo;

import com.example.mqdemo.dao.entity.MongoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test(){
        MongoEntity test = new MongoEntity();
        test.setName("test123");
        test.setSex("man");
        mongoTemplate.save(test);
    }
}
