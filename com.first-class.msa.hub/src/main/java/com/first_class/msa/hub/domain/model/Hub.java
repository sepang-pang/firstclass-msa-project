package com.first_class.msa.hub.domain.model;

import com.first_class.msa.hub.presentation.request.ReqHubPostDTO;
import com.first_class.msa.hub.presentation.request.ReqHubPutByIdDTO;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "p_hubs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"latitude", "longitude"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub {

    @Id @Tsid
    @Column(name = "hub_id")
    private Long id;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "hub_name", nullable = false)
    private String name;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Column(name = "latitude", nullable = false)
    private double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

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
    public Hub (Long managerId, String name, double latitude, double longitude,
                String address, String addressDetail, String account) {

        this.managerId = managerId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.addressDetail = addressDetail;
        this.createdBy = account;
        this.modifiedBy = account;
    }

    public static Hub createHub(String account, ReqHubPostDTO dto) {
        return Hub.builder()
                .managerId(dto.getHubDTO().getManagerId())
                .name(dto.getHubDTO().getName())
                .latitude(dto.getHubDTO().getLatitude())
                .longitude(dto.getHubDTO().getLongitude())
                .address(dto.getHubDTO().getAddress())
                .addressDetail(dto.getHubDTO().getAddressDetail())
                .account(account)
                .build();
    }

    public void modifyHub(String account, ReqHubPutByIdDTO dto) {
        this.name = dto.getHubDTO().getName();
        this.latitude = dto.getHubDTO().getLatitude();
        this.longitude = dto.getHubDTO().getLongitude();
        this.address = dto.getHubDTO().getAddress();
        this.addressDetail = dto.getHubDTO().getAddressDetail();
        this.modifiedBy = account;
    }

    public void deleteHub(String account) {
        this.deletedAt = LocalDateTime.now();
        this.modifiedBy = account;
        this.deletedBy = account;
    }
}
