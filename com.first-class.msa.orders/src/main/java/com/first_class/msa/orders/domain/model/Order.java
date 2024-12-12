package com.first_class.msa.orders.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.first_class.msa.orders.domain.model.common.BaseTime;
import com.first_class.msa.orders.domain.model.valueobject.Address;
import com.first_class.msa.orders.domain.model.valueobject.OrderTotalPrice;
import com.first_class.msa.orders.domain.model.valueobject.RequestInfo;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_orders")
public class Order extends BaseTime {

	@Id
	@Tsid
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "business_id", nullable = false)
	private Long businessId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "hub_id", nullable = false)
	private Long hubId;

	@Embedded
	@Column(name = "address", nullable = false)
	private Address address;

	@Embedded
	@Column(name = "request_info", nullable = false)
	private RequestInfo requestInfo;

	@Embedded
	@Column(name = "order_total_price", nullable = false)
	private OrderTotalPrice orderTotalPrice;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OrderLine> orderLineList = new ArrayList<>();

	public static Order createOrder(Long businessId, Long hubId, Long userId, Address address,RequestInfo requestInfo) {
		return Order.builder()
			.businessId(businessId)
			.hubId(hubId)
			.userId(userId)
			.address(address)
			.requestInfo(requestInfo)
			.build();

	}

	public void setCreateByAndUpdateBy(Long userId){
		this.setCreatedBy(userId);
		this.setUpdatedBy(userId);
	}

	public void addOrderLineList(List<OrderLine> orderLineList) {
		this.orderLineList = orderLineList;
	}

	public void updateOrderTotalPrice(List<OrderLine> orderLineList) {
		this.orderTotalPrice =
			new OrderTotalPrice(
				orderLineList.stream()
					.mapToInt(orderLine -> orderLine.getCount().getValue() * orderLine.getSupplyPrice().getValue())
					.sum()
			);
	}

	public void deleteOrder(Long userId){
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);

		for (OrderLine orderLine: orderLineList) {
			orderLine.deleteOrderLine(userId);
		}
	}

}
