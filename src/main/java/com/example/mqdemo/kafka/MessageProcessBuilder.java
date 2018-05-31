package com.example.mqdemo.kafka;

import com.example.mqdemo.kafka.consume.KafkaMessageConsumeTask;
import com.yidian.commerce.common.utils.rabbitmq.MessageProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessBuilder {

  @Autowired
  KafkaMessageConsumeTask kafkaMessageConsumeTask;
  //消息处理函数
  public MessageProcess<String> buildMessageProcess() {
    return kafkaMessageConsumeTask::process;
  }

}
