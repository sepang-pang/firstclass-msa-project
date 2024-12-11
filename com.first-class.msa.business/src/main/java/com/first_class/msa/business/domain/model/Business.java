package com.first_class.msa.business.domain.model;


import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
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

    @Column(name = "user_id", nullable = false)
    private Long userId;

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
    private Long createdBy;

    @Column(name = "modified_by", nullable = false)
    private Long modifiedBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Builder
    public Business(Long userId, Long hubId, String name, BusinessType type, String address, String addressDetail) {
        this.userId = userId;
        this.hubId = hubId;
        this.name = name;
        this.type = type;
        this.address = address;
        this.addressDetail = addressDetail;
        this.createdBy = userId;
        this.modifiedBy = userId;
    }

    public static Business createBusiness(Long userId, ReqBusinessPostDTO dto) {
        return Business.builder()
                .userId(userId)
                .hubId(dto.getBusinessDTO().getHubId())
                .name(dto.getBusinessDTO().getName())
                .type(BusinessType.fromLabel(dto.getBusinessDTO().getType()))
                .address(dto.getBusinessDTO().getAddress())
                .addressDetail(dto.getBusinessDTO().getAddressDetail())
                .build();
    }
}
