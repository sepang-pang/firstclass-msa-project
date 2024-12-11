package com.first_class.msa.orders.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.domain.model.Order;

@Repository
public interface OrderRepository {

	Order save(Order order);

	ResOrderSearchDTO findAll(Long userId, Pageable pageable);
}
