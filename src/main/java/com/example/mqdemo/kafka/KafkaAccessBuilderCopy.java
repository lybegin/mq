package com.example.mqdemo.kafka;

import com.yidian.commerce.common.utils.common.DetailRes;
import com.yidian.commerce.common.utils.kafka.KafkaAccessBuilder;
import com.yidian.commerce.common.utils.kafka.KafkaMessageConsumer;
import com.yidian.commerce.common.utils.kafka.KafkaMessageConsumerProcessor;
import com.yidian.commerce.common.utils.kafka.KafkaMessageSender;
import com.yidian.commerce.common.utils.model.ChargeInterfaceModel;
import com.yidian.commerce.common.utils.rabbitmq.MessageProcess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndMetadata;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaAccessBuilderCopy {

  private static final Logger log = LoggerFactory.getLogger(KafkaAccessBuilderCopy.class);
  private Properties properties;

  public KafkaAccessBuilderCopy(Properties properties) {
    this.properties = properties;
  }

  public KafkaMessageSender buildMessageSender(final String topic, final String key) {
    final Producer<String, byte[]> producer = new Producer(new ProducerConfig(this.properties));
    return new KafkaMessageSender() {
      private volatile long keyIndex = 0L;

      public DetailRes send(byte[] message) {
        KeyedMessage keyedMessage = new KeyedMessage(topic, key != null ? key : UUID.randomUUID().toString(), message);

        try {
          long t1 = System.currentTimeMillis();
          producer.send(keyedMessage);
        /*  MetricsUtil.markMeter("kafka.send.one");
          MetricsUtil.histogram("kafka.send.one.cost", System.currentTimeMillis() - t1);*/
          return new DetailRes(true, "");
        } catch (Exception var5) {
          //KafkaAccessBuilder.log.error("error", var5);
          return new DetailRes(false, var5.toString());
        }
      }

      public DetailRes send(List<String> messages) {
        if (messages != null && messages.size() != 0) {
          try {
            long t1 = System.currentTimeMillis();
            List<KeyedMessage<String, byte[]>> list = new ArrayList();
            Iterator var5 = messages.iterator();

            while(var5.hasNext()) {
              String message = (String)var5.next();
              list.add(new KeyedMessage(topic, key != null ? key : UUID.randomUUID().toString(), message.getBytes()));
            }

            producer.send(list);
           /* MetricsUtil.markMeter("kafka.send.list");
            MetricsUtil.histogram("kafka.send.list.cost", System.currentTimeMillis() - t1);*/
            return new DetailRes(true, "");
          } catch (Exception var7) {
            KafkaAccessBuilderCopy.log.error("error", var7);
            return new DetailRes(false, var7.toString());
          }
        } else {
          return new DetailRes(true, "");
        }
      }
    };
  }

  public KafkaMessageConsumer buildMessageConsumer(String topic, final MessageProcess<String> messageProcess, int threads) {
    final ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(this.properties));
    Map<String, Integer> topicCountMap = new HashMap();
    topicCountMap.put(topic, new Integer(threads));
    StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
    DefaultDecoder defaultDecoder = new DefaultDecoder(new VerifiableProperties());
    Map<String, List<KafkaStream<String, byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap, keyDecoder, defaultDecoder);
    final ExecutorService es = Executors.newFixedThreadPool(threads);
    final List<KafkaStream<String, byte[]>> streams = (List)consumerMap.get(topic);
    return new KafkaMessageConsumer() {
      public void consume() {
        Iterator var1 = streams.iterator();

        while(var1.hasNext()) {
          KafkaStream<String, byte[]> stream = (KafkaStream)var1.next();
          es.execute(() -> {
            ConsumerIterator it = stream.iterator();

            while(it.hasNext()) {
              try {
                MessageAndMetadata<String, byte[]> msgMeta = it.next();
                String msgStr = new String((byte[])msgMeta.message());
               // KafkaAccessBuilder.log.debug("@@kafka msg: {}", msgStr);
                String c = null;

                try {
                  c = msgStr;
                } catch (Exception var9) {
                //  KafkaAccessBuilder.log.error("kafka msg to ChargeInterfaceModel error, {} {} {} {} {}", new Object[]{Thread.currentThread().getName(), msgMeta.topic(), msgMeta.partition(), msgMeta.offset(), msgStr, var9});
                }

                if (c == null) {
                //  KafkaAccessBuilder.log.error("kafka msg to ChargeInterfaceModel error, {} {} {} {} {}", new Object[]{Thread.currentThread().getName(), msgMeta.topic(), msgMeta.partition(), msgMeta.offset(), msgStr});
                } else {
                 /* MetricsUtil.markMeter("kafka.consume");
                  MetricsUtil.markMeter("kafka.consume." + msgMeta.topic() + "." + msgMeta.partition());*/
                //  long consumeCost = System.currentTimeMillis() - c.getChargeTime();
               //   KafkaAccessBuilder.log.info("kafka consume {} {} {} {} {}", new Object[]{Thread.currentThread().getName(), msgMeta.topic(), msgMeta.partition(), msgMeta.offset(), consumeCost});
                 /* MetricsUtil.histogram("kafka.consume.cost", consumeCost);
                  MetricsUtil.histogram("kafka.consume.cost." + msgMeta.topic() + "." + msgMeta.partition(), consumeCost);*/
                  messageProcess.process(c);
                }

                consumer.commitOffsets();
              } catch (Exception var10) {
              //  KafkaAccessBuilder.log.error("kafka consume error", var10);

                try {
                  Thread.sleep(200L);
                } catch (InterruptedException var8) {
                 // KafkaAccessBuilder.log.error("kafka sleep error", var8);
                }
              }
            }

          });
        }

      }

      public void shutdown() {
        if (consumer != null) {
          consumer.shutdown();
        }

        if (es != null) {
          es.shutdown();
        }

      }
    };
  }

  public KafkaMessageConsumer buildStrMessageConsumer(String topic, final MessageProcess<String> messageProcess, int threads) {
    final ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(this.properties));
    Map<String, Integer> topicCountMap = new HashMap();
    topicCountMap.put(topic, new Integer(threads));
    StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
    DefaultDecoder defaultDecoder = new DefaultDecoder(new VerifiableProperties());
    Map<String, List<KafkaStream<String, byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap, keyDecoder, defaultDecoder);
    final ExecutorService es = Executors.newFixedThreadPool(threads);
    final List<KafkaStream<String, byte[]>> streams = (List)consumerMap.get(topic);
    return new KafkaMessageConsumer() {
      public void consume() {
        Iterator var1 = streams.iterator();

        while(var1.hasNext()) {
          KafkaStream<String, byte[]> stream = (KafkaStream)var1.next();
          es.execute(() -> {
            ConsumerIterator it = stream.iterator();

            while(it.hasNext()) {
              try {
                MessageAndMetadata<String, byte[]> msgMeta = it.next();
                String msgStr = new String((byte[])msgMeta.message());
               // KafkaAccessBuilder.log.debug("@@kafka msg: {}", msgStr);
                if (msgStr != null && msgStr.length() != 0) {
                //  KafkaAccessBuilder.log.info("kafka consume {} {} {} {}", new Object[]{Thread.currentThread().getName(), msgMeta.topic(), msgMeta.partition(), msgMeta.offset()});
                  messageProcess.process(msgStr);
                } else {
               //   KafkaAccessBuilder.log.error("kafka msg is empty, {} {} {} {} {}", new Object[]{Thread.currentThread().getName(), msgMeta.topic(), msgMeta.partition(), msgMeta.offset(), msgStr});
                }
              } catch (Exception var6) {
              //  KafkaAccessBuilder.log.error("kafka consume error", var6);

                try {
                  Thread.sleep(200L);
                } catch (InterruptedException var5) {
                //  KafkaAccessBuilder.log.error("kafka sleep error", var5);
                }
              }
            }

          });
        }

      }

      public void shutdown() {
        if (consumer != null) {
          consumer.shutdown();
        }

        if (es != null) {
          es.shutdown();
        }

      }
    };
  }

  public static void main(String[] args) {
  }

  private static void testBlockingQueue() {
    BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(20);

    int i;
    for(i = 0; i < 15; ++i) {
      try {
        blockingQueue.put("" + i);
      } catch (InterruptedException var6) {
        var6.printStackTrace();
      }

      System.out.println("size: " + blockingQueue.size());

      try {
        Thread.sleep(100L);
      } catch (InterruptedException var5) {
        var5.printStackTrace();
      }
    }

    for(i = 0; i < 100; ++i) {
      String a = null;

      try {
        a = (String)blockingQueue.take();
      } catch (InterruptedException var4) {
        var4.printStackTrace();
      }

      System.out.println("pop: " + a);
    }

  }

  private static void testProducer() {
    KafkaAccessBuilder builder = getBuilder();
    KafkaMessageSender testSender = builder.buildMessageSender("test", (String)null);
    KafkaMessageSender chargeTestSender = builder.buildMessageSender("ads_charge_buffer_test", (String)null);
    testSender.send("test_by_lgj".getBytes());
    chargeTestSender.send("chargetest_by_lgj".getBytes());
  }

  private static void testConsumer() {
    KafkaAccessBuilder builder = getBuilder();
    new KafkaMessageConsumerProcessor();
    KafkaMessageConsumer chargeTestConsumer = builder.buildMessageConsumer("ads_charge_buffer_test", new MessageProcess<ChargeInterfaceModel>() {
      public DetailRes process(ChargeInterfaceModel message) {
        System.out.println("@@3 " + message);
        return new DetailRes(true, "");
      }
    }, 1);
    chargeTestConsumer.consume();
  }

  private static KafkaAccessBuilder getBuilder() {
    Properties properties = new Properties();
    properties.put("serializer.class", "kafka.serializer.DefaultEncoder");
    properties.put("key.serializer.class", "kafka.serializer.StringEncoder");
    properties.put("message.send.max.retries", "3");
    properties.put("retry.backoff.ms", "100");
    properties.put("compression.codec", "snappy");
    properties.put("request.required.acks", "1");
    properties.put("metadata.broker.list", "10.103.35.20:9092,10.103.35.21:9092,10.103.35.22:9092,10.103.35.26:9092,10.103.35.27:9092");
    properties.put("zookeeper.connect", "10.103.35.20:2181,10.103.35.21:2181,10.103.35.22:2181/kafka");
    properties.put("group.id", "offline-test");
    properties.put("zookeeper.session.timeout.ms", "4000");
    properties.put("zookeeper.sync.time.ms", "200");
    properties.put("auto.commit.interval.ms", "1000");
    properties.put("auto.offset.reset", "smallest");
    KafkaAccessBuilder builder = new KafkaAccessBuilder(properties);
    return builder;
  }

}
