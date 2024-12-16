package com.first_class.msa.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import com.first_class.msa.delivery.domain.model.Delivery;

public interface DeliveryRepository {

	Delivery save(Delivery delivery);

	Optional<Delivery> findByIdAndIsNotNULL(Long deliveryId);

	Optional<Delivery> findById(Long deliveryId);

	List<Delivery> findAllByOrderIdInAndDeletedByIsNull(List<Long> orderIdList);

	boolean existsByIdAndOrderId(Long deliveryId, Long orderId);
}
