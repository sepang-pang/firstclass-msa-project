package com.first_class.msa.message.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResOrderGetDTO {

    private OrderDTO orderDTO;

    @Getter
    @NoArgsConstructor
    public static class OrderDTO {

        private String reqInfo;
        private List<OrderLineDTO> orderLineDTOList;

        @Getter
        @NoArgsConstructor
        public static class OrderLineDTO {

            private int count;
            private Long productId;

        }
    }
}

