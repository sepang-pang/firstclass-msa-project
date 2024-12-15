package com.first_class.msa.hub.application.service;

import com.first_class.msa.hub.application.dto.ResHubSearchDTO;
import com.first_class.msa.hub.application.dto.ResHubPostDTO;
import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.domain.repository.HubRepository;
import com.first_class.msa.hub.presentation.request.ReqHubPostDTO;
import com.first_class.msa.hub.presentation.request.ReqHubPutByIdDTO;
import com.querydsl.core.types.Predicate;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final AuthService authService;

    @Transactional
    public ResHubPostDTO postBy(Long userId, String account, ReqHubPostDTO dto) {

        validateHubCreationProcess(userId, dto);

        Hub hubForSaving = Hub.createHub(account, dto);

        return ResHubPostDTO.of(hubRepository.save(hubForSaving));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "hubSearchCache", key = "#predicate.hashCode() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public ResHubSearchDTO searchBy(Predicate predicate, Pageable pageable) {

        Page<Hub> hubPageByFilteringAndSorting = hubRepository.findAll(predicate, pageable);

        return ResHubSearchDTO.of(hubPageByFilteringAndSorting);
    }

    // --
    // TODO : 허브 상세 조회 구현하기 => 이동정보, 업체, 상품 구현 후
    // --

    @Transactional
    public void putBy(Long userId, String account, Long hubId, ReqHubPutByIdDTO dto) {

        validateUserRole(userId);

        Hub hubForModification = getHubBy(hubId);

        hubForModification.modifyHub(account, dto);
    }

    @Transactional
    public void deleteBy(Long userId, String account, Long hubId) {

        validateUserRole(userId);

        Hub hubForDeletion = getHubBy(hubId);

        hubForDeletion.deleteHub(account);
    }

    @Transactional(readOnly = true)
    public boolean existsBy(Long hubId) {
        return hubRepository.existsByIdAndDeletedAtIsNull(hubId);
    }

    @Transactional(readOnly = true)
    public Long getHubIdBy(Long userId) {
        return hubRepository.findByManagerIdAndDeletedAtIsNull(userId);
    }




    private void validateHubCreationProcess(Long userId, ReqHubPostDTO dto) {
        validateUserRole(userId);

        validateDuplicateHub(dto.getHubDTO().getLatitude(), dto.getHubDTO().getLongitude());
    }


    private void validateUserRole(Long userId) {
        if(!Objects.equals("MASTER", authService.getRoleBy(userId).getRole())) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

    private void validateDuplicateHub(double latitude, double longitude) {
        if (isDuplicateHub(latitude, longitude)) {
            throw new DuplicateRequestException("중복된 좌표의 허브입니다.");
        }
    }

    private boolean isDuplicateHub(double latitude, double longitude) {
        return hubRepository.existsByLatitudeAndLongitudeAndDeletedIsNull(latitude, longitude);
    }

    private Hub getHubBy(Long hubId) {
        return hubRepository.findByIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 허브입니다."));
    }
}
