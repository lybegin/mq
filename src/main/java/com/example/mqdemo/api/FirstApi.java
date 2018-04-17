package com.example.mqdemo.api;

import com.example.mqdemo.dao.entity.TestTable;
import com.example.mqdemo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class FirstApi {

    @Autowired
    private TestService testService;

    @RequestMapping("/home")
    @ResponseBody
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
}
