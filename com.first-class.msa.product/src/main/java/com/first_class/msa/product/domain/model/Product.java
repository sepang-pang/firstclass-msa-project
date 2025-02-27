package com.first_class.msa.product.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @Tsid
    @Column(name = "product_id")
    private Long id;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public Product(Long businessId, Long hubId, String name, int price, int quantity, String account) {
        this.businessId = businessId;
        this.hubId = hubId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.createdBy = account;
        this.modifiedBy = account;
    }

    public static Product createProduct(Long businessId, Long hubId, String name,
                                        int price, int quantity, String account) {
        return Product.builder()
                .businessId(businessId)
                .hubId(hubId)
                .name(name)
                .price(price)
                .quantity(quantity)
                .account(account)
                .build();
    }

    public void modifyProduct(String name, int price, int quantity, String account) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.modifiedBy = account;
    }

    public void deleteProduct(String account) {
        this.modifiedBy = account;
        this.deletedBy = account;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateQuantity(int quantity){
        this.quantity -= quantity;
    }
}
