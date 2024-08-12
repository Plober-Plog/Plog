package com.plog.backend.domain.image.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class ProxyController {

    private final AmazonS3 amazonS3;

    @GetMapping("/proxy")
    public ResponseEntity<byte[]> getImage(@RequestParam String url) {
        try {
            // URL 디코딩
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.name());
            log.info("Decoded URL: {}", decodedUrl);

            // S3 버킷과 키 추출
            String bucketName = "plogbucket";  // S3 버킷 이름
            String key = extractKeyFromUrl(decodedUrl);
            log.info("Extracted S3 key: {}", key);

            // S3에서 이미지 가져오기
            S3Object s3Object = amazonS3.getObject(bucketName, key);
            InputStream inputStream = s3Object.getObjectContent();
            byte[] imageBytes = IOUtils.toByteArray(inputStream);

            if (imageBytes != null && imageBytes.length > 0) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "image/jpeg");  // 이미지 타입에 맞게 변경
                headers.set("Access-Control-Allow-Origin", "*");  // CORS 헤더 추가
                log.info("Image fetched successfully from S3 with key: {}", key);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } else {
                log.error("Failed to fetch image: Received empty response for key: {}", key);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching image from S3", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractKeyFromUrl(String url) {
        // URL에서 S3 key를 추출하는 로직
        // 예: https://plogbucket.s3.ap-northeast-2.amazonaws.com/ad0855c8-0735-4dcf-933b-76b5bba00ab1_식물.jfif?t=1723435968252
        String s3Prefix = "https://plogbucket.s3.ap-northeast-2.amazonaws.com/";
        int startIndex = url.indexOf(s3Prefix) + s3Prefix.length();
        int endIndex = url.indexOf("?");  // 쿼리 파라미터 제거
        return url.substring(startIndex, endIndex);
    }
}
