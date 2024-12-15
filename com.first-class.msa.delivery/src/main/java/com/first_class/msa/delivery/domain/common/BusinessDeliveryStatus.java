package com.first_class.msa.delivery.domain.common;

public enum BusinessDeliveryStatus {
	READY,        // 최종 허브에 도착 하여 배송 대기중
	OUT_FOR_DELIVERY,    // 업체 배송 중
	DELIVERED,           // 업체 배송 완료

}
