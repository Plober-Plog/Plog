package com.plog.backend.domain.image.entity;

import com.plog.backend.domain.sns.entity.Article;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "article_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleImageId;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(name = "`order`", nullable = false)
    private int order; // 1부터 시작
}
