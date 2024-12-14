package com.first_class.msa.agent.libs.dto;

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
