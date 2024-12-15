package com.first_class.msa.delivery.application.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class ResHubTransitInfoGetDTO {
	private List<HubTransitInfoDTO> hubTransitInfoDTOList;

	@Getter
	public static class HubTransitInfoDTO {

		private Long departureHubId;
		private Long arrivalHubId;
		private Long transitTime;
		private double distance;

	}


}
