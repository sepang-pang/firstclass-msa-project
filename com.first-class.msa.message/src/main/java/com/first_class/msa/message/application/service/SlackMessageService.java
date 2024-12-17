package com.first_class.msa.message.application.service;

import com.first_class.msa.message.domain.model.Message;
import com.first_class.msa.message.infrastructure.client.SlackApiClient;
import com.first_class.msa.message.infrastructure.client.UserClient;
import com.first_class.msa.message.application.dto.ResUserGetByIdDTO;
import com.first_class.msa.message.infrastructure.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackMessageService {

    private final SlackApiClient slackApiClient;
    private final MessageRepository messageRepository;
    private final UserClient userClient;

    // 채널에서 메시지 가져오기
    @Transactional
    public void fetchAndSaveMessageFromChannel(String channelId, String token){
        ResponseEntity<Map<String, Object>> response = slackApiClient.getMessagesFromChannel(channelId, token);

        if (response.getBody() != null && response.getBody().containsKey("messages")) {
            List<Map<String, Object>> messages = (List<Map<String, Object>>) response.getBody().get("messages");

            for (Map<String, Object> messageData : messages) {
                String slackUserId = (String) messageData.get("user");
                String content = (String) messageData.get("text");

                if (slackUserId != null){
                    // 메시지 저장
                    saveMessage(slackUserId, content);
                }
            }
        } else {
            System.out.println("No messages found in channel " + channelId);
        }
    }

    // 채널에서 메시지 가져오기
    @Transactional
    public void fetchAndSaveMessages(String token) {
        ResponseEntity<Map<String, Object>> response = slackApiClient.getChannels(token);

        if (response.getBody() != null && response.getBody().containsKey("channels")) {
            List<Map<String, Object>> channels = (List<Map<String, Object>>) response.getBody().get("channels");

            // 각 채널에서 메시지 가져오기
            for (Map<String, Object> channel : channels) {
                String channelId = (String) channel.get("id");
                fetchAndSaveMessageFromChannel(channelId, token);
            }
        } else {
            System.out.println("No channels found or API response is empty.");
        }
    }

    private void saveMessage(String slackUserId, String content) {

        String createdBy = getUsernameBySlackId(slackUserId);

        Message message = Message.builder()
                .content(content)
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .sendResult(true)
                .build();

        messageRepository.save(message);
    }

    private String getUsernameBySlackId(String slackId) {
        ResponseEntity<ResUserGetByIdDTO> response = userClient.getUser(null, slackId, null);

        ResUserGetByIdDTO userDto = response.getBody();

        if (userDto == null || userDto.getUsername() == null) {
            throw new RuntimeException("Slack ID에 해당하는 사용자를 찾을 수 없습니다.");
        }

        return userDto.getUsername();
    }



}
