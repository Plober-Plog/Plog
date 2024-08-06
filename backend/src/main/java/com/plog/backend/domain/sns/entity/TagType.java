package com.plog.backend.domain.sns.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tag_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TagType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagTypeId;

    @Column
    private String tagName;
}