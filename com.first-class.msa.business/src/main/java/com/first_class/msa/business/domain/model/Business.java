package com.first_class.msa.business.domain.model;


import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "p_business")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Business {

    @Id @Tsid
    @Column(name = "business_id")
    private Long id;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BusinessType type;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at" , nullable = false)
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
    public Business(Long managerId, Long hubId, String name, BusinessType type, String address, String addressDetail, String account) {
        this.managerId = managerId;
        this.hubId = hubId;
        this.name = name;
        this.type = type;
        this.address = address;
        this.addressDetail = addressDetail;
        this.createdBy = account;
        this.modifiedBy = account;
    }

    public static Business createBusiness(Long managerId, String account, Long hubId,
                                          String businessName, String type, String address, String addressDetail) {

        return Business.builder()
                .managerId(managerId)
                .hubId(hubId)
                .name(businessName)
                .type(BusinessType.fromLabel(type))
                .address(address)
                .addressDetail(addressDetail)
                .account(account)
                .build();
    }

    public void modifyBusiness(String account, Long hubId,
                               String businessName, String type, String address, String addressDetail) {

        this.modifiedBy = account;
        this.hubId = hubId;
        this.name = businessName;
        this.type = BusinessType.fromLabel(type);
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public void deleteBusiness(String account) {
        this.modifiedBy = account;
        this.deletedBy = account;
        this.deletedAt = LocalDateTime.now();
    }
}
