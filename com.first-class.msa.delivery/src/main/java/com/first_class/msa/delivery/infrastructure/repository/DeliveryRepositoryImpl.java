package com.first_class.msa.delivery.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.first_class.msa.delivery.domain.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
	private final DeliveryJpaRepository deliveryJpaRepository;

}
