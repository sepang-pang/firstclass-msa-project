package com.first_class.msa.hub.application.service;

import com.first_class.msa.hub.application.dto.ResHubPostDTO;
import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.domain.repository.HubRepository;
import com.first_class.msa.hub.presentation.request.ReqHubPostDTO;
import com.first_class.msa.hub.presentation.request.ReqHubPutByIdDTO;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    @Transactional
    public ResHubPostDTO postBy(Long userId, ReqHubPostDTO dto) {

        if (isDuplicateHub(dto.getHubDTO().getLatitude(), dto.getHubDTO().getLongitude())) {
            throw new DuplicateRequestException("중복된 좌표의 허브입니다.");
        }

        Hub hubForSaving = Hub.createHub(userId, dto);

        return ResHubPostDTO.of(hubRepository.save(hubForSaving));
    }

    @Transactional
    public void putBy(Long userId, Long hubId, ReqHubPutByIdDTO dto) {

        Hub hubForModification = getHubBy(hubId);

        hubForModification.modifyHub(userId, dto);
    }


    private Hub getHubBy(Long hubId) {
        return hubRepository.findByIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 허브입니다."));
    }


    private boolean isDuplicateHub(double latitude, double longitude) {
        return hubRepository.existsByLatitudeAndLongitudeAndDeletedIsNull(latitude, longitude);
    }

}
