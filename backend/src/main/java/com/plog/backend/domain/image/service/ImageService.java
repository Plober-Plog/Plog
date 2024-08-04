package com.plog.backend.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    // TODO [이영원] 게시글 사진 추가 예정
    String[] uploadImages(MultipartFile[] images);

    String[] plantDiaryUploadImages(MultipartFile[] images, Long plantDiaryId, int thumbnailIdx);

    boolean deleteImage(String url);

    String loadImage(Long imageId);

    List<String> loadImagesByPlantDiaryId(int plantDiaryId);

    String loadThumbnailImageByPlantDiaryId(int plantDiaryId);
}
