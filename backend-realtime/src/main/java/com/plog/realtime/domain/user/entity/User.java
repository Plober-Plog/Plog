package com.plog.realtime.domain.user.entity;

import com.plog.realtime.domain.image.entity.Image;
import com.plog.realtime.domain.user.entity.ChatAuth;
import com.plog.realtime.domain.user.entity.Gender;
import com.plog.realtime.domain.user.entity.Role;
import com.plog.realtime.domain.user.entity.State;
import com.plog.realtime.global.model.dto.BaseEntity;
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
    private int gender;
    public Gender getGender() {return Gender.gender(gender);};
    public void setGender(Gender gender) {this.gender = gender.getValue();}

    @Column
    private String source;

    @Column(nullable = false)
    private int state;
    public State getState() {return State.state(state);};
    public void setState(State state) {this.state = state.getValue();}

    @Column(nullable = false)
    private int role;
    public Role getRole() {return Role.role(role);};
    public void setRole(Role role) {this.role = role.getValue();}

    @Column
    private int provider;

    @Column
    private String providerId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalExp;

    @Column(nullable = false)
    private int chatAuth;
    public ChatAuth getChatAuth() {return ChatAuth.chatAuth(chatAuth);};
    public void setChatAuth(ChatAuth chatAuth) {this.chatAuth = chatAuth.getValue();}

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
