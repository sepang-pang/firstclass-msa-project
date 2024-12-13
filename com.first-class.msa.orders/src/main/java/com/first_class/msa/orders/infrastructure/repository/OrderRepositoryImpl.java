package com.first_class.msa.orders.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
	private final OrderJpaRepository orderJpaRepository;
	private final OrderQueryRepository orderQueryRepository;

	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(order);
	}

	@Override
	public ResOrderSearchDTO findAll(AuthSearchConditionDTO authSearchConditionDTO, Pageable pageable) {
		return orderQueryRepository.findAll(authSearchConditionDTO, pageable);
	}

	@Override
	public Optional<Order> findById(Long orderId) {
		return orderJpaRepository.findById(orderId);
	}

}
