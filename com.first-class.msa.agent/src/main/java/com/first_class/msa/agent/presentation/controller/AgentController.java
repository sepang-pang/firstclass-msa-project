package com.first_class.msa.agent.presentation.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.service.DeliveryAgentService;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.libs.dto.SuccessResponseDTO;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentPostDTO;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentPutDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agents")
public class AgentController {

	private final DeliveryAgentService deliveryAgentService;

	@PostMapping()
	public ResponseEntity<SuccessResponseDTO<ResDeliveryAgentDTO>> postBy(
		@RequestHeader(name = "X-User-Id") Long userId,
		@RequestBody ReqDeliveryAgentPostDTO deliveryAgentDTO
	) {
		return new ResponseEntity<>(
			SuccessResponseDTO.<ResDeliveryAgentDTO>builder()
				.code(HttpStatus.CREATED.value())
				.message("배송 관리자 생성 성공")
				.data(deliveryAgentService.postBy(userId, deliveryAgentDTO))
				.build(),
			HttpStatus.CREATED
		);
	}

	@GetMapping()
	public ResponseEntity<SuccessResponseDTO<ResDeliveryAgentSearchDTO>> getSearchBy(
		@RequestHeader(name = "X-User-Id") Long userId,
		@RequestParam(required = false) Long hubId,
		@RequestParam(required = false) Type type,
		@RequestParam(required = false) IsAvailable isAvailable,
		Pageable pageable
	) {
		return new ResponseEntity<>(
			SuccessResponseDTO.<ResDeliveryAgentSearchDTO>builder()
				.code(HttpStatus.OK.value())
				.message("배송 관리자 조회 성공")
				.data(deliveryAgentService.getSearchDeliveryAgentBy(userId, hubId, type, isAvailable, pageable))
				.build(),
			HttpStatus.OK
		);
	}

	@GetMapping("/{deliveryAgentId}")
	public ResponseEntity<SuccessResponseDTO<ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO>> getSearchDetailBy(
		@RequestHeader(name = "X-User-Id") Long userId,
		@PathVariable(name = "deliveryAgentId") Long deliveryAgentId
	) {
		return new ResponseEntity<>(
			SuccessResponseDTO.<ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO>builder()
				.code(HttpStatus.OK.value())
				.message("배송 관리자 단건 조회 성공")
				.data(deliveryAgentService.getDeliveryAgentById(userId, deliveryAgentId))
				.build(),
			HttpStatus.OK
		);
	}

	@PutMapping("/{deliveryAgentId}")
	public ResponseEntity<SuccessResponseDTO<Void>> putBy(
		@PathVariable(name = "deliveryAgentId") Long deliveryAgentId,
		@RequestHeader(name = "X-User-Id") Long userId,
		@RequestBody ReqDeliveryAgentPutDTO reqDeliveryAgentPutDTO
	) {
		deliveryAgentService.putBy(userId, deliveryAgentId, reqDeliveryAgentPutDTO);
		return new ResponseEntity<>(SuccessResponseDTO.<Void>builder()
			.code(HttpStatus.OK.value())
			.message("배송 담당자 정보 수정 성공")
			.build(),
			HttpStatus.OK
		);
	}

	@DeleteMapping("/{deliveryAgentId}")
	public ResponseEntity<SuccessResponseDTO<Void>> deleteBy(
		@PathVariable(name = "deliveryAgentId") Long deliveryAgentId,
		@RequestHeader(name = "X-User-Id") Long userId
	) {
		deliveryAgentService.deleteBy(userId, deliveryAgentId);
		return new ResponseEntity<>(SuccessResponseDTO.<Void>builder()
			.code(HttpStatus.OK.value())
			.message("배송 담당자 정보 삭제 성공")
			.build(),
			HttpStatus.OK
		);
	}



}
