package com.first_class.msa.delivery.presentation.dto;

import com.first_class.msa.delivery.domain.common.BusinessDeliveryStatus;

import lombok.Getter;

@Getter
public class ReqBusinessDeliveryPutDTO {
	private BusinessDeliveryStatus businessDeliveryStatus;
}
