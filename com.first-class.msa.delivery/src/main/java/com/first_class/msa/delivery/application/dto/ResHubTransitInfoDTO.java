package com.first_class.msa.delivery.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class ResHubTransitInfoDTO {
	private HubTransitInfoList hubTransitInfo;

	@Getter
	public static class HubTransitInfoList {
		private List<HubTransitInfo> hubTransitInfoLIst;

		@Getter
		public static class HubTransitInfo {
			private Long departureHubId;
			private Long ArrivalHubId;
			private LocalDateTime transitTime;
			private Double distance;
		}
	}


}
