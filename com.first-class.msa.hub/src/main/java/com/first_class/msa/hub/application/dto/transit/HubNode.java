package com.first_class.msa.hub.application.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubNode {
    private Long hubId;
    private double distance;
}
