package com.example.mqdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RabbitmqTest {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void testSend(){

        String context = "hello " + new Date();
        System.out.println("Sender1 : " + context);
        this.rabbitTemplate.convertAndSend("testTopicRabbit","testTopicRabbit.test", context);

        /*String context1 = "hello " + new Date();
        System.out.println("Sender2 : " + context);
        this.rabbitTemplate.convertAndSend("testRabbits", context1);*/
    }

}
