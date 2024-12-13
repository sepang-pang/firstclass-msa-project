package com.first_class.msa.agent.presentation.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.agent.application.dto.ResDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.dto.ResGlobalDeliveryAgentDto;
import com.first_class.msa.agent.application.dto.ResHubDeliveryAgentDto;
import com.first_class.msa.agent.application.service.DeliveryAgentService;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agents")
public class AgentController {

	private final DeliveryAgentService deliveryAgentService;

	@PostMapping()
	public ResponseEntity<ResDTO<ResDeliveryAgentDTO>> postBy(
		@RequestHeader(name = "X-User-Id") Long userId,
		@RequestBody ReqDeliveryAgentDTO deliveryAgentDTO
	){
		return new ResponseEntity<>(
			ResDTO.<ResDeliveryAgentDTO>builder()
				.code(HttpStatus.CREATED.value())
				.message("배송 관리자 생성 성공")
				.data(deliveryAgentService.postBy(userId, deliveryAgentDTO))
				.build(),
			HttpStatus.CREATED
		);
	}

	@GetMapping()
	public ResponseEntity<ResDTO<ResDeliveryAgentSearchDTO>> getSearchBy(
		@RequestHeader(name = "X-User-Id") Long userId,
		@RequestParam(required = false) Long hubId,
		@RequestParam(required = false) Type type,
		@RequestParam(required = false) IsAvailable isAvailable,
		Pageable pageable
	){
		return new ResponseEntity<>(
			ResDTO.<ResDeliveryAgentSearchDTO>builder()
				.code(HttpStatus.OK.value())
				.message("배송 관리자 조회 성공")
				.data(deliveryAgentService.getSearchDeliveryAgentBy(userId, hubId, type, isAvailable, pageable))
				.build(),
			HttpStatus.OK
		);
	}


	@GetMapping("/{deliveryAgentId}")
	public ResponseEntity<ResDTO<ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO>> getSearchDetailBy(
		@RequestHeader(name = "X-User-Id") Long userId,
		@PathVariable(name = "deliveryAgentId") Long deliveryAgentId
	){
		return new ResponseEntity<>(
			ResDTO.<ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO>builder()
			.code(HttpStatus.OK.value())
				.message("배송 관리자 단건 조회 성공")
				.data(deliveryAgentService.getDeliveryAgentById(userId, deliveryAgentId))
				.build(),
			HttpStatus.OK
		);
	}

	@PostMapping("/external/global")
	public ResponseEntity<ResDTO<ResGlobalDeliveryAgentDto>> assignGlobalAgent(){
		return new ResponseEntity<>(ResDTO.<ResGlobalDeliveryAgentDto>builder()
			.code(HttpStatus.OK.value())
			.message("허브간 배송 관리자 요청 완료")
			.data(deliveryAgentService.assignGlobalDeliveryAgent())
			.build()
			,HttpStatus.OK
			);
	}
	@PostMapping("/external/hub/{hubId}")
	public ResponseEntity<ResDTO<ResHubDeliveryAgentDto>> assignHubAgent(
		@PathVariable Long hubId
	){
		return new ResponseEntity<>(ResDTO.<ResHubDeliveryAgentDto>builder()
			.code(HttpStatus.OK.value())
			.message("허브간 배송 관리자 요청 완료")
			.data(deliveryAgentService.assignHubDeliveryAgent(hubId))
			.build()
			,HttpStatus.OK
		);
	}



}
