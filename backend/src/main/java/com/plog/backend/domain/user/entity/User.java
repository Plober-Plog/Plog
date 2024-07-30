package com.plog.backend.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
public class User {
    @Id
    private Integer userId;

    @Column
    private String email;

    @Column
    private String searchId

    @Column(nullable = false)
    String nickname;

    @Column(nullable = false)
    String password;

    @Column
    Date birthDate

    public User() {

    }
}
