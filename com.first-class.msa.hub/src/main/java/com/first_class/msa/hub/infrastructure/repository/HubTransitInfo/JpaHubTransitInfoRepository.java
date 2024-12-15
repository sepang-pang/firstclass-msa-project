package com.first_class.msa.hub.infrastructure.repository.HubTransitInfo;

import com.first_class.msa.hub.domain.model.HubTransitInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubTransitInfoRepository extends JpaRepository<HubTransitInfo, Long> {
}
