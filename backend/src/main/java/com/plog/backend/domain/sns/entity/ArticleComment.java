package com.plog.backend.domain.sns.entity;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "article_comment")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ArticleComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleCommentId;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "articleId")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "parentId")
    private ArticleComment articleComment;

    @Column
    private String tagName;

    @Column
    @ColumnDefault("1")
    private int deleteType;

    public DeleteType getDeleteType() {
        return DeleteType.deleteType(deleteType);
    }
    public void setVisibility(DeleteType deleteType) {
        this.deleteType = deleteType.getValue();
    }
}
