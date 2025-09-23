package org.example.controller;


import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

/**
 * @author Peng
 * @date 2025-09-23 16:34
 */
@RestController
@RequestMapping("/ai/doctor")
public class AIDoctorController {

    // 系统提示词模板
    private static final String SYSTEM_PROMPT = """
            你是一位基于医学知识的AI健康咨询助手，你的职责是：
            1. 礼貌询问用户的健康问题（症状、持续时间、年龄、基础疾病等关键信息）
            2. 基于用户描述提供可能的健康建议（非诊断结果）
            3. 明确提示"本建议仅供参考，不替代专业医师诊断"
            4. 对紧急症状（如胸痛、严重出血等）优先建议立即就医
            
            回复要求：
            - 语言通俗易懂，避免专业术语堆砌
            - 结构清晰：先回应问题，再给出建议，最后提示就医
            - 不承诺治愈效果，不提供具体用药剂量建议
            - 对超出常识的问题，引导用户咨询专业医生
            """;

    private final ChatClient chatClient;
    private final int MAX_MESSAGES = 100;
    private final MessageWindowChatMemory messageWindowChatMemory;

    /**
     * AIDoctorController构造函数
     * 初始化聊天客户端和消息窗口内存管理器
     *
     * @param builder                   ChatClient.Builder对象，用于构建聊天客户端
     * @param redisChatMemoryRepository Redis聊天内存仓库，用于持久化聊天记录
     */
    public AIDoctorController(ChatClient.Builder builder, RedisChatMemoryRepository redisChatMemoryRepository) {
        // 构建消息窗口聊天内存管理器，设置最大消息数量和Redis仓库
        this.messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(MAX_MESSAGES)
                .build();

        // 构建聊天客户端，设置默认的消息内存
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
                                .build()
                )
                .build();
    }

    /**
     * 处理聊天调用请求
     * 接收用户查询和会话ID，返回AI助手的回复内容
     *
     * @param query          用户的查询内容
     * @param conversationId 会话ID
     * @return AI助手的回复内容字符串
     */
    @GetMapping("/call")
    public Flux<String> call(@RequestParam(value = "query") String query,
                             @RequestParam(value = "conversation_id") String conversationId,
                             HttpServletResponse response
    ) {
        response.setCharacterEncoding("UTF-8");
        return chatClient.prompt(query)
                .advisors(
                        a -> a.param(CONVERSATION_ID, conversationId)
                )
                .stream().content();
    }

    /**
     * 获取指定会话的所有消息记录
     *
     * @param conversationId 会话ID
     * @return 指定会话的消息列表
     */
    @GetMapping("/messages")
    public List<Message> messages(@RequestParam(value = "conversation_id") String conversationId) {
        return messageWindowChatMemory.get(conversationId);
    }
}
