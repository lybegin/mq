package com.example.mqdemo.serverConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.hostName}")
    private String hostName;

    @Value("${spring.data.redis.timeout}")
    private int timeout;

    @Value("${spring.data.redis.database}")
    private int index;

    /**
     * pool config
     */

    @Value("${spring.data.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.pool.max-wait}")
    private Long maxWait;

    @Value("${spring.data.redis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisConnectionFactory getJedisConnection(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setTimeout(timeout);
        jedisConnectionFactory.setHostName(hostName);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setDatabase(index);
        return jedisConnectionFactory;
    }

    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        return jedisPoolConfig;
    }

}
