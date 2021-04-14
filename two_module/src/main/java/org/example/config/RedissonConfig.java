package org.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//https://github.com/redisson/redisson
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.Lettuce.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.Lettuce.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.Lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.Lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Bean
    public RedissonClient getRedissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port)//Redis url should start with redis:// or rediss://
                .setDatabase(database)
                .setTimeout(timeout);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
