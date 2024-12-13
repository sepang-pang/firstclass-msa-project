package com.first_class.msa.agent.libs.message;

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
	 * AGENT
	 */
	NOT_FOUND_DELIVERY_AGENT(HttpStatus.BAD_REQUEST, "배송 관리자 정보가 없습니다."),
	SEQUENCE_NOT_MINUS(HttpStatus.BAD_REQUEST, "배송 순번은 음수일 수 없습니다."),

	/**
	 * HUb
	 */
	NOT_FOUND_HUB(HttpStatus.BAD_REQUEST, "허브 정보를 찾을 수 없습니다."),
	HUB_MANAGER_MUST_HUB_ID(HttpStatus.BAD_REQUEST, "허브 매니저는 허브아이디가 필수입니다.");


	private final HttpStatus httpStatus;
	private final String message;
}
