package com.example.mqdemo.context;

import com.example.mqdemo.kafka.consume.KafkaConsumeProcess;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

@Component
public class SystemInitContext extends ContextLoaderListener{

  @Autowired
  private KafkaConsumeProcess kafkaConsumeProcess;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      kafkaConsumeProcess.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    super.contextDestroyed(event);
  }
}
