package com.plog.backend.domain.sns.entity;

import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "article_tag")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleTagId;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "articleId")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "tag_type_id", referencedColumnName = "tagTypeId")
    private TagType tagType;
}