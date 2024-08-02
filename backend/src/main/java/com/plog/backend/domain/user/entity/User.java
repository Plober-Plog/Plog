package com.plog.backend.domain.user.entity;

import com.plog.backend.domain.area.entity.Gugun;
import com.plog.backend.domain.area.entity.Sido;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "user")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class User extends BaseEntity {
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

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column
    private String source;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;

    @Column
    private String providerId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalExp;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatAuth chatAuth;

    @Column(nullable = false)
    @ColumnDefault("'글을 남겨주세요.'")
    private String profileInfo;

    @Column
    @ColumnDefault("false")
    private boolean isAd;

    private int sidoCode;
    private int gugunCode;

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
                boolean isAd,
                int sidoCode,
                int gugunCode) {
        this.email = email;
        this.searchId = searchId;
        this.nickname = nickname;
        this.password = password;
        this.gender = Gender.gender(gender);
        this.state = State.state(state);
        this.role = Role.role(role);
        this.totalExp = totalExp;
        this.chatAuth = ChatAuth.chatAuth(chatAuth);
        this.profileInfo = profileInfo;
        this.isAd = isAd;
        this.sidoCode = sidoCode;
        this.gugunCode = gugunCode;
    }
}
