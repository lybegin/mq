package com.example.mqdemo.kafka.consume;

import com.alibaba.fastjson.JSONObject;
import com.yidian.commerce.common.utils.common.DetailRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageConsumeTask {


  public DetailRes process(String object) {

    System.out.println("正在消费的消息是:" + JSONObject.toJSONString(object));
    log.info("message is :{}",object);
    DetailRes res = new DetailRes();
    res.setSuccess(true);
    res.setErrMsg("");

    return new DetailRes();

  }

}
