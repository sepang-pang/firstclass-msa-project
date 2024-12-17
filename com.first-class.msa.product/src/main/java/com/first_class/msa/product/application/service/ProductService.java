package com.first_class.msa.product.application.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.product.application.dto.ResProductPostDTO;
import com.first_class.msa.product.application.dto.ResProductSearchDTO;
import com.first_class.msa.product.application.dto.external.ExternalResProductGetByIdInDTO;
import com.first_class.msa.product.application.global.exception.custom.AuthorityException;
import com.first_class.msa.product.application.global.exception.custom.BadRequestException;
import com.first_class.msa.product.application.global.exception.custom.EntityAlreadyExistException;
import com.first_class.msa.product.domain.model.Product;
import com.first_class.msa.product.domain.model.RoleType;
import com.first_class.msa.product.domain.repository.ProductRepository;
import com.first_class.msa.product.infrastructure.configuration.RabbitMQConfig;
import com.first_class.msa.product.infrastructure.event.OrderCreateProductEvent;
import com.first_class.msa.product.infrastructure.massaging.ProductEventPublisher;
import com.first_class.msa.product.presentation.request.ReqProductPostDTO;
import com.first_class.msa.product.presentation.request.ReqProductPutByIdDTO;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final HubService hubService;
    private final BusinessService businessService;
    private final ProductEventPublisher productEventPublisher;

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

    @Transactional(readOnly = true)
    @Cacheable(value = "productSearchCache", key = "{#pageable.pageNumber, #pageable.pageSize, #minPrice, #maxPrice, #sort}")
    public ResProductSearchDTO searchBy(Long userId, Pageable pageable, String name, Integer minPrice, Integer maxPrice, String sort) {
        String roleForSearch = authService.getRoleBy(userId).getRole();

        Long hubIdForSearch = Objects.equals(RoleType.HUB_MANAGER, roleForSearch) ? hubService.getHubIdBy(userId) : null;

        return ResProductSearchDTO.of(productRepository.findProductByDeletedAtIsNullWithConditions(hubIdForSearch, pageable, name, minPrice, maxPrice, sort));
    }


    @Transactional
    @CacheEvict(cacheNames = "productSearchCache", allEntries = true)
    public void putBy(Long userId, String account, Long productId, ReqProductPutByIdDTO dto) {
        String roleForValidation = authService.getRoleBy(userId).getRole();

        Product productForModification = getProductBy(productId);

        validateProductModificationProcess(userId, dto, roleForValidation, productForModification);

        productForModification.modifyProduct(
                dto.getProductDTO().getName(),
                dto.getProductDTO().getPrice(),
                dto.getProductDTO().getQuantity(),
                account
        );
    }

    @Transactional
    @CacheEvict(cacheNames = "productSearchCache", allEntries = true)
    public void deleteBy(Long userId, String account, Long productId) {
        String roleForValidation = authService.getRoleBy(userId).getRole();

        Product productForDeletion = getProductBy(productId);

        validateProductDeletionProcess(userId, roleForValidation, productForDeletion);

        productForDeletion.deleteProduct(account);
    }

    @Transactional(readOnly = true)
    public List<ExternalResProductGetByIdInDTO> getBy(List<Long> productIdList) {

        List<Product> productListForMapping = getProductListBy(productIdList);

        return productListForMapping.stream()
                .map(ExternalResProductGetByIdInDTO::of)
                .toList();
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

    private void validateProductModificationProcess(Long userId, ReqProductPutByIdDTO dto, String roleForValidation, Product productForModification) {
        // NOTE : 권한 검증
        validateUserRole(roleForValidation, Set.of(RoleType.MASTER, RoleType.HUB_MANAGER, RoleType.BUSINESS_MANAGER));

        switch (roleForValidation) {
            case RoleType.HUB_MANAGER ->
                // NOTE : 허브 관리자 검증
                    validateHubManagerHubAssignment(userId, productForModification.getHubId());
            case RoleType.BUSINESS_MANAGER ->
                // NOTE : 업체 담당자 검증
                    validateBusinessManagerBusinessAssignment(userId, productForModification.getBusinessId());
        }

        // NOTE : 이름 중복 검사
        validateProductNameDuplication(dto.getProductDTO().getName(), productForModification.getBusinessId());
    }

    private void validateProductDeletionProcess(Long userId, String roleForValidation, Product productForDeletion) {
        // NOTE : 권한 검증
        validateUserRole(roleForValidation, Set.of(RoleType.MASTER, RoleType.HUB_MANAGER));

        // NOTE : 허브 관리자 검증
        if (Objects.equals(roleForValidation, RoleType.HUB_MANAGER)) {
            validateHubManagerHubAssignment(userId, productForDeletion.getHubId());
        }
    }

    private Product getProductBy(Long productId) {
        return productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new BadRequestException("유효하지 않은 상품입니다."));
    }

    private void validateUserRole(String roleForValidation, Set<String> validRoles) {
        if (!validRoles.contains(roleForValidation)) {
            throw new AuthorityException("접근 권한이 없습니다 : " + roleForValidation);
        }
    }

    private void validateHubManagerHubAssignment(Long userId, Long reqHubId) {
        if (!Objects.equals(hubService.getHubIdBy(userId), reqHubId)) {
            throw new AuthorityException("본인이 속한 허브가 아닙니다.");
        }
    }

    private void validateBusinessManagerBusinessAssignment(Long userId, Long reqBusinessId) {
        if (!Objects.equals(userId, businessService.getBy(reqBusinessId).getManagerId())) {
            throw new AuthorityException("본인 업체가 아닙니다.");
        }
    }

    private void validateBusiness(Long businessId) {
        if (!businessService.existsBy(businessId)) {
            throw new BadRequestException("유효하지 않은 업체입니다.");
        }
    }

    private void validateHub(Long hubId) {
        if (!hubService.existsBy(hubId)) {
            throw new BadRequestException("유효하지 않은 허브입니다.");
        }
    }

    private void validateProductNameDuplication(String name, Long businessId) {
        if (productRepository.existsByNameAndBusinessIdAndDeletedAtIsNull(name, businessId)) {
            throw new EntityAlreadyExistException("이미 존재하는 상품명입니다.");
        }
    }

    private List<Product> getProductListBy(List<Long> productIdList) {
        List<Product> productList = productRepository.findByIdInAndDeletedAtIsNull(productIdList);

        if (productList.isEmpty()) {
            throw new EntityNotFoundException("유효하지 않은 상품 목록입니다.");
        }

        return productList;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.PRODUCT_QUEUE)
    public void handleOrderCreateProductEvent(OrderCreateProductEvent event) {
        try {
            List<Long> productIdList =
                event
                    .getOrderLineDTO()
                    .stream()
                    .map(OrderCreateProductEvent.OrderLineDTO::getProductId)
                    .toList();

            if(event.getOrderLineDTO().size() != productIdList.size()){
                throw new IllegalArgumentException("상품 정보를 가져오지 못했습니다.");
            }

            List<Product> productList = productRepository.findByIdInAndDeletedAtIsNull(productIdList);

            for (Product product: productList) {
                for (OrderCreateProductEvent.OrderLineDTO orderLineDTO : event.getOrderLineDTO()) {
                    if(product.getId().equals(orderLineDTO.getProductId())){
                        if(product.getQuantity() - orderLineDTO.getCount() < 0){
                            throw new IllegalArgumentException("상품 정보를 업데이트 할 수 없습니다.");
                        } else {
                            product.updateQuantity(orderLineDTO.getCount());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            productEventPublisher.publishFailedOrder(event.getOrderId());
            throw e;
        }


    }
}
