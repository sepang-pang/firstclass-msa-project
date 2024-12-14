package com.first_class.msa.agent.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.domain.valueobject.Sequence;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_delivery_agent")
public class DeliveryAgent {
	@Id
	@Tsid
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "hud_id")
	private Long hubId;

	@Column(name = "slack_id", nullable = false)
	private String slackId;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private Type type;

	@Embedded
	private Sequence sequence;

	@Enumerated(EnumType.STRING)
	@Column(name = "is_available", nullable = false)
	private IsAvailable isAvailable;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Setter
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Setter
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@Setter
	@LastModifiedBy
	@Column(name = "updated_by")
	private Long updatedBy;

	@Setter
	@Column(name = "deleted_by")
	private Long deletedBy;

	public static DeliveryAgent createDeliveryAgent(
		Long userId,
		String slackId,
		Long hubId,
		Type type,
		Sequence sequence
	) {
		return DeliveryAgent.builder()
			.userId(userId)
			.hubId(hubId)
			.slackId(slackId)
			.sequence(sequence)
			.isAvailable(IsAvailable.ENABLE)
			.type(type)
			.build();
	}

	public void updateDeliveryAgent(IsAvailable isAvailable, String slackId, Type type, Long userId){
		this.isAvailable = isAvailable != null ? isAvailable : this.isAvailable;
		this.slackId = slackId != null ? slackId : this.slackId;
		this.type = type != null ? type : this.type;
		this.setUpdatedBy(userId);
	}

	public void deleteDeliveryAgent(Long userId){
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);
	}

}
