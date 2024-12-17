package com.first_class.msa.message.libs.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 발생"),
	SLACK_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 API 오류"),
	MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"메세지를 찾을 수 없습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 데이터를 찾을 수 없습니다."),
	HUB_TRANSIT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 정보를 찾을 수 없습니다."),
	AI_DATA_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 데이터 생성에 실패했습니다.");



	private final HttpStatus httpStatus;
	private final String message;
}
