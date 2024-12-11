package com.first_class.msa.orders.libs.exception;

import com.first_class.msa.orders.libs.message.ErrorMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{

	private final ErrorMessage errorMessage;
	private final String message;
	public ApiException(ErrorMessage error){
		this.errorMessage = error;
		this.message = error.getMessage();
	}
}
