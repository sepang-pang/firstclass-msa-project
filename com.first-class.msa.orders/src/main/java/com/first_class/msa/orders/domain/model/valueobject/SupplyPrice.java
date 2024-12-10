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
