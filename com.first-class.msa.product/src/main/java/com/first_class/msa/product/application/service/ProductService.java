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


    /*
        TODO
         1. 권한 체크 / MASTER, HUB_MANAGER, BUSINESS_MANAGER
         2. 허브 관리자 : 담당 허브 확인 (한정) O
         3. 업체 담당자 : 본인 업체 확인 (한정) O
         4. 업체 존재 검증
         5. 허브 존재 검증
         6. 업체 내 상품 동일 이름 중복 검사
         7. 최종 생성
    */

    @Transactional
    public ResProductPostDTO postBy(Long userId, String account, ReqProductPostDTO dto) {

        String roleForValidation = authService.getRoleBy(userId).getRole();

        validateUserRole(roleForValidation, Set.of(RoleType.MASTER, RoleType.HUB_MANAGER, RoleType.BUSINESS_MANAGER));


        // NOTE : 허브 관리자 검증
        if (Objects.equals(RoleType.HUB_MANAGER, roleForValidation)) {
            Long hubIdForChecking = hubService.getHubIdBy(userId);

            if (hubIdForChecking == null) {
                throw new IllegalArgumentException("사용자가 담당하는 허브를 찾을 수 없습니다.");
            }

            if (!Objects.equals(hubIdForChecking, dto.getProductDTO().getHubId())) {
                throw new IllegalArgumentException("허브 관리자는 자신이 담당한 허브에서만 업체를 생성할 수 있습니다.");
            }
        }
        // --------

        // NOTE : 업체 담당자 검증
        if (Objects.equals(RoleType.BUSINESS_MANAGER, roleForValidation)) {
            if (!Objects.equals(userId, businessService.getBy(dto.getProductDTO().getBusinessId()).getManagerId())) {
                throw new IllegalArgumentException("해당 업체 담당자가 아닙니다.");
            }
        }
        // --------

        // NOTE : 업체 유효성 검증
        if (!businessService.existsBy(dto.getProductDTO().getBusinessId())) {
            throw new IllegalArgumentException("유효하지 않은 업체입니다.");
        }
        // --------

        // NOTE : 허브 유효성 검증
        if (!hubService.existsBy(dto.getProductDTO().getHubId())) {
            throw new IllegalArgumentException("유효하지 않은 허브입니다.");
        }
        // --------

        // NOTE : 이름 중복 검사
        if (productRepository.existsByNameAndBusinessIdAndDeletedAtIsNull(dto.getProductDTO().getName(), dto.getProductDTO().getBusinessId())) {
            throw new IllegalArgumentException("이미 존재하는 상품명입니다.");
        }

        // NOTE : 상품 생성
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


    private void validateUserRole(String roleForValidation, Set<String> validRoles) {
        if (!validRoles.contains(roleForValidation)) {
            throw new IllegalArgumentException("접근 권한이 없습니다 : " + roleForValidation);
        }
    }
}