package com.first_class.msa.delivery.domain.repository;

import java.util.Optional;

import com.first_class.msa.delivery.domain.model.Delivery;

public interface DeliveryRepository {

	Delivery save(Delivery delivery);

	Optional<Delivery> findById(Long deliveryId);
}
