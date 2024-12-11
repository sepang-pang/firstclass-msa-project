package com.first_class.msa.business.application.service;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.repository.BusinessRepository;
import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    @Transactional
    public ResBusinessPostDTO postBy(Long userId, ReqBusinessPostDTO dto) {

        if (isDuplicateName(dto.getBusinessDTO().getName())) {
            throw new DuplicateRequestException("이미 존재하는 업체명입니다 : " + dto.getBusinessDTO().getName());
        }

        // --
        // TODO : 허브 검증 [ dto.getBusinessDTO().getHubId(); ]
        // --

        Business businessForSaving = Business.createBusiness(userId, dto);

        businessRepository.save(businessForSaving);

        return ResBusinessPostDTO.of(businessForSaving);
    }

    private boolean isDuplicateName(String name) {
        return businessRepository.existsByNameAndDeletedAtIsNull(name);
    }
}
