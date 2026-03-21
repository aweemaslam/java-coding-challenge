package com.crewmeister.cmcodingchallenge.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Base entity class to provide automatic creation and update timestamps.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /** Timestamp when the entity was created (automatically set, not updatable). */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the entity was last updated (automatically set). */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}