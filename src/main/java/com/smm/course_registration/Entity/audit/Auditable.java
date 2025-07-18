package com.smm.course_registration.Entity.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
