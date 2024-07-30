package com.plog.backend.domain.image.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image uploadImage(String url) {
        return imageRepository.save(new Image(url));
    }
}
