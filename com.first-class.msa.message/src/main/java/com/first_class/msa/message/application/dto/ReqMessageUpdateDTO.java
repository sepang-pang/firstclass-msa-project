package com.first_class.msa.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqMessageUpdateDTO {

    private Long slackMessageId;
    private String content;
}
