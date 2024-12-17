package com.first_class.msa.message.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "slack-api", url = "https://slack.com/api")
public interface SlackApiClient {

    @GetMapping("/conversations.list")
    ResponseEntity<Map<String, Object>> getChannels(@RequestParam("token") String token);

    @GetMapping("/conversations.history")
    ResponseEntity<Map<String, Object>> getMessagesFromChannel(
            @RequestParam("channel") String channelId,
            @RequestParam("token") String token);
}
