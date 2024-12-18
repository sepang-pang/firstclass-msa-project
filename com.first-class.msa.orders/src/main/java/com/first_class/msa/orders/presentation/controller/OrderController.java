package com.first_class.msa.orders.presentation.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.application.service.OrderService;
import com.first_class.msa.orders.libs.dto.SuccessResponseDTO;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/business/{businessId}")
	public ResponseEntity<SuccessResponseDTO<ResOrderDTO>> postBy(
		@PathVariable Long businessId,
		@RequestHeader("X-User-Id") Long userId,
		@RequestBody ReqOrderPostDTO reqOrderPostDTO)
	{
		return new ResponseEntity<>(
			SuccessResponseDTO.<ResOrderDTO>builder()
				.code(HttpStatus.CREATED.value())
				.message("주문 생성 성공")
				.data(orderService.postBy(businessId, userId, reqOrderPostDTO))
				.build(),
			HttpStatus.CREATED
		);

	}

	@GetMapping
	public ResponseEntity<SuccessResponseDTO<ResOrderSearchDTO>> getAllOrderBy(
		@RequestHeader("X-User-Id") Long userId,
		Pageable pageable
	){
		return new ResponseEntity<>(
			SuccessResponseDTO.<ResOrderSearchDTO>builder()
				.code(HttpStatus.OK.value())
				.message("주문 조회 성공")
				.data(orderService.getAllOrderBy(userId, pageable))
				.build(),
			HttpStatus.OK
		);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<SuccessResponseDTO<ResOrderDTO>> getOrderDetailBy(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long orderId
	){
		return new ResponseEntity<>(
			SuccessResponseDTO.<ResOrderDTO>builder()
				.code(HttpStatus.OK.value())
				.message("주문 상세 조회 성공")
				.data(orderService.getOrderDetailBy(userId ,orderId))
				.build(),
			HttpStatus.OK
		);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<SuccessResponseDTO<Void>> deleteBy(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long orderId
	){
		orderService.deleteBy(userId ,orderId);
		return new ResponseEntity<>(
			SuccessResponseDTO.<Void>builder()
				.code(HttpStatus.OK.value())
				.message("주문 삭제 성공")
				.build(),
			HttpStatus.OK
		);
	}

}
