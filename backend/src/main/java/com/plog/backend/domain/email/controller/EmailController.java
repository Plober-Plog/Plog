package com.plog.backend.domain.email.controller;

import com.plog.backend.domain.email.dto.request.EmailRequestDto;
import com.plog.backend.domain.email.dto.response.VerifyResponseDto;
import com.plog.backend.domain.email.service.EmailService;
import com.plog.backend.global.model.response.BaseResponseBody;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class EmailController {
    private final EmailService emailService;

    // 인증코드 메일 발송
    @PostMapping("/email/send")
    public ResponseEntity<BaseResponseBody> mailSend(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        log.info(">>> [POST] /user/email/send - 인증코드 메일 발송 요청: {}", emailRequestDto.toString());
        emailService.sendEmail(emailRequestDto.getMail());
        log.info(">>> [POST] /user/email/send - 인증 메일 전송 완료: {}", emailRequestDto.getMail());
        return ResponseEntity.status(200).body(BaseResponseBody.of(200,"인증 메일이 전송 되었습니다."));
    }

    // 인증코드 인증
    @PostMapping("/email/check")
    public ResponseEntity<VerifyResponseDto> verify(@RequestBody EmailRequestDto emailRequestDto) {
        log.info(">>> [POST] /user/email/check - 인증코드 검증 요청: {}", emailRequestDto.toString());
        VerifyResponseDto verifyResponseDto;
        try {
            boolean isVerify = emailService.verifyEmailCode(emailRequestDto.getMail(), emailRequestDto.getVerifyCode());
            log.info(">>> [POST] /user/email/check - 인증코드 검증 결과: {}", isVerify);
            verifyResponseDto = new VerifyResponseDto(isVerify, "");
            if(isVerify) {
                verifyResponseDto.setMessage("인증코드가 확인 되었습니다.");
                return ResponseEntity.status(200).body(verifyResponseDto);
            } else {
                verifyResponseDto.setMessage("인증코드가 틀립니다.");
                return ResponseEntity.status(400).body(verifyResponseDto);
            }
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
                log.warn(">>> [POST] /user/email/check - 인증코드 만료: {}", emailRequestDto.getMail());
                verifyResponseDto = new VerifyResponseDto(false, "인증코드가 만료되었습니다.");
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(verifyResponseDto);
            } else {
                log.error(">>> [POST] /user/email/check - 인증코드 검증 중 예외 발생", e);
                throw e;
            }
        }
    }

    // 비밀번호 찾기 인증코드 메일 발송
    @PostMapping("/password/send")
    public ResponseEntity<BaseResponseBody> findPassword(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        log.info(">>> [POST] /user/password/send - 비밀번호 찾기 인증코드 메일 발송 요청: {}", emailRequestDto);
        emailService.sendEmail(emailRequestDto.getMail());
        log.info(">>> [POST] /user/password/send - 비밀번호 찾기 인증 메일 전송 완료: {}", emailRequestDto.getMail());
        return ResponseEntity.status(200).body(BaseResponseBody.of(200,"인증 메일이 전송 되었습니다."));
    }

    // 비밀번호 찾기 인증코드 검증
    @PostMapping("/password/check")
    public ResponseEntity<VerifyResponseDto> findPasswordVerify(@RequestBody EmailRequestDto emailRequestDto)  {
        log.info(">>> [POST] /user/password/check - 비밀번호 찾기 인증코드 검증 요청: {}", emailRequestDto);
        VerifyResponseDto verifyResponseDto;
        try {
            boolean isVerify = emailService.verifyEmailCode(emailRequestDto.getMail(), emailRequestDto.getVerifyCode());
            log.info(">>> [POST] /user/password/check - 비밀번호 찾기 인증코드 검증 결과: {}", isVerify);
            verifyResponseDto = new VerifyResponseDto(isVerify, "");
            if(isVerify) {
                verifyResponseDto.setMessage("인증코드가 확인 되었습니다.");
                return ResponseEntity.status(200).body(verifyResponseDto);
            } else {
                verifyResponseDto.setMessage("인증코드가 틀립니다.");
                return ResponseEntity.status(400).body(verifyResponseDto);
            }
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
                log.warn(">>> [POST] /user/password/check - 인증코드 만료: {}", emailRequestDto.getMail());
                verifyResponseDto = new VerifyResponseDto(false, "인증코드가 만료되었습니다.");
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(verifyResponseDto);
            } else {
                log.error(">>> [POST] /user/password/check - 비밀번호 찾기 인증코드 검증 중 예외 발생", e);
                throw e;
            }
        }
    }
}
