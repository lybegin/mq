package com.example.mqdemo.api;

import com.example.mqdemo.dao.entity.TestTable;
import com.example.mqdemo.kafka.consume.KafkaConsumeProcess;
import com.example.mqdemo.kafka.consume.KafkaMessageConsumeTask;
import com.example.mqdemo.kafka.produce.KafkaMessageSenderTask;
import com.example.mqdemo.service.TestService;
import com.yidian.commerce.common.utils.kafka.KafkaMessageConsumerProcessor;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class FirstApi {

    @Autowired
    private TestService testService;

    @Autowired
    private KafkaMessageSenderTask kafkaMessageSenderTask;

    @Autowired
    private KafkaConsumeProcess kafkaConsumeProcess;

    @RequestMapping("/home")
    public void homes(){
        System.out.println("this is home");
    }

    @RequestMapping("/index")
    @ResponseBody
    public TestTable test(@RequestBody TestTable table){
        testService.insert(table);
        return table;
    }

    @RequestMapping("/index1")
    @ResponseBody
    public void test1(){
        testService.testInsertFind();
    }

    @RequestMapping("/sender")
    public void testSendMessage(@RequestParam("message") String message){
        kafkaMessageSenderTask.process(message);
    }


    @RequestMapping("/start")
    public void testConsumeMessage(){
        try {
            kafkaConsumeProcess.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
