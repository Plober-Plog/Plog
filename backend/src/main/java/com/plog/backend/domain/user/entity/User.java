package com.plog.backend.domain.user.entity;

import com.plog.backend.domain.image.entity.Image;
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
@ToString(exclude = {"password","provider","providerId","image"})
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @JoinColumn(name="image_id", referencedColumnName = "imageId")
    private Image imageId;

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
    private int gender;
    public Gender getGender() {return Gender.gender(gender);};
    public void setGender(Gender gender) {this.gender = gender.getValue();}

    @Column
    private String source;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private int state;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private int role;

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
    private int chatAuth;

    @Column
    private String profileInfo;

    @Column
    @ColumnDefault("false")
    private boolean isAd;

    private int sidoCode;
    private int gugunCode;
}
