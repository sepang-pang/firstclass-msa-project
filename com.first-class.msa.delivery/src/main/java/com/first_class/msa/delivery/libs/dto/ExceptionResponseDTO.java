package com.first_class.msa.delivery.libs.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionResponseDTO<T> {

	private final HttpStatus status;
	private final String msg;
	private final T data;

	public static <T> ExceptionResponseDTO<T> of(HttpStatus status, String msg, T data) {
		return new ExceptionResponseDTO<>(status, msg, data);
	}
}
