package com.example.mqdemo.kafka.consume;

import com.example.mqdemo.kafka.KafkaAccessBuilderCopy;
import com.example.mqdemo.kafka.MessageProcessBuilder;
import com.example.mqdemo.kafka.config.KafkaConfig;
import com.yidian.commerce.common.utils.kafka.KafkaAccessBuilder;
import com.yidian.commerce.common.utils.kafka.KafkaMessageConsumer;
import com.yidian.commerce.common.utils.kafka.KafkaMessageConsumerProcessor;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dudream on 2017/10/18.
 */
@Service
@Slf4j
public class KafkaConsumeProcess extends KafkaMessageConsumerProcessor {

  @Autowired
  private MessageProcessBuilder messageProcessBuilder;
  @Autowired
  private KafkaConfig kafkaInternalConf;

  private KafkaMessageConsumer kafkaMessageConsumer;

  @PostConstruct
  public void init() {
    KafkaAccessBuilderCopy internalKafkaAccessBuilder = new KafkaAccessBuilderCopy(
        kafkaInternalConf.getKafkaConf());
    kafkaMessageConsumer = internalKafkaAccessBuilder
        .buildMessageConsumer(kafkaInternalConf.getTopic(),
            messageProcessBuilder.buildMessageProcess(), kafkaInternalConf.getConsumerThreads());
  }

  public void start() throws IOException {
    kafkaMessageConsumer.consume();
  }

  public void stop() {
    kafkaMessageConsumer.shutdown();
  }


}
