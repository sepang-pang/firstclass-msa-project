package com.first_class.msa.delivery.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.first_class.msa.delivery.domain.common.BusinessDeliveryStatus;
import com.first_class.msa.delivery.domain.common.DeliveryStatus;
import com.first_class.msa.delivery.domain.common.HubDeliveryStatus;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_delivery")
public class Delivery extends BaseTime {
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

	@Column(name = "delivery_agent_id")
	private Long deliveryAgentId;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_status", nullable = false)
	private DeliveryStatus deliveryStatus;

	@OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HubDeliveryRoute> hubDeliveryRouteList = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "business_delivery_route_id")
	private BusinessDeliveryRoute businessDeliveryRoute;

	public static Delivery createDelivery(
		Long orderId,
		Long departureHubId,
		Long arrivalHubId
	) {
		return Delivery.builder()
			.orderId(orderId)
			.departureHubId(departureHubId)
			.arrivalHubId(arrivalHubId)
			.deliveryStatus(DeliveryStatus.READY)
			.build();
	}

	public void addCreatedBy(Long userId) {
		this.setCreatedBy(userId);
	}
	public void addUpdatedBy(Long userId){
		this.setUpdatedBy(userId);
	}

	public void addHubDeliveryRouteList(List<HubDeliveryRoute> hubDeliveryRouteList) {
		this.hubDeliveryRouteList = hubDeliveryRouteList;
	}

	public void addBusinessDeliveryRoute(BusinessDeliveryRoute businessDeliveryRoute) {
		this.businessDeliveryRoute = businessDeliveryRoute;
	}

	public boolean updateHubDeliveryRouteState(Long hubDeliveryRouteId, HubDeliveryStatus newStatus) {
		HubDeliveryRoute hubRoute = findHubDeliveryRoute(hubDeliveryRouteId);

		hubRoute.updateHubDeliveryStatus(newStatus);

		if (isLastHub(hubRoute, newStatus)) {
			updateDeliveryStatus(DeliveryStatus.AT_FINAL_HUB);
			return true;
		}
		return false;
	}

	private HubDeliveryRoute findHubDeliveryRoute(Long hubDeliveryRouteId) {
		return hubDeliveryRouteList.stream()
			.filter(route -> route.getId().equals(hubDeliveryRouteId))
			.findFirst()
			.orElseThrow(
				() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_HUB_DELIVERY_ROUTE))
			);
	}

	private boolean isLastHub(HubDeliveryRoute hubRoute, HubDeliveryStatus newStatus) {
		return hubRoute.getArrivalHubId().equals(this.arrivalHubId) && newStatus == HubDeliveryStatus.ARRIVED_AT_HUB;
	}

	private void updateDeliveryStatus(DeliveryStatus newStatus) {
		this.deliveryStatus = newStatus;
	}

	public void updateBusinessDeliveryRouteStatus(
		Long businessDeliveryRouteId,
		BusinessDeliveryStatus businessDeliveryStatus
	) {
		if (!this.businessDeliveryRoute.getId().equals(businessDeliveryRouteId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_HUB_DELIVERY_ROUTE));
		}
		this.businessDeliveryRoute.updateBusinessDeliveryStatus(businessDeliveryStatus);
		if(isDelivery(this.businessDeliveryRoute, BusinessDeliveryStatus.DELIVERED)){
			updateDeliveryStatus(DeliveryStatus.DELIVERED);
		}
	}

	private boolean isDelivery(BusinessDeliveryRoute businessDeliveryRoute, BusinessDeliveryStatus newStatus){
		return businessDeliveryRoute.getBusinessDeliveryStatus().equals(newStatus);
	}

}





