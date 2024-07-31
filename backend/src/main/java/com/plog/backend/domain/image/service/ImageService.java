package com.plog.backend.domain.image.service;

import com.plog.backend.domain.image.entity.Image;

public interface ImageService {
    Image uploadImage(String url);
}
