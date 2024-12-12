package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.application.dto.ResBusinessDTO;
import com.first_class.msa.orders.application.dto.ResDeliveryDTO;
import com.first_class.msa.orders.application.dto.ResHubDto;
import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDetailDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.valueobject.RequestInfo;
import com.first_class.msa.orders.domain.repository.OrderRepository;
import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final OrderLineService orderLineService;
	private final OrderEventService orderEventService;
	private final HubService hubService;
	private final BusinessService businessService;
	private final AuthService authService;
	private final DeliveryService deliveryService;

	@Override
	@Transactional
	public ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO) {

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
	@Transactional(readOnly = true)
	public ResOrderSearchDTO getAllOrderBy(Long userId, Pageable pageable) {
		String userRole = authService.getRoleBy(userId).getRole();
		return orderRepository.findAll(SearchCondition(userId, userRole), pageable);
	}

	private AuthSearchConditionDTO SearchCondition(Long userId, String userRole) {

		return switch (userRole) {
			case "MASTER" -> AuthSearchConditionDTO.createForMaster();
			case "HUB_MANAGER" -> {
				ResHubDTO resHubDto = hubService.getHubBy(userId);
				yield AuthSearchConditionDTO.createForHubManager(resHubDto.getHubId());
			}
			case "BUSINESS_MANAGER" -> {
				ResBusinessDTO businessDTO = businessService.getBusinessUserBy(userId);
				yield AuthSearchConditionDTO.createForBusinessManager(businessDTO.getBusinessId());
			}
			case "DELIVERY_MANAGER" -> {
				ResDeliveryOrderSearchDTO resDeliveryOrderSearchDTO = deliveryService.getAllDeliveryBy(userId);
				yield AuthSearchConditionDTO.createForDeliveryManager(
					resDeliveryOrderSearchDTO.getOrderIdList());
			}
			default -> throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		};
	}


}
