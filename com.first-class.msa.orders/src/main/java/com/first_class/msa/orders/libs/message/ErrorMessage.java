package com.first_class.msa.orders.libs.message;

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
	/**
	 * Order
	 */
	NOT_FOUND_ORDER(HttpStatus.BAD_REQUEST, "해당하는 주문을 찾을 수 없습니다."),
	REQUEST_INFO_REQUEST_EMPTY(HttpStatus.BAD_REQUEST, "요청사항은 필수입니다."),
	ORDER_TOTAL_PRICE_NOT_MINUS(HttpStatus.BAD_REQUEST, "수량은 음수일 수 없습니다."),
	ORDER_TOTAL_PRICE_EMPTY(HttpStatus.BAD_REQUEST, "총 가격이 비어있습니다."),
	INSUFFICIENT_COUNT(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
	/**
	 * Product
	 */
	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
	/**
	 * OrderLine
	 */
	SUPPLY_PRICE_NOT_MINUS(HttpStatus.BAD_REQUEST, "수량은 음수일 수 없습니다."),
	SUPPLY_PRICE_EMPTY(HttpStatus.BAD_REQUEST, "총 가격이 비어있습니다."),
	PRODUCT_COUNT_EMPTY(HttpStatus.BAD_REQUEST, "수량이 비어있습니다."),
	PRODUCT_COUNT_NOT_MINUS(HttpStatus.BAD_REQUEST, "수량은 음수일 수 없습니다.");



	private final HttpStatus httpStatus;
	private final String message;
}
