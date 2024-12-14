package com.first_class.msa.delivery.domain.valueobject;

import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

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
public class Address {
	@Column(name = "address", nullable = false)
	String value;

	public Address(String value){
		if(value == null || value.trim().isEmpty()){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.ADDRESS_EMPTY));
		}
		this.value = value;
	}
	@Override
	public String toString(){
		return value;
	}

}
