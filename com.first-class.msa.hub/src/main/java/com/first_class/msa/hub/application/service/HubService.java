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

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    @Transactional
    public ResHubPostDTO postBy(String account, ReqHubPostDTO dto) {

        if (isDuplicateHub(dto.getHubDTO().getLatitude(), dto.getHubDTO().getLongitude())) {
            throw new DuplicateRequestException("중복된 좌표의 허브입니다.");
        }

        Hub hubForSaving = Hub.createHub(account, dto);

        return ResHubPostDTO.of(hubRepository.save(hubForSaving));
    }


    // --
    // XXX : 페이징 검색 기능에 캐싱은 필요할까 ? : 캐시 히트율이 높지 않을 거 같다.
    // --
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
    public void putBy(String account, Long hubId, ReqHubPutByIdDTO dto) {

        Hub hubForModification = getHubBy(hubId);

        hubForModification.modifyHub(account, dto);
    }

    @Transactional
    public void deleteBy(String account, Long hubId) {

        // --
        // XXX : 삭제 후 해당 허브를 참조하는 테이블 처리 여부 생각하기
        // --

        Hub hubForDeletion = getHubBy(hubId);

        hubForDeletion.deleteHub(account);
    }

    @Transactional(readOnly = true)
    public boolean existsBy(Long hubId) {
        return hubRepository.existsByIdAndDeletedAtIsNull(hubId);
    }


    private Hub getHubBy(Long hubId) {
        return hubRepository.findByIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 허브입니다."));
    }


    private boolean isDuplicateHub(double latitude, double longitude) {
        return hubRepository.existsByLatitudeAndLongitudeAndDeletedIsNull(latitude, longitude);
    }

    public Long getHubIdBy(Long userId) {
        return hubRepository.findByManagerIdAndDeletedAtIsNull(userId);
    }
}
