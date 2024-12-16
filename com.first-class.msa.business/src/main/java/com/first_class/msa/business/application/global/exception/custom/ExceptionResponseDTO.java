package com.first_class.msa.business.application.global.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDTO<T> {
    private Integer code;
    private String message;
    private T data;
}