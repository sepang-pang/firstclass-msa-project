package com.first_class.msa.agent.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.first_class.msa.agent.domain.entity.DeliveryAgent;

public interface DeliveryAgentJpaRepository extends JpaRepository<DeliveryAgent, Long> {

	@Query("SELECT MAX(d.sequence.value) FROM DeliveryAgent d WHERE d.hubId = :hubId AND d.deletedBy IS NOT NULL" )
	Integer findMaxSequenceByHubId(@Param("hubId") Long hubId);

	@Query("SELECT MAX(d.sequence.value) FROM DeliveryAgent d WHERE d.hubId IS NULL AND d.deletedBy IS NOT NULL")
	Integer findMaxSequenceByHubIdIsNull();

	@Query("SELECT d FROM DeliveryAgent d WHERE d.hubId IS NULL AND d.deletedBy IS NOT NULL ORDER BY d.sequence.value ASC")
	List<DeliveryAgent> findByHubIdIsNull();

	@Query("SELECT d FROM DeliveryAgent d WHERE d.hubId = :hubId AND d.deletedBy IS NOT NULL ORDER BY d.sequence.value ASC")
	List<DeliveryAgent> findByHubId(@Param("hubId") Long hubId);
}
