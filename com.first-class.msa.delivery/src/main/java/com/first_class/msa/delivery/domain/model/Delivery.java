package com.first_class.msa.delivery.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.first_class.msa.delivery.domain.common.DeliveryStatus;
import com.first_class.msa.delivery.domain.valueobject.Address;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "p_delivers")
public class Delivery extends BaseTime{
	@Id
	@Tsid
	@GeneratedValue
	private Long id;

	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Column(name = "departure_hub_id", nullable = false)
	private Long departureHubId;

	@Column(name = "arrival_hu_id", nullable = false)
	private Long arrivalHubId;

	@Column(name = "delivery_business_id", nullable = false)
	private Long deliveryBusinessId;

	@Column(name = "delivery_agent_id")
	private Long deliveryAgentId;

	@Embedded
	private Address address;

	@Column(name = "delivery_status", nullable = false)
	private DeliveryStatus deliveryStatus;

	@OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HubDeliveryRoute> hubDeliveryRouteList = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "business_delivery_route_id")
	private BusinessDeliveryRoute businessDeliveryRoute;

	public static Delivery createDelivery(
		Long orderId,
		Long departureHubId,
		Long arrivalHubId,
		Long deliveryBusinessId,
		Address address
	){
		return Delivery.builder()
			.orderId(orderId)
			.departureHubId(departureHubId)
			.arrivalHubId(arrivalHubId)
			.deliveryBusinessId(deliveryBusinessId)
			.address(address)
			.deliveryStatus(DeliveryStatus.READY)
			.build();

	}

	public void updateHubDeliveryRouteList(List<HubDeliveryRoute> hubDeliveryRouteList){
		this.hubDeliveryRouteList = hubDeliveryRouteList;
	}




}
