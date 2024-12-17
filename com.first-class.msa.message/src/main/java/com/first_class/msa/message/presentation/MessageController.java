package com.first_class.msa.message.presentation;

import com.first_class.msa.message.application.dto.ReqMessageUpdateDTO;
import com.first_class.msa.message.application.service.AiMessageService;
import com.first_class.msa.message.application.service.MessageService;
import com.first_class.msa.message.application.service.SlackMessageService;
import com.first_class.msa.message.application.dto.ReqMessagePostDTO;
import com.first_class.msa.message.application.dto.ResMessageDTO;
import com.first_class.msa.message.application.dto.ResUserGetByIdDTO;
import com.first_class.msa.message.infrastructure.client.UserClient;
import com.first_class.msa.message.libs.dto.SuccessResponseDTO;
import com.first_class.msa.message.libs.exception.ApiException;
import com.first_class.msa.message.libs.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserClient userClient;
    private final SlackMessageService slackMessageService;
    private final AiMessageService aiMessageService;

    // 공용 채널에 메시지 전송
    @PostMapping("/send/{channelId}")
    public ResponseEntity<SuccessResponseDTO<ResMessageDTO>> createAndSendMessageToChannel(
            @PathVariable String channelId,
            @RequestBody ReqMessagePostDTO request
    ) {
        try {
            ResMessageDTO response = messageService.createAndSendMessageToChannel(request, channelId);
            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "메시지가 성공적으로 전송되었습니다.", response));
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    // DM에 메시지 전송
    @PostMapping("/sendDM")
    public ResponseEntity<SuccessResponseDTO<ResMessageDTO>> createAndSendMessageToDM(
            @RequestParam String slackEmail,
            @RequestBody ReqMessagePostDTO request
    ) {
        try {
            if (slackEmail == null || slackEmail.isEmpty()) {
                throw new ApiException(ErrorMessage.INVALID_INPUT);
            }

            ResMessageDTO response = messageService.createAndSendMessageToDM(request, slackEmail);
            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "DM 메시지가 성공적으로 전송되었습니다.", response));

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    // AI 메시지 생성 및 Slack에 전송
    @PostMapping("/generate-ai-message")
    public ResponseEntity<SuccessResponseDTO<String>> generateAndSendAiMessage(
            @RequestParam Long userId,
            @RequestParam Long orderId,
            @RequestParam Long departureHubId,
            @RequestParam Long arrivalHubId,
            @RequestParam String channelId
    ) {
        try {
            String aiMessage = aiMessageService.generateAiMessage(userId, orderId, departureHubId, arrivalHubId, channelId);

            ReqMessagePostDTO messageRequest = new ReqMessagePostDTO(aiMessage, userId, channelId);
            messageService.createAndSendMessageToChannel(messageRequest, channelId);

            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "AI 메시지가 성공적으로 생성되어 전송되었습니다.", "Success"));
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }


    // 워크스페이스 내 모든 메시지 동기화
    @PostMapping("/sync")
    public ResponseEntity<SuccessResponseDTO<String>> syncAllMessages(@RequestHeader("Authorization") String token) {
        try {
            slackMessageService.fetchAndSaveMessages(token);
            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "모든 워크스페이스 메시지가 성공적으로 동기화되었습니다.", "Success"));
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    // 특정 채널 메시지 동기화
    @PostMapping("/sync/{channelId}")
    public ResponseEntity<SuccessResponseDTO<String>> syncChannelMessages(
            @PathVariable String channelId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            slackMessageService.fetchAndSaveMessageFromChannel(channelId, token);
            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "채널 메시지가 성공적으로 동기화되었습니다.", "Success"));
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    // 메시지 수정
    @PutMapping("/update")
    public ResponseEntity<SuccessResponseDTO<ResMessageDTO>> updateMessage(
            @RequestBody ReqMessageUpdateDTO request
    ) {
        try {
            ResMessageDTO response = messageService.updateMessage(request);
            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "메시지가 성공적으로 수정되었습니다.", response));
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<SuccessResponseDTO<String>> deleteMessage(
            @PathVariable Long messageId,
            @RequestHeader("Deleted-By") String deletedBy
    ) {
        try {
            messageService.deleteMessage(messageId, deletedBy);
            return ResponseEntity.ok(new SuccessResponseDTO<>(200, "메시지가 성공적으로 삭제되었습니다.", "Success"));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorMessage.SERVER_ERROR);
        }
    }


}

