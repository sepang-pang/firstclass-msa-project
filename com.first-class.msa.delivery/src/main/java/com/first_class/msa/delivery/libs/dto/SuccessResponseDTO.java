package com.first_class.msa.delivery.libs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDTO<T> {
	private Integer code;
	private String message;
	private T data;
}
