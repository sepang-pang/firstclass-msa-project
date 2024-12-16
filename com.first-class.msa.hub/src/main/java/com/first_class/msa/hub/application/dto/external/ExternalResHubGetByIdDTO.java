package com.first_class.msa.hub.application.dto.external;

import com.first_class.msa.hub.domain.model.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalResHubGetByIdDTO {

    private double latitude;
    private double longitude;

    public static ExternalResHubGetByIdDTO of(Hub hub) {
        return ExternalResHubGetByIdDTO.builder()
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .build();
    }
}
