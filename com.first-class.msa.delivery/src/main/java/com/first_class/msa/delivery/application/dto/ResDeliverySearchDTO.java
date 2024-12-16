package com.first_class.msa.delivery.application.dto;

import java.util.List;

import com.first_class.msa.delivery.domain.model.BusinessDeliveryRoute;
import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;
import com.first_class.msa.delivery.domain.valueobject.Address;
import com.first_class.msa.delivery.domain.valueobject.Sequence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResDeliverySearchDTO {
	private DeliveryDTO deliveryDTO;

	public static ResDeliverySearchDTO from(Delivery delivery){
		return ResDeliverySearchDTO.builder()
			.deliveryDTO(DeliveryDTO.from(delivery))
			.build();
	}


	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DeliveryDTO{
		private BusinessDeliveryRouteDTO businessDeliveryRouteDTO;
		private HubDeliveryRouteDTOList hubDeliveryRouteListDTO;

		public static DeliveryDTO from(Delivery delivery){
			return DeliveryDTO.builder()
				.businessDeliveryRouteDTO(BusinessDeliveryRouteDTO.from(delivery.getBusinessDeliveryRoute()))
				.hubDeliveryRouteListDTO(HubDeliveryRouteDTOList.from(delivery.getHubDeliveryRouteList()))
				.build();
		}


		@Getter
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		public static class BusinessDeliveryRouteDTO{
			private Long departureHubId;
			private Long deliveryBusinessId;
			private Address address;
			private Long deliveryAgentId;

			public static BusinessDeliveryRouteDTO from(BusinessDeliveryRoute businessDeliveryRoute){
				return BusinessDeliveryRouteDTO.builder()
					.departureHubId(businessDeliveryRoute.getDepartureHubId())
					.deliveryBusinessId(businessDeliveryRoute.getDeliveryBusinessId())
					.address(businessDeliveryRoute.getAddress())
					.deliveryAgentId(businessDeliveryRoute.getDeliveryAgentId())
					.build();
			}
		}

		@Getter
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		public static class HubDeliveryRouteDTOList{
			private List<HubDeliveryRouteDTO> hubDeliveryRouteList;

			public static HubDeliveryRouteDTOList from(List<HubDeliveryRoute> hubDeliveryRouteList){
				return HubDeliveryRouteDTOList.builder()
					.hubDeliveryRouteList(
						hubDeliveryRouteList.stream()
						.map(HubDeliveryRouteDTO::from)
							.toList()
					)
					.build();
			}
			@Getter
			@Builder
			@AllArgsConstructor
			@NoArgsConstructor
			public static class HubDeliveryRouteDTO{
				private Long departureHubId;
				private Long arrivalHubId;
				private Sequence sequence;
				private Long hubAgentId;

				public static HubDeliveryRouteDTO from(HubDeliveryRoute hubDeliveryRoute){
					return HubDeliveryRouteDTO.builder()
						.departureHubId(hubDeliveryRoute.getDepartureHubId())
						.arrivalHubId(hubDeliveryRoute.getArrivalHubId())
						.sequence(hubDeliveryRoute.getSequence())
						.hubAgentId(hubDeliveryRoute.getHubAgentId())
						.build();
				}
			}

		}
	}

}
