package org.example.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Peng
 * @date 2025-09-22 21:24
 */
@RestController
@RequestMapping("/ai")
public class FunctionController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/fc")
    public String functionCall() {

        String content = this.chatClient.prompt("明天是几号，那天广州天气如何")
                .tools(new DateTimeTools(), new WeatherTools())
                .call().content();
        return content;
    }

    class DateTimeTools {
        @Tool(description = "获取当前的最新日期时间")
        String getCurrentDateTime() {
            System.out.println("获取当前时间工具被调用");
            return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
        }
    }

    class WeatherTools {
        @Tool(description = "获取指定城市的天气")
        String getWeather(
                @ToolParam(description = "城市名称") String city,
                @ToolParam(description = "日期，格式为YYYY-MM-DD，或今天 明天") String date) {
            System.out.println("获取天气工具被调用");
            String format = String.format("城市：%s，日期：%s，天气：多云", city, date);
            System.out.println(format);
            //模拟调用三方API
            return format;
        }
    }
}
