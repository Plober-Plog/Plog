package com.plog.backend.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    @Id
    private Integer userId;
    @Column(nullable = false)
    String nickname;
    @Column(nullable = false)
    String password;
    @Column(nullable = false, unique = true)
    String searchId;

    public User() {

    }
}
