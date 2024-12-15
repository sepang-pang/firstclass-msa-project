package com.first_class.msa.delivery.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverRouteInfoDTO {
	private long distance; // 거리 (미터)
	private long duration; // 시간 (초)
}
