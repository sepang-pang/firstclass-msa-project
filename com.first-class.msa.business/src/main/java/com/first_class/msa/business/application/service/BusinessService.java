package com.first_class.msa.business.application.service;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import com.first_class.msa.business.application.dto.ResBusinessSearchDTO;
import com.first_class.msa.business.application.dto.ResRoleGetByUserIdDTO;
import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.model.RoleType;
import com.first_class.msa.business.domain.repository.BusinessRepository;
import com.first_class.msa.business.infrastructure.client.AuthClient;
import com.first_class.msa.business.infrastructure.client.HubClient;
import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
import com.first_class.msa.business.presentation.request.ReqBusinessPutByIdDTO;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final HubClient hubClient;
    private final AuthClient authClient;

    @Transactional
    public ResBusinessPostDTO postBy(Long userId, String account, ReqBusinessPostDTO dto) {

        ResRoleGetByUserIdDTO dtoForValidation = authClient.getRoleBy(userId);

        // -- 권한 검증
        validateUserRole(dtoForValidation.getRole(), Set.of(RoleType.MANAGER, RoleType.HUB_MANAGER));

        // -- 허브 관리자 검증 : 담당 허브일 경우에만 업체를 생성할 수 있음
        validateHubForManager(userId, dto.getBusinessDTO().getHubId(), dtoForValidation.getRole());

        // -- 업체 관리자 권한 검증 : 요청한 업체 관리자가 업체 관리자 권한 소유자인지 검증
        validateBusinessManagerRole(dto.getBusinessDTO().getManagerId());

        // -- 업체 이름 중복 검증
        validateBusinessNameDuplication(dto.getBusinessDTO().getName());

        Business businessForSaving = Business.createBusiness(
                dto.getBusinessDTO().getManagerId(),
                account,
                dto.getBusinessDTO().getHubId(),
                dto.getBusinessDTO().getName(),
                dto.getBusinessDTO().getType(),
                dto.getBusinessDTO().getAddress(),
                dto.getBusinessDTO().getAddressDetail()
        );

        businessRepository.save(businessForSaving);

        return ResBusinessPostDTO.of(businessForSaving);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "businessSearchCache", key = "{#pageable.pageNumber, #pageable.pageSize, #name, #address, #type, #sort}")
    public ResBusinessSearchDTO searchBy(Pageable pageable, String name, String address, String type, String sort) {
        return ResBusinessSearchDTO.of(businessRepository.findBusinessByDeletedAtIsNullWithConditions(pageable, name, address, type, sort));
    }


    @Transactional
    @CacheEvict(cacheNames = "businessSearchCache", allEntries = true)
    public void putBy(Long userId, String account, Long businessId, ReqBusinessPutByIdDTO dto) {

        ResRoleGetByUserIdDTO roleForValidation = authClient.getRoleBy(userId);

        // -- 권한 검증
        validateUserRole(roleForValidation.getRole(), Set.of(RoleType.MANAGER, RoleType.HUB_MANAGER, RoleType.BUSINESS_MANAGER));

        Business businessForModification = getBusinessBy(businessId);

        // -- 허브 관리자 : 담당 허브에 속한 업체만 수정 가능합
        // -- 업체 관리자 : 본인 업체만 수정 가능함
        validateHubOrBusinessManagerAccess(userId, roleForValidation.getRole(), businessForModification);

        // -- 유효성 검사
        validateHubChangeRequest(businessForModification, dto.getBusinessDTO().getHubId());

        businessForModification.modifyBusiness(
                account,
                dto.getBusinessDTO().getHubId(),
                dto.getBusinessDTO().getName(),
                dto.getBusinessDTO().getType(),
                dto.getBusinessDTO().getAddress(),
                dto.getBusinessDTO().getAddressDetail()
        );
    }

    @Transactional
    @CacheEvict(cacheNames = "businessSearchCache", allEntries = true)
    public void deleteBy(Long userId, String account, Long businessId) {

        ResRoleGetByUserIdDTO dtoForValidation = authClient.getRoleBy(userId);

        // -- 권한 검증
        validateUserRole(dtoForValidation.getRole(), Set.of(RoleType.MANAGER, RoleType.HUB_MANAGER));

        Business businessForDeletion = getBusinessBy(businessId);

        // -- 허브 관리자 검증 : 담당 허브에 속한 업체만 삭제 가능
        if (Objects.equals(dtoForValidation.getRole(), RoleType.HUB_MANAGER)) {
            validateHubManager(userId, businessForDeletion);
        }

        businessForDeletion.deleteBusiness(account);
    }


    private void validateUserRole(String roleForValidation, Set<String> validRoles) {
        if (!validRoles.contains(roleForValidation)) {
            throw new IllegalArgumentException("접근 권한이 없습니다 : " + roleForValidation);
        }
    }

    private void validateHubForManager(Long userId, Long hubId, String roleForValidation) {
        if (RoleType.HUB_MANAGER.equals(roleForValidation)) {
            Long hubIdForChecking = hubClient.getHubIdBy(userId);

            if (hubIdForChecking == null) {
                throw new IllegalArgumentException("사용자가 담당하는 허브를 찾을 수 없습니다.");
            }

            if (!Objects.equals(hubIdForChecking, hubId)) {
                throw new IllegalArgumentException("허브 관리자는 자신이 담당한 허브에서만 업체를 생성할 수 있습니다.");
            }
        }
    }

    private void validateBusinessNameDuplication(String name) {
        if (isDuplicateName(name)) {
            throw new DuplicateRequestException("이미 등록된 업체명입니다. 다른 이름을 사용하거나 기존 업체 정보를 확인해주세요");
        }
    }

    private boolean isDuplicateName(String name) {
        return businessRepository.existsByNameAndDeletedAtIsNull(name);
    }

    private Business getBusinessBy(Long businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다."));
    }

    private void validateBusinessManagerRole(Long managerId) {
        if (!Objects.equals(RoleType.BUSINESS_MANAGER, authClient.getRoleBy(managerId).getRole())) {
            throw new IllegalArgumentException("업체 관리자가 아닙니다. 다시 확인해주세요.");
        }
    }

    private void validateHubOrBusinessManagerAccess(Long userId, String roleForValidation, Business businessForModification) {
        switch (roleForValidation) {
            case RoleType.HUB_MANAGER -> {
                validateHubManager(userId, businessForModification);
            }

            case RoleType.BUSINESS_MANAGER -> {
                validateBusinessManager(userId, businessForModification);
            }
        }
    }

    private void validateHubManager(Long userId, Business businessForModification) {
        if (!Objects.equals(hubClient.getHubIdBy(userId), businessForModification.getHubId())) {
            throw new IllegalArgumentException("담당 허브의 업체만 수정할 수 있습니다.");
        }
    }

    private static void validateBusinessManager(Long userId, Business businessForModification) {
        if (!Objects.equals(userId, businessForModification.getManagerId())) {
            throw new IllegalArgumentException("본인의 업체만 수정할 수 있습니다.");
        }
    }

    private void validateHubChangeRequest(Business businessForModification, Long reqHubId) {

        // NOTE: 요청한 허브 ID가 기존 업체의 허브 ID와 다를 경우,
        //       사용자가 허브를 변경하려는 것으로 간주하고 요청한 허브 ID의 유효성을 검증한다.
        //       요청한 허브 ID가 존재하지 않으면 예외를 발생시킨다.

        if (!Objects.equals(businessForModification.getHubId(), reqHubId) && !hubClient.existsBy(reqHubId)) {
            throw new IllegalArgumentException("허브 정보를 찾을 수 없습니다. 허브 ID를 확인해주세요.");
        }
    }
}
