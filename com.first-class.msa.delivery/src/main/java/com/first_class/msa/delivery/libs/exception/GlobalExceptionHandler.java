package com.first_class.msa.delivery.libs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.first_class.msa.delivery.libs.dto.ExceptionResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
		log.error("RuntimeException: ", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ExceptionResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러", null));
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ExceptionResponseDTO<Void>> customExceptionHandler(ApiException e) {
		log.error("CustomException: "+ e);
		return ResponseEntity.status(e.getErrorMessage().getHttpStatus()).body(
			ExceptionResponseDTO.of(
				e.getErrorMessage().getHttpStatus(),
				e.getErrorMessage().getMessage(),
				null
			)
		);
	}
}
