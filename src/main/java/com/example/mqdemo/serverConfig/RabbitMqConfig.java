package com.example.mqdemo.serverConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class RabbitMqConfig {

   /* @Value("${spring.rabbitmq.publisher-confirms}")
    private String publisherConfirms;

    @Value("${spring.rabbitmq.username}")
    private String userName;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.addresses}")
    private String address;

    @Value("${spring.rabbitmq.dynamic}")
    private String dynamic;

    @Value("${spring.rabbitmq.listener.simple.prefetch}")
    private String prefetch;

    @Value("${spring.rabbitmq.template.retry.initial-interval}")
    private String initialInterval;

    @Value("${spring.rabbitmq.template.retry.max-attempts}")
    private String maxAttempts;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;


    *//**
     * rabbitmq
     *//*
    @Value("${spring.rabbitmq.test1.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.test1.binding-key}")
    private String bindingKey1;

    @Value("${spring.rabbitmq.test1.queue}")
    private String queue1;

   *//* @Value("${spring.rabbitmq.test2.exchange}")
    private String exchange2;
    *//*
    @Value("${spring.rabbitmq.test2.binding-key}")
    private String bindingKey2;

    @Value("${spring.rabbitmq.test2.queue}")
    private String queue2;


    @Bean
    public ConnectionFactory getConnection(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(address);
        connectionFactory.setUsername(userName);
        connectionFactory.setPort(Integer.valueOf(port));
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(Boolean.valueOf(publisherConfirms));
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }


    *//*public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.
    }*//*

    @Bean(name = "testQueue1" )
    public Queue queue1(){
        return new Queue(queue1,true);
    }

    @Bean(name = "testQueue2" )
    public Queue queue2(){
        return new Queue(queue2,true);
    }


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange,true,false);
    }

    @Bean
    public Binding bindingTopic1(@Qualifier("testQueue1") Queue queue1, TopicExchange topic){
        return BindingBuilder.bind(queue1).to(topic).with(bindingKey1);
    }

    @Bean
    public Binding bindingTopic2(@Qualifier("testQueue2") Queue queue2, TopicExchange topic){
        return BindingBuilder.bind(queue2).to(topic).with(bindingKey2);
    }

    @Bean
    public MessageConverter getMessageConvert(){
        JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
        jsonMessageConverter.setCreateMessageIds(true);
        return jsonMessageConverter;
    }*/

}
