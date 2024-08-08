package com.plog.backend.domain.user.entity;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.user.entity.*;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"password", "provider", "providerId", "image"})
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    private Image image;

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

    @Column(nullable = false)
    private Gender gender;

    @Column
    private String source;

    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private Role role;

    @Column
    private Provider provider;

    @Column
    private String providerId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalExp;

    @Column(nullable = false)
    private ChatAuth chatAuth;

    @Column
    private String profileInfo;

    @Column
    @ColumnDefault("false")
    private boolean isAd;

    private int sidoCode;
    private int gugunCode;

    @Column
    private String notificationToken; // FCM 토큰 필드 추가

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isPushNotificationEnabled; // 푸시 알림 수신 여부 필드 추가
}
