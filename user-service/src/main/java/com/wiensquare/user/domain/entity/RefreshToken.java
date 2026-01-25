package com.wiensquare.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class RefreshToken extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @ManyToOne(optional = false)
    private UserEntity user;

    private LocalDateTime expiryDate;
}
