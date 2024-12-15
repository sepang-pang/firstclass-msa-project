package com.first_class.msa.hub.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "p_hub_transit_infos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubTransitInfo {

    @Id @Tsid
    @Column(name = "hub_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_hub_id", referencedColumnName = "hub_id")
    private Hub departureHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_hub_id", referencedColumnName = "hub_id")
    private Hub arrivalHub;

    @Column(name = "transit_time")
    private Duration transitTime;

    @Column(name = "distance")
    private double distance;

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
    public HubTransitInfo(Hub departureHub, Hub arrivalHub, Duration transitTime, double distance, String account) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.transitTime = transitTime;
        this.distance = distance;
        this.createdBy = account;
        this.modifiedBy = account;
    }

    public static HubTransitInfo createHubTransitInfo(Hub departureHub, Hub arrivalHub, Duration transitTime, double distance, String account) {
        return HubTransitInfo.builder()
                .departureHub(departureHub)
                .arrivalHub(arrivalHub)
                .transitTime(transitTime)
                .distance(distance)
                .account(account)
                .build();
    }

    public void updateHubTransitInfo(Hub departureHub, Hub arrivalHub, Duration transitTime, double distance, String account) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.transitTime = transitTime;
        this.distance = distance;
        this.modifiedBy = account;
    }

    public void deleteHubTransitInfo(String account) {
        this.deletedBy = account;
        this.modifiedBy = account;
        this.deletedAt = LocalDateTime.now();
    }
}
