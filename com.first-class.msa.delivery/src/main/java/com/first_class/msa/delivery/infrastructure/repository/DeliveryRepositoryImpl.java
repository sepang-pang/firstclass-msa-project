package com.first_class.msa.delivery.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
	private final DeliveryJpaRepository deliveryJpaRepository;

	@Override
	public Delivery save(Delivery delivery) {
		return deliveryJpaRepository.save(delivery);
	}

	@Override
	public Optional<Delivery> findById(Long deliveryId) {
		return deliveryJpaRepository.findByIdAndDeletedByIsNotNull(deliveryId);
	}

}
