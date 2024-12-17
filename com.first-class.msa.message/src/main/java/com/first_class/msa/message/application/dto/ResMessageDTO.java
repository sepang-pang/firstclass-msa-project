package com.first_class.msa.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResMessageDTO {

    private Long slackMessageId;
    private String content;
    private boolean sendResult;
    private LocalDateTime createdAt;
}
