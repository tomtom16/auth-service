package com.wiensquare.user.domain.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@MappedSuperclass
public class BaseEntity {

    @Id
    private UUID id;

    private LocalDateTime creationTimestamp;

    private LocalDateTime modificationTimestamp;

    @PrePersist
    public void prePersist() {
        generateId();
        updateCreationTimestamp();
    }

    @PreUpdate
    public void preUpdate() {
        updateModificationTimestamp();
    }

    private void generateId() {
        this.id = UUID.randomUUID();
    }

    private void updateCreationTimestamp() {
        this.creationTimestamp = LocalDateTime.now();
    }

    private void updateModificationTimestamp() {
        this.modificationTimestamp = LocalDateTime.now();
    }

}
