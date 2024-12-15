package com.first_class.msa.delivery.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.delivery.application.service.DeliveryService;
import com.first_class.msa.delivery.libs.dto.SuccessResponseDTO;
import com.first_class.msa.delivery.presentation.dto.ReqBusinessDeliveryPutDTO;
import com.first_class.msa.delivery.presentation.dto.ReqHubDeliveryPutDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {
	private final DeliveryService deliveryService;

	@PutMapping("/{deliveryId}/hub/{hubDeliveryRouteId}")
	public ResponseEntity<SuccessResponseDTO<Void>> HubStatusRoutePutBy(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long deliveryId,
		@PathVariable Long hubDeliveryRouteId,
		@RequestBody ReqHubDeliveryPutDTO reqHubDeliveryPutDTO
	) {
		deliveryService.hubRouteStatusPutBy(
			userId,
			deliveryId,
			hubDeliveryRouteId,
			reqHubDeliveryPutDTO
		);
		return new ResponseEntity<>(SuccessResponseDTO.<Void>builder()
			.code(HttpStatus.OK.value())
			.message("허브 배송 상태 변경 성공")
			.build(),
			HttpStatus.OK
		);
	}

	@PutMapping("/{deliveryId}/hub/{businessDeliveryRouteId}")
	public ResponseEntity<SuccessResponseDTO<Void>> businessDeliveryStatusPutBy(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long deliveryId,
		@PathVariable Long businessDeliveryRouteId,
		@RequestBody ReqBusinessDeliveryPutDTO reqBusinessDeliveryPutDTO
	) {
		deliveryService.businessDeliveryStatusPutBy(
			userId,
			deliveryId,
			businessDeliveryRouteId,
			reqBusinessDeliveryPutDTO
		);
		return new ResponseEntity<>(SuccessResponseDTO.<Void>builder()
			.code(HttpStatus.OK.value())
			.message("허브 배송 상태 변경 성공")
			.build(),
			HttpStatus.OK
		);
	}

}
