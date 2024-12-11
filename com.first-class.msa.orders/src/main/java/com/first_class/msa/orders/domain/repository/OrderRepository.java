package com.first_class.msa.orders.domain.repository;

import org.springframework.stereotype.Repository;

import com.first_class.msa.orders.domain.model.Order;

@Repository
public interface OrderRepository {

	Order save(Order order);

}
