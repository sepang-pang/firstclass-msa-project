package com.first_class.msa.product.application.service;

import com.first_class.msa.product.application.dto.ResProductPostDTO;
import com.first_class.msa.product.domain.model.Product;
import com.first_class.msa.product.domain.model.RoleType;
import com.first_class.msa.product.domain.repository.ProductRepository;
import com.first_class.msa.product.presentation.request.ReqProductPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final HubService hubService;
    private final BusinessService businessService;

    @Transactional
    public ResProductPostDTO postBy(Long userId, String account, ReqProductPostDTO dto) {

        String roleForValidation = authService.getRoleBy(userId).getRole();

        validateProductCreationProcess(userId, dto, roleForValidation);

        Product productForSaving = Product.createProduct(
                dto.getProductDTO().getBusinessId(),
                dto.getProductDTO().getHubId(),
                dto.getProductDTO().getName(),
                dto.getProductDTO().getPrice(),
                dto.getProductDTO().getQuantity(),
                account
        );

        productRepository.save(productForSaving);

        return ResProductPostDTO.of(productForSaving);
    }

    private void validateProductCreationProcess(Long userId, ReqProductPostDTO dto, String roleForValidation) {
        // NOTE : 권한 검증
        validateUserRole(roleForValidation, Set.of(RoleType.MASTER, RoleType.HUB_MANAGER, RoleType.BUSINESS_MANAGER));

        switch (roleForValidation) {
            case RoleType.HUB_MANAGER ->
                // NOTE : 허브 관리자 검증
                    validateHubManagerHubAssignment(userId, dto.getProductDTO().getHubId());
            case RoleType.BUSINESS_MANAGER ->
                // NOTE : 업체 담당자 검증
                    validateBusinessManagerBusinessAssignment(userId, dto.getProductDTO().getBusinessId());
        }

        // NOTE : 업체 유효성 검증
        validateBusiness(dto.getProductDTO().getBusinessId());

        // NOTE : 허브 유효성 검증
        validateHub(dto.getProductDTO().getHubId());

        // NOTE : 이름 중복 검사
        validateProductNameDuplication(dto.getProductDTO().getName(), dto.getProductDTO().getBusinessId());
    }

    private void validateUserRole(String roleForValidation, Set<String> validRoles) {
        if (!validRoles.contains(roleForValidation)) {
            throw new IllegalArgumentException("접근 권한이 없습니다 : " + roleForValidation);
        }
    }

    private void validateHubManagerHubAssignment(Long userId, Long reqHubId) {
        if (!Objects.equals(hubService.getHubIdBy(userId), reqHubId)) {
            throw new IllegalArgumentException("본인이 속한 허브가 아닙니다.");
        }
    }

    private void validateBusinessManagerBusinessAssignment(Long userId, Long reqBusinessId) {
        if (!Objects.equals(userId, businessService.getBy(reqBusinessId).getManagerId())) {
            throw new IllegalArgumentException("본인 업체가 아닙니다.");
        }
    }

    private void validateBusiness(Long businessId) {
        if (!businessService.existsBy(businessId)) {
            throw new IllegalArgumentException("유효하지 않은 업체입니다.");
        }
    }

    private void validateHub(Long hubId) {
        if (!hubService.existsBy(hubId)) {
            throw new IllegalArgumentException("유효하지 않은 허브입니다.");
        }
    }

    private void validateProductNameDuplication(String name, Long businessId) {
        if (productRepository.existsByNameAndBusinessIdAndDeletedAtIsNull(name, businessId)) {
            throw new IllegalArgumentException("이미 존재하는 상품명입니다.");
        }
    }
}