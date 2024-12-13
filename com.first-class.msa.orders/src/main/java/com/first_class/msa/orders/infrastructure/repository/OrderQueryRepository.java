package com.first_class.msa.orders.infrastructure.repository;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;

public interface OrderQueryRepository {

	ResOrderSearchDTO findAll(AuthSearchConditionDTO authSearchConditionDTO, Pageable pageable);

}
