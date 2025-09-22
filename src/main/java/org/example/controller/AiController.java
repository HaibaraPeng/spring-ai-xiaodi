package org.example.controller;


import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Peng
 * @date 2025-09-22 16:48
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private OpenAiChatModel chatModel;

    @GetMapping("/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        return Map.of("generation", chatModel.call(message));
    }
}
