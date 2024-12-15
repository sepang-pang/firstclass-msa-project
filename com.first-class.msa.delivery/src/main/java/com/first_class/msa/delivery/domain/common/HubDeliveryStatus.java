package com.first_class.msa.delivery.domain.common;

public enum HubDeliveryStatus {
	WAITING_FOR_TRANSIT,  // 허브 이동 대기 중
	IN_TRANSIT,           // 허브 이동 중
	ARRIVED_AT_HUB,       // 목적지 허브 도착
}
