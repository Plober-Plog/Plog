package com.plog.backend.domain.sns.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tag_type")
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