package com.first_class.msa.message.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Message {

    @Id
    @Tsid
    @Column(name = "slack_message_id")
    private Long slackMessageId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "send_result")
    private boolean sendResult;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    public void updateContent(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
        this.content = newContent;
    }

    public void updateModifiedAt(LocalDateTime modifiedAt) {
        if (modifiedAt == null) {
            throw new IllegalArgumentException("UpdatedAt cannot be null");
        }
        this.modifiedAt = modifiedAt;
    }

    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public void setSendResult(boolean sendResult) {
        this.sendResult = sendResult;
    }
}

