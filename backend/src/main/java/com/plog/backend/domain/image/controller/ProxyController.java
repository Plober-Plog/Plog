package com.plog.backend.domain.image.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/image")
public class ProxyController {

    // S3 이미지 캡처를 위한 프록시 컨트롤러
    @GetMapping("/proxy")
    public ResponseEntity<byte[]> getImage(@RequestParam String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg");  // 이미지 타입에 맞게 변경
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
