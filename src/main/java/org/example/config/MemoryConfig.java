package org.example.config;


import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @date 2025-09-23 16:32
 */
@Configuration
public class MemoryConfig {

    @Value("${spring.memory.redis.host}")
    private String redisHost;
    @Value("${spring.memory.redis.port}")
    private int redisPort;
    @Value("${spring.memory.redis.timeout}")
    private int redisTimeout;

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository() {
        return RedisChatMemoryRepository.builder()
                .host(redisHost)
                .port(redisPort)
                .timeout(redisTimeout)
                .build();
    }
}
