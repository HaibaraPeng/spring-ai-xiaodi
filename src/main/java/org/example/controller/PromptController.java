package org.example.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Peng
 * @date 2025-09-22 17:33
 */
@RestController
@RequestMapping("/ai")
public class PromptController {

    @Autowired
    private OpenAiChatModel chatModel;

    @GetMapping("/prompt")
    public String prompt() {
        // 方式1：直接构造
        Prompt prompt = new Prompt("你是名称是");

        // 方式2：结构化角色消息
        List<Message> messages = List.of(
                new SystemMessage("你是Java讲师，名称叫大当家"),
                new UserMessage("你是名称是")
        );
        Prompt structuredPrompt = new Prompt(messages);

        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String content = chatClient.prompt(structuredPrompt).call().content();
        return content;

    }

    @GetMapping("/role")
    public String role() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String content = chatClient.prompt().system("你是小滴课堂技术专家").user("介绍下你们有什么编程课程")
                .call().content();
        return content;
    }

    @GetMapping("/template")
    public String template() {
        // 定义模板
        PromptTemplate template = new PromptTemplate("请以{theme}为主题写一首诗");
        // 填充参数
        Prompt poemPrompt = template.create(Map.of("theme", "春天"));
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String content = chatClient.prompt(poemPrompt).call().content();
        return content;
    }
}
