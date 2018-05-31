package com.example.mqdemo;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate ;

    @Test
    @Ignore
    public void test(){
        redisTemplate.opsForValue().set("hello","world123");
        System.out.println(redisTemplate.opsForValue().get("hello"));
    }

}
