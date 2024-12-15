package com.first_class.msa.hub.infrastructure.repository.HubTransitInfo;

import com.first_class.msa.hub.domain.repository.HubTransitInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubTransitInfoRepositoryImpl implements HubTransitInfoRepository {

    private final JpaHubTransitInfoRepository jpaHubTransitInfoRepository;
}
