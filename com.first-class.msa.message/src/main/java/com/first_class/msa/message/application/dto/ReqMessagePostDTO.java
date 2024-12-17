package com.first_class.msa.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqMessagePostDTO {

    private String content;
    private Long userId;
    private String SlackId;
    private String username;
    private String slackEmail;

    public ReqMessagePostDTO(String content, Long userId, String slackId) {
        this.content = content;
        this.userId = userId;
        this.SlackId = slackId;
        this.username = null;
    }
}
