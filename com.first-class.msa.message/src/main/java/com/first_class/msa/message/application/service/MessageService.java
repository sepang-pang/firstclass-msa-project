package com.first_class.msa.message.application.service;

import com.first_class.msa.message.application.dto.ReqMessageUpdateDTO;
import com.first_class.msa.message.domain.model.Message;
import com.first_class.msa.message.infrastructure.client.UserClient;
import com.first_class.msa.message.application.dto.ReqMessagePostDTO;
import com.first_class.msa.message.application.dto.ResMessageDTO;
import com.first_class.msa.message.application.dto.ResUserGetByIdDTO;
import com.first_class.msa.message.infrastructure.repository.MessageRepository;
import com.first_class.msa.message.libs.exception.ApiException;
import com.first_class.msa.message.libs.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {


    private final MessageRepository messageRepository;
    private final SlackIntegrationService slackIntegrationService;
    private final UserClient userClient;

    // 공용 채널에 메시지 보내기
    @Transactional
    public ResMessageDTO createAndSendMessageToChannel(ReqMessagePostDTO request, String channelId) {

        try {
            Message message = createMessage(request);
            slackIntegrationService.sendMessageToChannel(request.getUserId(), channelId, message.getContent());
            message.setSendResult(true);
            Message savedMessage = messageRepository.save(message);

            return mapToResMessageDTO(savedMessage);
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    // DM에 메시지 보내기
    @Transactional
    public ResMessageDTO createAndSendMessageToDM(ReqMessagePostDTO request, String slackEmail) {

        try {
            Message message = createMessageForDM(request, slackEmail);

            slackIntegrationService.sendMessageToDirectMessage(slackEmail, message.getContent());
            message.setSendResult(true);
            Message savedMessage = messageRepository.save(message);

            return mapToResMessageDTO(savedMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    private Message createMessage(ReqMessagePostDTO request) {
        if (request.getUserId() == null && request.getSlackId() == null && request.getUsername() == null) {
            throw new ApiException(ErrorMessage.INVALID_INPUT);
        }

        ResponseEntity<ResUserGetByIdDTO> user = userClient.getUser(
                request.getUserId(),
                request.getSlackId(),
                request.getUsername()
        );

        String createdBy = user.getBody().getUsername();

        return Message.builder()
                .content(request.getContent())
                .userId(request.getUserId())
                .sendResult(false)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();
    }

    // DM에 보내는 메시지 생성 (슬랙 이메일만 검증)
    private Message createMessageForDM(ReqMessagePostDTO request, String slackEmail) {
        if (slackEmail == null || slackEmail.isEmpty()) {
            throw new ApiException(ErrorMessage.INVALID_INPUT);
        }

        return Message.builder()
                .content(request.getContent())
                .sendResult(false)
                .createdAt(LocalDateTime.now())
                .createdBy(slackEmail)  // Slack 이메일을 'createdBy'에 저장
                .build();
    }

    // 메세지 수정
    @Transactional
    public ResMessageDTO updateMessage(ReqMessageUpdateDTO request) {
        Message existingMessage = messageRepository.findById(request.getSlackMessageId())
                .orElseThrow(() -> new ApiException(ErrorMessage.MESSAGE_NOT_FOUND));

        existingMessage.updateContent(request.getContent());
        existingMessage.updateModifiedAt(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(existingMessage);

        return mapToResMessageDTO(updatedMessage);
    }

    // 메세지 삭제
    @Transactional
    public void deleteMessage(Long messageId, String deletedBy) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ApiException(ErrorMessage.MESSAGE_NOT_FOUND));

        if (deletedBy == null || deletedBy.isBlank()) {
            throw new ApiException(ErrorMessage.INVALID_INPUT, "DeletedBy 정보를 제공해야 합니다.");
        }

        message.markAsDeleted(deletedBy);
        messageRepository.save(message);
    }

    private ResMessageDTO mapToResMessageDTO(Message message) {
        return ResMessageDTO.builder()
                .slackMessageId(message.getSlackMessageId())
                .content(message.getContent())
                .sendResult(message.isSendResult())
                .createdAt(message.getCreatedAt())
                .build();
    }


}
