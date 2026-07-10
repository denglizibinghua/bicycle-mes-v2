package com.bicyclemes.framework.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AI 配置
 *
 * @author BicycleMES
 */
@Configuration
public class AiConfig
{
    @Bean
    public ChatClient chatClient(ChatModel chatModel)
    {
        return ChatClient.create(chatModel);
    }
}