package com.first_class.msa.agent.presentation.dto;

import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;

import lombok.Getter;

@Getter
public class ReqDeliveryAgentPutDTO {
	private IsAvailable isAvailable;
	private String slackId;
	private Type type;
}
