package com.plog.backend.domain.image.service;

import com.plog.backend.domain.image.dto.ArticleImageGetResponseDto;
import com.plog.backend.domain.image.dto.PlantDiaryImageGetResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    // TODO [이영원] 게시글 사진 추가 예정
    String[] uploadImages(MultipartFile[] images);

    String[] plantDiaryUploadImages(MultipartFile[] images, Long plantDiaryId, int thumbnailIdx);

    boolean deleteImage(String url);

    String loadImage(Long imageId);

    List<PlantDiaryImageGetResponseDto> loadImagesByPlantDiaryId(Long plantDiaryId);

    List<String> loadImageUrlsByPlantDiaryId(Long plantDiaryId);

    String loadThumbnailImageByPlantDiaryId(Long plantDiaryId);

    String[] ArticleUploadImages(MultipartFile[] images, Long articleId);

    List<ArticleImageGetResponseDto> loadImagesByArticleId(Long articleId);

    List<String> loadImagUrlsByArticleId(Long articleId);
}
