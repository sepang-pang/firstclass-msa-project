package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.application.dto.ResBusinessDTO;
import com.first_class.msa.orders.application.dto.ResHubDto;
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
	private final AuthService authService;
	
	@Override
	@Transactional
	public ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO){

		RequestInfo requestInfo = new RequestInfo(reqOrderPostDTO.getRequestInfo());
		ResBusinessDTO resbusinessDTO = businessService.getBusinessBy(businessId);
		Order order = Order.createOrder(businessId, resbusinessDTO.getHubId(), userId, requestInfo);
		order.setCreateByAndUpdateBy(userId);

		List<OrderLine> orderLineList
			= orderLineService.createOrderLineList(reqOrderPostDTO.getReqOrderLinePostDTOList(), order);


		order.addOrderLineList(orderLineList);
		order.updateOrderTotalPrice(orderLineList);
		order = orderRepository.save(order);

		ResOrderPostDTO orderPostDTO = ResOrderPostDTO.of(order);
		orderEventService.orderCreateEvent(order.getId(), userId, orderPostDTO.getOrderDTO().getOrderLineList());


		return orderPostDTO;
	}

	@Override
	public ResOrderSearchDTO getAllOrderBy(Long userId, Pageable pageable) {
		return orderRepository.findAll(SearchCondition(userId), pageable);
	}

	private AuthSearchConditionDTO SearchCondition(Long userId){
		AuthSearchConditionDTO authSearchConditionDTO;
		if(authService.getRoleBy(userId).getRole().equals("MASTER")){
			authSearchConditionDTO = AuthSearchConditionDTO.createForMaster();

		} else if(authService.getRoleBy(userId).getRole().equals("HUB_MANAGER")){
			ResHubDto resHubDto = hubService.getHubBy(userId);
			authSearchConditionDTO = AuthSearchConditionDTO.createForHubManager(resHubDto.getHubId());

		} else if(authService.getRoleBy(userId).getRole().equals("BUSINESS_MANAGER")) {
			ResBusinessDTO businessDTO = businessService.getBusinessUserBy(userId);
			authSearchConditionDTO = AuthSearchConditionDTO.createForBusinessManager(businessDTO.getBusinessId());
		} else {
			authSearchConditionDTO = AuthSearchConditionDTO.createForDefault(userId);
		}
		return authSearchConditionDTO;
	}


}
