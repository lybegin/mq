package com.example.mqdemo.kafka.config;

import java.util.Properties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KafkaConfig {

  @Value("${kafka.metadata.broker.list2}")
  private String metadataBrokerList;
  @Value("${kafka.topic}")
  private String topic;
  @Value("${kafka.zookeeper.connect}")
  private String zookeeperConnect;
  @Value("${kafka.groupid}")
  private String groupId;
  @Value("${kafka.consumer.threads}")
  private Integer consumerThreads;

  @Value("${kafka.processing.guarantee}")
  private String kafkaGuarantee;

  public Properties getKafkaConf() {
    Properties properties = new Properties();
    //producer
    properties.put("serializer.class", "kafka.serializer.DefaultEncoder"); // DefaultEncoder for binary StringEncoder for String
    properties.put("key.serializer.class", "kafka.serializer.StringEncoder"); // DefaultEncoder for binary StringEncoder for String
    properties.put("message.send.max.retries", "3");
    properties.put("retry.backoff.ms","100");
    //同步方式发送
    properties.put("producer.type", "sync");

    properties.put("compression.codec","snappy");
    properties.put("request.required.acks", "1");

    //consumer
    properties.put("zookeeper.session.timeout.ms", "4000");
    properties.put("zookeeper.connection.timeout.ms", "10000");
    properties.put("rebalance.backoff.ms", "2000");
    properties.put("rebalance.max.retries", "10");
    properties.put("zookeeper.sync.time.ms", "200");
    properties.put("auto.commit.interval.ms", "6000");
    properties.put("auto.offset.reset", "smallest");
    properties.put("fetch.message.max.bytes", String.valueOf(20 * 1024 * 1024)); // 20M

    //producer
    properties.put("metadata.broker.list", metadataBrokerList);
    //consumer
    properties.put("zookeeper.connect", zookeeperConnect);
    properties.put("group.id", groupId);

    properties.put("processing.guarantee", kafkaGuarantee);

    return properties;
  }

}
