package com.first_class.msa.orders.domain.model.valueobject;

import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderTotalPrice {

	@Column(name = "order_total_price", nullable = false)
	Integer value;

	public OrderTotalPrice(Integer value){
		if(value == null){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.ORDER_TOTAL_PRICE_EMPTY));
		}
		if(value < 0){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.ORDER_TOTAL_PRICE_NOT_MINUS));
		}
		this.value = value;

	}
}
