package com.first_class.msa.delivery.libs.message;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

	/**
	 * User
	 */
	INVALID_USER_ROLE(HttpStatus.FORBIDDEN, "해당 유저는 접근이 불가한 서비스입니다."),
	INVALID_USER_ROLE_HUB_MANAGER(HttpStatus.FORBIDDEN, "허브 매니저 권한 인증에 실패했습니다."),
	INVALID_USER_ROLE_BUSINESS_MANAGER(HttpStatus.FORBIDDEN, "업체 매니저 권한 인증에 실패했습니다."),
	INVALID_USER_ROLE_DELIVERY_MANAGER(HttpStatus.FORBIDDEN, "배송 매니저 권한 인증에 실패했습니다."),

	/**
	 * Delivery
	 */
	ADDRESS_EMPTY(HttpStatus.BAD_REQUEST, "총 가격이 비어있습니다."),
	SEQUENCE_NOT_MINUS(HttpStatus.BAD_REQUEST, "시퀀수는 음수일 수 없습니다.");




	private final HttpStatus httpStatus;
	private final String message;
}
