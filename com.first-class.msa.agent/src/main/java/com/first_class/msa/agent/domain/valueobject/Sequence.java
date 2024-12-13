package com.first_class.msa.agent.domain.valueobject;

import com.first_class.msa.agent.libs.exception.ApiException;
import com.first_class.msa.agent.libs.message.ErrorMessage;

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
public class Sequence {

	@Column(name = "sequence")
	int value = 0;

	public Sequence(Integer value){
		if(value < 0){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.SEQUENCE_NOT_MINUS));
		}
		this.value = value;
	}
}
