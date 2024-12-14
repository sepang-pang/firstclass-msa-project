package com.first_class.msa.delivery.domain.model;

import java.time.LocalDateTime;

import com.first_class.msa.delivery.domain.common.BusinessDeliveryStatus;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class BusinessDeliveryRoute extends BaseTime{
	@Id
	@Tsid
	@GeneratedValue
	private Long id;

	@OneToOne(mappedBy = "businessDeliveryRoute")
	private Delivery delivery;

	@Column(name = "departure_hub_id", nullable = false)
	private Long departureHubId;

	@Column(name = "expected_distance", nullable = false)
	private Double expectedDistance;

	@Column(name = "expected_time", nullable = false)
	private LocalDateTime expectedTime;

	@Column(name = "actual_distance")
	private Double actualDistance;

	@Column(name = "actual_time")
	private LocalDateTime actualTime;

	@Column(name = "business_delivery_status", nullable = false)
	private BusinessDeliveryStatus businessDeliveryStatus;



}
