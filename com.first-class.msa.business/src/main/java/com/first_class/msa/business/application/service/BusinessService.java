package com.first_class.msa.business.application.service;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.repository.BusinessRepository;
import com.first_class.msa.business.infrastructure.client.HubClient;
import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final HubClient hubClient;

    @Transactional
    public ResBusinessPostDTO postBy(Long userId, String account, ReqBusinessPostDTO dto) {

        validateBusiness(dto);

        Business businessForSaving = Business.createBusiness(userId, account, dto);

        businessRepository.save(businessForSaving);

        return ResBusinessPostDTO.of(businessForSaving);
    }

    private void validateBusiness(ReqBusinessPostDTO dto) {
        if (isDuplicateName(dto.getBusinessDTO().getName())) {
            throw new DuplicateRequestException("이미 등록된 업체명입니다. 다른 이름을 사용하거나 기존 업체 정보를 확인해주세요");
        }

        if (!hubClient.existsBy(dto.getBusinessDTO().getHubId())) {
            throw new IllegalArgumentException("허브 정보를 찾을 수 없습니다. 허브 ID를 확인해주세요.");
        }
    }

    private boolean isDuplicateName(String name) {
        return businessRepository.existsByNameAndDeletedAtIsNull(name);
    }
}
