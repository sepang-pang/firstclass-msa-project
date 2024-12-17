package com.first_class.msa.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqGeminiDTO {

    private String reqInfo;
    private List<ProductInfo> products;
    private List<TransitHubInfo> transitHubInfos;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInfo {
        private Long productId;
        private int count;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransitHubInfo {
        private String departureHubName;
        private String arrivalHubName;
        private Long transitTime;
        private double distance;
    }
}
