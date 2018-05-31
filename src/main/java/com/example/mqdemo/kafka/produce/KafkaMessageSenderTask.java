package com.example.mqdemo.kafka.produce;


import com.example.mqdemo.kafka.KafkaAccessBuilderCopy;
import com.example.mqdemo.kafka.config.KafkaConfig;
import com.yidian.commerce.common.utils.common.DetailRes;
import com.yidian.commerce.common.utils.kafka.KafkaAccessBuilder;
import com.yidian.commerce.common.utils.kafka.KafkaMessageSender;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageSenderTask {

  @Autowired
  private KafkaConfig kafkaInternalConf;

  private KafkaMessageSender internalKafkaMessageSender;

  @PostConstruct
  public void init() {
    KafkaAccessBuilderCopy internalKafkaAccessBuilder = new KafkaAccessBuilderCopy(kafkaInternalConf.getKafkaConf());

    internalKafkaMessageSender = internalKafkaAccessBuilder.buildMessageSender(kafkaInternalConf.getTopic(), null);
  }

  public DetailRes process(String message) {
    DetailRes detailRes = internalKafkaMessageSender.send(message.getBytes());
    return detailRes;
  }

}
