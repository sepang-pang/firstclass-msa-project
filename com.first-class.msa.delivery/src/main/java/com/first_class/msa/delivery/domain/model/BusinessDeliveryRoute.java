package com.first_class.msa.delivery.domain.model;

import java.time.LocalDateTime;

import com.first_class.msa.delivery.domain.common.BusinessDeliveryStatus;
import com.first_class.msa.delivery.domain.valueobject.Address;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "p_business_delivery_route")
public class BusinessDeliveryRoute extends BaseTime {
	@Id
	@Tsid
	@GeneratedValue
	private Long id;

	@Column(name = "departure_hub_id", nullable = false)
	private Long departureHubId;

	@Column(name = "delivery_business_id", nullable = false)
	private Long deliveryBusinessId;

	@Embedded
	private Address address;

	@Column(name = "expected_distance")
	private Long expectedDistance;

	@Column(name = "expected_time")
	private Long expectedTime;

	@Column(name = "actual_distance")
	private Long actualDistance;

	@Column(name = "actual_time")
	private Long actualTime;

	@Column(name = "delivery_agent_id")
	private Long deliveryAgentId;

	@Enumerated(EnumType.STRING)
	@Column(name = "business_delivery_status", nullable = false)
	private BusinessDeliveryStatus businessDeliveryStatus;

	public static BusinessDeliveryRoute createBusinessDeliveryRoute(
		Long departureHubId,
		Long deliveryBusinessId,
		Address address
	) {
		return BusinessDeliveryRoute.builder()
			.departureHubId(departureHubId)
			.deliveryBusinessId(deliveryBusinessId)
			.address(address)
			.build();
	}

	public void assignDeliveryAgentId(Long deliveryAgentId) {
		this.deliveryAgentId = deliveryAgentId;
	}

	public void updateExpectedTimeAndDistance(Long expectedTime, Long expectedDistance) {
		this.expectedTime = expectedTime;
		this.expectedDistance = expectedDistance;

	}

	public void updateBusinessDeliveryStatus(BusinessDeliveryStatus deliveryStatus) {
		validateStatusTransition(deliveryStatus);
		this.businessDeliveryStatus = deliveryStatus;
	}

	private void validateStatusTransition(BusinessDeliveryStatus newStatus) {
		if (this.businessDeliveryStatus == BusinessDeliveryStatus.READY
			&& newStatus != BusinessDeliveryStatus.OUT_FOR_DELIVERY
		) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_BUSINESS_STATUS));
		}
		if (this.businessDeliveryStatus == BusinessDeliveryStatus.OUT_FOR_DELIVERY
			&& newStatus != BusinessDeliveryStatus.DELIVERED
		) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_BUSINESS_STATUS));
		}
	}

	public void setBusinessDeliveryRoute(Long userId){
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);
	}

}
