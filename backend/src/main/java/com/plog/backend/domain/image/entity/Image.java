package com.plog.backend.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int imageId;
    @Column
    String imageUrl;

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
