package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.valueobject.RequestInfo;
import com.first_class.msa.orders.domain.repository.OrderRepository;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
	private final OrderRepository orderRepository;
	private final OrderLineService orderLineService;
	private final OrderEventService orderEventService;
	private final HubService hubService;
	private final BusinessService businessService;
	
	@Override
	@Transactional
	public ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO){

		RequestInfo requestInfo = new RequestInfo(reqOrderPostDTO.getRequestInfo());
		Order order = Order.createOrder(businessId, userId,requestInfo);
		List<OrderLine> orderLineList
			= orderLineService.createOrderLineList(reqOrderPostDTO.getReqOrderLinePostDTOList(), order);

		order.addOrderLineList(orderLineList);
		order.updateOrderTotalPrice(orderLineList);
		order = orderRepository.save(order);

		ResOrderPostDTO orderPostDTO = ResOrderPostDTO.of(order);
		orderEventService.orderCreateEvent(order.getId(), userId, orderPostDTO.getOrderDTO().getOrderLineList());


		return orderPostDTO;
	}

	// TODO: 2024-12-11 권한에 따라서 수정 예정 
	@Override
	public ResOrderSearchDTO getAllOrderBy(Long userId, String userRole, Pageable pageable) {
		if(userRole.equals("MASTER")){
			orderRepository.findAll(userId, pageable);

		} else if(userRole.equals("HUB_MANAGER")){
			// TODO: 2024-12-11 허브에 UserId 확인 및 서치 추가
			orderRepository.findAll(userId, pageable);

		} else if(userRole.equals("BUSINESS_MANAGER")) {
			// TODO: 2024-12-11 업체에 userId 확인 및 서치
			orderRepository.findAll(userId, pageable);

		} else {
			orderRepository.findAll(userId, pageable);
		}

		return orderRepository.findAll(userId, pageable);
	}

}
