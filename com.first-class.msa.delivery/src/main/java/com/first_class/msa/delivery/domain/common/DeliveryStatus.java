package com.first_class.msa.delivery.domain.common;

public enum DeliveryStatus {
	READY,                 // 배송 준비 완료
	AT_FINAL_HUB,		   // 최종 허브 도착 완료
	IN_TRANSIT,            // 이동 중 (고객 배송 중)
	DELIVERED,             // 배송 완료
	CANCELLED,             // 배송 취소
	FAILED                 // 배송 실패

}
