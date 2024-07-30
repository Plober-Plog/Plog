package com.plog.backend.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int imageId;
    @Column
    String imageUrl;

    public Image() {

    }

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
