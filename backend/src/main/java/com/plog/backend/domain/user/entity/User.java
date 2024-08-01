package com.plog.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String searchId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("1")
    private Gender gender;

    @Column
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("1")
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("1")
    private Role role;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDate createAt;

    @Column
    @LastModifiedDate
    private LocalDate modifiedAt;

    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;

    @Column
    private String provider_id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalExp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("1")
    private ChatAuth chatAuth;

    @Column(nullable = false)
    @ColumnDefault("'글을 남겨주세요.'")
    private String profileInfo;

    @Column
    @ColumnDefault("'false")
    private boolean isAd;

    @Builder
    public User(String email,
                String searchId,
                String nickname,
                String password,
                int gender,
                int state,
                int role,
                int totalExp,
                int chatAuth,
                String profileInfo,
                boolean isAd) {
        this.email = email;
        this.searchId = searchId;
        this.nickname = nickname;
        this.password = password;
        this.gender = Gender.gender(gender);
        this.state = State.state(state);
        this.role = Role.role(role);
        this.createAt = LocalDate.now();
        this.totalExp = totalExp;
        this.chatAuth = ChatAuth.chatAuth(chatAuth);
        this.profileInfo = profileInfo;
        this.isAd = isAd;
    }
}
