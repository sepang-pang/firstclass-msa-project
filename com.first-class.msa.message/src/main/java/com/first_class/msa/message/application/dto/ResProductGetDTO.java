package com.first_class.msa.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductGetDTO {

    private Long productId;
    private Integer count;
    private Long hubId;
}
