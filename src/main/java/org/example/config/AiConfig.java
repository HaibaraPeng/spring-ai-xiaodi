package org.example.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @date 2025-09-22 16:56
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你是小滴课堂的超级智能助手 老王，可以回答IT任何技术问题")
                .build();
    }
}
