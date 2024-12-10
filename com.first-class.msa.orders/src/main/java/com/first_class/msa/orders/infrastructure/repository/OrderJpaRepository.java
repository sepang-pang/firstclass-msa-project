package com.first_class.msa.orders.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.repository.OrderRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepository{
}
