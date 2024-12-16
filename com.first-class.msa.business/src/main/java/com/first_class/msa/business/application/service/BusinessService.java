package com.first_class.msa.business.application.service;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import com.first_class.msa.business.application.dto.ResBusinessSearchDTO;
import com.first_class.msa.business.application.dto.external.ExternalResBusinessGetByIdDTO;
import com.first_class.msa.business.application.dto.external.ExternalResBusinessGetByUserIdDTO;
import com.first_class.msa.business.application.global.exception.custom.AuthorityException;
import com.first_class.msa.business.application.global.exception.custom.BadRequestException;
import com.first_class.msa.business.application.global.exception.custom.EntityAlreadyExistException;
import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.model.RoleType;
import com.first_class.msa.business.domain.repository.BusinessRepository;
import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
import com.first_class.msa.business.presentation.request.ReqBusinessPutByIdDTO;
import jakarta.persistence.EntityNotFoundException;
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
    private final AuthService authService;
    private final HubService hubService;

    @Transactional
    public ResBusinessPostDTO postBy(Long userId, String account, ReqBusinessPostDTO dto) {

        String roleForValidation = authService.getRoleBy(userId).getRole();

        validateBusinessCreationProcess(userId, dto, roleForValidation);

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

        String roleForValidation = authService.getRoleBy(userId).getRole();

        Business businessForModification = getBusinessBy(businessId);

        validateBusinessModificationProcess(userId, dto, roleForValidation, businessForModification);

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

        Business businessForDeletion = getBusinessBy(businessId);

        validateBusinessDeletionProcess(userId, businessForDeletion);

        businessForDeletion.deleteBusiness(account);
    }


    @Transactional(readOnly = true)
    public boolean existsBy(Long businessId) {
        return businessRepository.existsByIdAndDeletedAtIsNull(businessId);
    }

    @Transactional(readOnly = true)
    public ExternalResBusinessGetByIdDTO getBy(Long businessId) {
        return ExternalResBusinessGetByIdDTO.of(getBusinessBy(businessId));
    }
    
    @Transactional(readOnly = true)
    public ExternalResBusinessGetByUserIdDTO getByUserId(Long userId) {
        return ExternalResBusinessGetByUserIdDTO.of(getBusinessByUserId(userId));
    }


    private void validateBusinessCreationProcess(Long userId, ReqBusinessPostDTO dto, String roleForValidation) {

        // NOTE : 권한 검증
        validateUserRole(roleForValidation, Set.of(RoleType.MANAGER, RoleType.HUB_MANAGER));

        // NOTE : 허브 관리자 검증
        if (Objects.equals(roleForValidation, RoleType.HUB_MANAGER)) {
            validateHubManagerHubAssignment(userId, dto.getBusinessDTO().getHubId());
        }

        // NOTE : 업체 관리자 권한 검증
        validateBusinessManagerRole(dto.getBusinessDTO().getManagerId());

        // NOTE : 업체 이름 중복 검증
        validateBusinessNameDuplication(dto.getBusinessDTO().getName());
    }

    private void validateBusinessModificationProcess(Long userId, ReqBusinessPutByIdDTO dto, String roleForValidation, Business businessForModification) {

        // NOTE : 권한 검증
        validateUserRole(roleForValidation, Set.of(RoleType.MANAGER, RoleType.HUB_MANAGER, RoleType.BUSINESS_MANAGER));

        switch (roleForValidation) {
            case RoleType.HUB_MANAGER ->
                // NOTE : 허브 관리자 검증
                    validateHubManagerHubAssignment(userId, businessForModification.getHubId());
            case RoleType.BUSINESS_MANAGER ->
                // NOTE : 업체 담당자 검증
                    validateBusinessManagerBusinessAssignment(userId, businessForModification.getManagerId());
        }

        // NOTE : 유효성 검사
        validateHubChangeRequest(businessForModification, dto.getBusinessDTO().getHubId());
    }

    private void validateBusinessDeletionProcess(Long userId, Business businessForDeletion) {
        // NOTE : 허브 관리자 검증
        if (Objects.equals( authService.getRoleBy(userId).getRole(), RoleType.HUB_MANAGER)) {
            validateHubManagerHubAssignment(userId, businessForDeletion.getHubId());
        }
    }

    private void validateUserRole(String roleForValidation, Set<String> validRoles) {
        if (!validRoles.contains(roleForValidation)) {
            throw new AuthorityException("접근 권한이 없습니다");
        }
    }

    private void validateHubManagerHubAssignment(Long userId, Long reqHubId) {
        if (!Objects.equals(hubService.getHubIdBy(userId), reqHubId)) {
            throw new AuthorityException("본인이 속한 허브가 아닙니다.");
        }
    }

    private void validateBusinessManagerBusinessAssignment(Long userId, Long managerId) {
        if (!Objects.equals(userId, managerId)) {
            throw new AuthorityException("본인 업체가 아닙니다.");
        }
    }

    private void validateBusinessNameDuplication(String name) {
        if (businessRepository.existsByNameAndDeletedAtIsNull(name)) {
            throw new EntityAlreadyExistException("이미 등록된 업체명입니다. 다른 이름을 사용하거나 기존 업체 정보를 확인해주세요");
        }
    }

    private Business getBusinessBy(Long businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 업체입니다."));
    }

    private Business getBusinessByUserId(Long userId) {
        return businessRepository.findByManagerIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 업체입니다."));
    }

    private void validateBusinessManagerRole(Long managerId) {
        if (!Objects.equals(RoleType.BUSINESS_MANAGER, authService.getRoleBy(managerId).getRole())) {
            throw new AuthorityException("업체 관리자가 아닙니다. 다시 확인해주세요.");
        }
    }

    private void validateHubChangeRequest(Business businessForModification, Long reqHubId) {

        // NOTE: 요청한 허브 ID가 기존 업체의 허브 ID와 다를 경우,
        //       사용자가 허브를 변경하려는 것으로 간주하고 요청한 허브 ID의 유효성을 검증한다.
        //       요청한 허브 ID가 존재하지 않으면 예외를 발생시킨다.

        if (!Objects.equals(businessForModification.getHubId(), reqHubId) && !hubService.existsBy(reqHubId)) {
            throw new BadRequestException("허브 정보를 찾을 수 없습니다. 허브 ID를 확인해주세요.");
        }
    }
}
