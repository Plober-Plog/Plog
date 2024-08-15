package com.plog.backend.domain.sns.entity;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "article")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column
    private String content;

    @Column
    private int visibility;

    public Visibility getVisibility() {
        return Visibility.visibility(visibility);
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility.getValue();
    }

    @Column
    private int view;

    @Column
    @ColumnDefault("1")
    private int state;

    public State getState() {
        return State.state(state);
    }

    public void setState(State state) {
        this.state = state.getValue();
    }
}