package org.example.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Peng
 * @date 2025-09-22 16:57
 */
@RestController
@RequestMapping("/ai")
public class DefaultChatClientController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 使用ChatClient 生成结果
     *
     * @param message
     * @return
     */
    @GetMapping("/default_client")
    public String generation(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        return this.chatClient
                .prompt()//创建一个prompt
                .user(message)//设置用户输入
                .call()//非流式调用，一次性输出
                .content();//获取结果
    }

    //stream 方法开启流式响应模式，最后通过 content 方法获取流式的响应内容，返回类型为 Flux
    //这是 Reactor 库中的响应式类型，用于处理流式数据,支持异步、非阻塞地处理流式数据，适合高并发场景的需求
    //AI 模型生成的内容会被分割成多个片段逐次返回，content () 方法会将这些片段以字符串的形式通过 Flux 发射出去

    @GetMapping(value = "/stream_client", produces = "text/html;charset=UTF-8")
    public Flux<String> stream(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        return this.chatClient
                .prompt()//创建一个prompt
                .user(message)//设置用户输入
                .stream()//流式调用
                .content();//获取结果
    }
}
