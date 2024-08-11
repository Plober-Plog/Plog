package com.plog.realtime.domain.notification.entity;

import com.plog.realtime.domain.image.entity.Image;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private int type;
    @Column
    private Boolean isRead = false;

    @Column(length = 255)
    private String content;

    @Column(length = 255)
    private String clickUrl;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 Notification이 하나의 Image를 가질 수 있음
    @JoinColumn(name = "image_id")
    private Image image;
}
