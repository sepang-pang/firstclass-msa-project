package com.first_class.msa.delivery.presentation.dto;

import com.first_class.msa.delivery.domain.common.HubDeliveryStatus;

import lombok.Getter;

@Getter
public class ReqHubDeliveryPutDTO {
	private HubDeliveryStatus hubDeliveryStatus;
}
