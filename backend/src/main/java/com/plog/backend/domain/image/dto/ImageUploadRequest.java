package com.plog.backend.domain.image.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class ImageUploadRequest {
    MultipartFile file;
}
