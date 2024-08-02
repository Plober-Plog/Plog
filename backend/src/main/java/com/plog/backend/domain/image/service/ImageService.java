package com.plog.backend.domain.image.service;

import com.plog.backend.domain.image.entity.Image;

public interface ImageService {
    //TODO [강윤서]
    // - uploadImage parameter 변경 (String -> MultipartFile)
    Image uploadImage(String url);
}
