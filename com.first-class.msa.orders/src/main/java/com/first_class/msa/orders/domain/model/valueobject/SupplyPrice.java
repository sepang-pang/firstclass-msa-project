package com.first_class.msa.orders.domain.model.valueobject;

import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;

import jakarta.persistence.Column;

public class SupplyPrice {
	@Column(name = "supply_price", nullable = false)
	Integer value;

	public SupplyPrice(Integer value){
		if(value == null){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.REQUEST_INFO_REQUEST_EMPTY));
		}
		if(value < 0){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.REQUEST_INFO_REQUEST_EMPTY));
		}
		this.value = value;
	}
}
