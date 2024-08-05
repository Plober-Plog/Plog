package com.plog.backend.domain.image.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.plog.backend.domain.image.dto.PlantDiaryImageGetResponseDto;
import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.entity.PlantDiaryImage;
import com.plog.backend.domain.image.exception.ImageNotFoundException;
import com.plog.backend.domain.image.exception.InvalidImageFileException;
import com.plog.backend.domain.image.exception.S3FileUploadException;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.repository.PlantDiaryImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service("imageService")
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final PlantDiaryImageRepository plantDiaryImageRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    @Transactional
    public String[] uploadImages(MultipartFile[] images) {
        log.info(">>> uploadImages - 이미지 업로드 시작");
        String[] urls = new String[images.length];
        List<Image> imageList = new ArrayList<>();
        List<String> s3UploadedFileNames = new ArrayList<>();

        try {
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                if (image.isEmpty()) {
                    throw new InvalidImageFileException("빈 이미지 파일이 업로드 시도되었습니다.");
                }

                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                InputStream is = image.getInputStream();
                byte[] bytes = IOUtils.toByteArray(is);

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(bytes.length);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata);
                amazonS3.putObject(putObjectRequest); // put image to S3

                s3UploadedFileNames.add(fileName); // 업로드된 파일명 저장

                String url = amazonS3.getUrl(bucketName, fileName).toString();
                Image img = new Image(url);
                imageList.add(img);
                urls[i] = url;

                byteArrayInputStream.close();
                is.close();
            }

            imageRepository.saveAll(imageList); // 한 번에 저장
            log.info(">>> uploadImages - 이미지 업로드 및 DB 저장 완료");
        } catch (AmazonS3Exception e) {
            for (String fileName : s3UploadedFileNames) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            }
            throw new S3FileUploadException("S3 업로드 중 오류 발생");
        } catch (SdkClientException e) {
            for (String fileName : s3UploadedFileNames) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            }
            throw new S3FileUploadException("S3 업로드 중 클라이언트 오류 발생");
        } catch (IOException e) {
            for (String fileName : s3UploadedFileNames) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            }
            throw new S3FileUploadException("이미지 업로드 중 입출력 오류 발생");
        } catch (InvalidImageFileException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred");
        }

        return urls;
    }

    @Override
    @Transactional
    public String[] plantDiaryUploadImages(MultipartFile[] images, Long plantDiaryId, int thumbnailIdx) {
        log.info(">>> plantDiaryUploadImages - 플랜트 다이어리 이미지 업로드 시작, plantDiaryId: {}, thumbnailIdx: {}", plantDiaryId, thumbnailIdx);
        String[] urls = uploadImages(images);
        int order = 1;
        List<PlantDiaryImage> plantDiaryImageList = new ArrayList<>();

        try {

            for (String url : urls) {
                Optional<Image> optionalImage = imageRepository.findByImageUrl(url);
                if (optionalImage.isPresent()) {
                    Image img = optionalImage.get();

                    PlantDiaryImage plantDiaryImage = new PlantDiaryImage();
                    plantDiaryImage.setPlantDiaryId(plantDiaryId);
                    plantDiaryImage.setImage(img);
                    if (order - 1 == thumbnailIdx)
                        plantDiaryImage.setThumbnail(true);
                    plantDiaryImage.setOrder(order++);
                    plantDiaryImageList.add(plantDiaryImage);
                }
            }
            plantDiaryImageRepository.saveAll(plantDiaryImageList); // 한 번에 저장
            log.info(">>> plantDiaryUploadImages - 플랜트 다이어리 이미지 업로드 및 DB 저장 완료");
        } catch (Exception e) {
            // 업로드된 S3 파일 삭제
            for (String url : urls) {
                String fileName = url.substring(url.lastIndexOf("/") + 1);
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            }
            throw new S3FileUploadException("플랜트 다이어리 이미지 업로드 중 오류 발생");
        }

        return urls;
    }

    @Transactional
    @Override
    public boolean deleteImage(String url) {
        log.info(">>> deleteImage - 이미지 삭제 시작, URL: {}", url);
        Optional<Image> optionalImage = imageRepository.findByImageUrl(url);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            image.setDeleted(true);
            imageRepository.save(image);
            // URL에서 파일명 추출
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            try {
                // 파일명에 해당하는 이미지 S3에서 제거
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
                log.info(">>> deleteImage - 이미지 삭제 완료, URL: {}", url);
            } catch (AmazonS3Exception e) {
                throw new S3FileUploadException("S3에서 이미지 삭제 중 오류 발생");
            } catch (SdkClientException e) {
                throw new S3FileUploadException("S3에서 이미지 삭제 중 클라이언트 오류 발생");
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error occurred");
            }
            return true;
        }
        throw new ImageNotFoundException("해당 URL에 해당하는 이미지를 찾을 수 없습니다.");
    }

    @Override
    public String loadImage(Long imageId) {
        log.info(">>> loadImage - 이미지 로드 시작, ID: {}", imageId);
        Optional<Image> optionalImage = imageRepository.findByImageIdAndIsDeletedFalse(imageId);
        if (optionalImage.isPresent()) {
            log.info(">>> loadImage - 이미지 로드 성공, ID: {}", imageId);
            return optionalImage.get().getImageUrl();
        }
        throw new ImageNotFoundException("해당 ID에 해당하는 이미지를 찾을 수 없거나 삭제되었습니다.");
    }

    @Override
    public List<PlantDiaryImageGetResponseDto> loadImagesByPlantDiaryId(Long plantDiaryId) {
        log.info(">>> loadImagesByPlantDiaryId - 플랜트 다이어리 이미지 로드 시작, PlantDiaryId: {}", plantDiaryId);
        List<PlantDiaryImage> plantDiaryImages = plantDiaryImageRepository.findByPlantDiaryIdAndImageIsDeletedFalseOrderByOrderAsc(plantDiaryId);
        if (plantDiaryImages.isEmpty()) {
            return new ArrayList<>();
        }

        List<PlantDiaryImageGetResponseDto> plantDiaryImageGetResponseDtoList = new ArrayList<>();
        for (PlantDiaryImage plantDiaryImage : plantDiaryImages) {
            plantDiaryImageGetResponseDtoList.add(
                    PlantDiaryImageGetResponseDto.builder()
                            .plantDiaryImageId(plantDiaryImage.getPlantDiaryImageId())
                            .plantDiaryId(plantDiaryImage.getPlantDiaryId())
                            .image(plantDiaryImage.getImage())
                            .order(plantDiaryImage.getOrder())
                            .isThumbnail(plantDiaryImage.isThumbnail())
                            .build());

        }

        log.info(">>> loadImagesByPlantDiaryId - 플랜트 다이어리 이미지 로드 성공, PlantDiaryId: {}", plantDiaryId);
        return plantDiaryImageGetResponseDtoList;
    }

    @Override
    public List<String> loadImageUrlsByPlantDiaryId(Long plantDiaryId) {
        log.info(">>> loadImageUrlsByPlantDiaryId - 플랜트 다이어리 이미지 로드 시작, PlantDiaryId: {}", plantDiaryId);
        List<PlantDiaryImage> plantDiaryImages = plantDiaryImageRepository.findByPlantDiaryIdAndImageIsDeletedFalseOrderByOrderAsc(plantDiaryId);
        if (plantDiaryImages.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> imageUrls = new ArrayList<>();
        for (PlantDiaryImage plantDiaryImage : plantDiaryImages) {
            imageUrls.add(plantDiaryImage.getImage().getImageUrl());
        }
        return imageUrls;
    }

    @Override
    public String loadThumbnailImageByPlantDiaryId(Long plantDiaryId) {
        log.info(">>> loadThumbnailImageByPlantDiaryId - 플랜트 다이어리 썸네일 이미지 로드 시작, PlantDiaryId: {}", plantDiaryId);
        Optional<PlantDiaryImage> thumbnailImage = plantDiaryImageRepository.findByPlantDiaryIdAndIsThumbnailTrue(plantDiaryId);
        if (thumbnailImage == null) {
            return "";
        }
        log.info(">>> loadThumbnailImageByPlantDiaryId - 플랜트 다이어리 썸네일 이미지 로드 성공, PlantDiaryId: {}", plantDiaryId);
        return thumbnailImage.get().getImage().getImageUrl();
    }
}
