package com.first_class.msa.message.application.service;

import com.first_class.msa.message.infrastructure.client.UserClient;
import com.first_class.msa.message.application.dto.ResUserGetByIdDTO;
import com.first_class.msa.message.libs.exception.ApiException;
import com.first_class.msa.message.libs.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SlackIntegrationService {

    private final RestTemplate restTemplate;
    private final UserClient userClient;

    private static final String SLACK_API_URL = "https://slack.com/api/";

    @Value("${slack.bot.token}")
    private String slackBotToken;

    // 슬랙 채널에 메시지 보내기
    public void sendMessageToChannel(Long userId, String channelId, String content) {
        try {
            joinChannel(channelId);

            String url = SLACK_API_URL + "chat.postMessage";
            ResponseEntity<ResUserGetByIdDTO> user = userClient.getUser(userId, null, null);
            String slackEmail = user.getBody().getSlackId();
            sendMessageToSlack(url, channelId, content);
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SLACK_API_ERROR);
        }
    }

    // 슬랙 DM에 메시지 보내기
    public void sendMessageToDirectMessage(String slackEmail, String content) {
        try {
            String slackUserId = getSlackUserIdByEmail(slackEmail);

            String url = SLACK_API_URL + "chat.postMessage";
            sendMessageToSlack(url, slackUserId, content);
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SLACK_API_ERROR);
        }
    }


    private String getSlackUserIdByEmail(String slackEmail) {
        String url = SLACK_API_URL + "users.lookupByEmail?email=" + slackEmail;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(slackBotToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        JSONObject responseBody = new JSONObject(responseEntity.getBody());
        if (responseBody.getBoolean("ok")) {
            return responseBody.getJSONObject("user").getString("id");
        } else {
            throw new RuntimeException("Failed to find Slack user by email: " + responseBody.getString("error"));
        }
    }

    // 슬랙 채널에 가입하기
    private void joinChannel(String channelId) {
        String joinUrl = SLACK_API_URL + "conversations.join";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(slackBotToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("channel", channelId);

        HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(joinUrl, HttpMethod.POST, requestEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to join Slack channel: " + responseEntity.getStatusCode());
        }

        JSONObject responseBody = new JSONObject(responseEntity.getBody());
        if (!responseBody.getBoolean("ok")) {
            throw new RuntimeException("Slack API error while joining channel: " + responseBody.getString("error"));
        }
    }

    // 메시지 전송 공통 메서드
    private void sendMessageToSlack(String url, String channelOrUserEmail, String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(slackBotToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("channel", channelOrUserEmail);
        body.put("text", content);

        HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        } catch (Exception e) {
            // 로그 추가: 예외 발생 시 출력
            System.err.println("Error while sending message to Slack: " + e.getMessage());
            throw new RuntimeException("Failed to send message to Slack", e);
        }

        JSONObject responseBody = new JSONObject(responseEntity.getBody());
        if (!responseBody.getBoolean("ok")) {
            throw new RuntimeException("Slack API error: " + responseBody.getString("error"));
        }
    }


}