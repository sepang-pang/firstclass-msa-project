package com.first_class.msa.agent.presentation.dto;

import com.first_class.msa.agent.domain.common.Type;

import lombok.Getter;

@Getter
public class ReqDeliveryAgentPostDTO {
	private Long userId;
	private Long hubId;
	private String slackId;
	private Type type;
}
