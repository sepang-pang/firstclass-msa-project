package com.first_class.msa.agent.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.first_class.msa.agent.domain.entity.DeliveryAgent;

public interface DeliveryAgentJpaRepository extends JpaRepository<DeliveryAgent, Long> {

	Optional<DeliveryAgent> findByIdAndDeletedByIsNull(Long deliveryAgentId);

	@Query("SELECT MAX(d.sequence.value) FROM DeliveryAgent d WHERE d.hubId = :hubId AND d.deletedBy IS NOT NULL" )
	Integer findMaxSequenceByHubId(@Param("hubId") Long hubId);

	@Query("SELECT MAX(d.sequence.value) " +
		"FROM DeliveryAgent d " +
		"WHERE d.hubId = :hubId " +
		"AND d.type = com.first_class.msa.agent.domain.common.Type.HUB_AGENT " +
		"AND d.deletedBy IS NOT NULL")
	Integer findMaxSequenceByHubIdAndTypeHub(@Param("hubId") Long hubId);

	@Query("SELECT MAX(d.sequence.value) " +
		"FROM DeliveryAgent d " +
		"WHERE d.hubId = :hubId " +
		"AND d.type = com.first_class.msa.agent.domain.common.Type.DELIVERY_AGENT " +
		"AND d.deletedBy IS NOT NULL")
	Integer findMaxSequenceByHubIdAndTypeDelivery(@Param("hubId") Long hubId);

	@Query("SELECT d FROM DeliveryAgent d WHERE d.hubId = :hubId "
		+ "AND d.type = com.first_class.msa.agent.domain.common.Type.HUB_AGENT "
		+ "AND d.deletedBy IS NULL "
		+ "AND d.isAvailable = com.first_class.msa.agent.domain.common.IsAvailable.ENABLE "
	)
	DeliveryAgent findByDeliveryIdAndHubId(Long hubId);

	@Query("SELECT d FROM DeliveryAgent d WHERE d.hubId = :hubId AND d.deletedBy IS NULL ORDER BY d.sequence.value ASC")
	List<DeliveryAgent> findByHubId(@Param("hubId") Long hubId);

	Optional<DeliveryAgent> findByUserIdAndDeletedByIsNull(Long userId);
}
