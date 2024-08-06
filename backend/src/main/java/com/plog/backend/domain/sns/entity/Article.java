package com.plog.backend.domain.sns.entity;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "article")
@NoArgsConstructor
@Getter
@Setter
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
    private int deleteType;

    public DeleteType getDeleteType() {
        return DeleteType.deleteType(deleteType);
    }
    public void setVisibility(DeleteType deleteType) {
        this.deleteType = deleteType.getValue();
    }
}