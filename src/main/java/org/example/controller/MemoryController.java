package org.example.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @date 2025-09-22 19:34
 */
@RestController
@RequestMapping("/ai")
public class MemoryController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/memory")
    public String chat(@RequestParam(value = "msg") String msg, @RequestParam(value =
            "conversationId") String conversationId) {
        String result = this.chatClient.prompt()
                .user(msg)
                // 重点：通过 advisor 参数指定对话ID
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
        return result;
    }
}
