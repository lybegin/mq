package com.example.mqdemo.consume;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumeTask {

    @RabbitListener(queues = "testQueue1")
    @RabbitHandler
    public void process(String str){
        System.out.println("receiver1:"+str);
    }

    @RabbitListener(queues = "testQueue2")
    @RabbitHandler
    public void process1(String str){
        System.out.println("receiver2:"+str);
    }
}
