package com.plog.backend.domain.image.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/image")
@Slf4j
public class ProxyController {

    // S3 이미지 캡처를 위한 프록시 컨트롤러
    @GetMapping("/proxy")
    public ResponseEntity<byte[]> getImage(@RequestParam String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("Attempting to fetch image from URL: {}", url);

            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);

            if (imageBytes != null && imageBytes.length > 0) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "image/jpeg");  // 이미지 타입에 맞게 변경
                log.info("Image fetched successfully from URL: {}", url);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } else {
                log.error("Failed to fetch image: Received empty response from URL: {}", url);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching image from URL: {}", url, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
