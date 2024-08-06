package com.plog.backend.domain.email.controller;

import com.plog.backend.domain.email.dto.request.EmailRequestDto;
import com.plog.backend.domain.email.dto.request.EmailVerifyRequestDto;
import com.plog.backend.domain.email.dto.response.VerifyResponseDto;
import com.plog.backend.domain.email.service.EmailService;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import com.plog.backend.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Email API", description = "Email 관련 API")
public class EmailController {
    private final EmailService emailService;
    private final UserRepositorySupport userRepositorySupport;

    @Operation(summary = "인증코드 메일 발송", description = "이메일로 인증코드를 발송합니다.")
    @PostMapping("/email/send")
    public ResponseEntity<BaseResponseBody> mailSend(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        log.info(">>> [POST] /user/email/send - 인증코드 메일 발송 요청: {}", emailRequestDto.toString());
        emailService.sendEmail(emailRequestDto.getEmail());
        log.info(">>> [POST] /user/email/send - 인증 메일 전송 완료: {}", emailRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200,"인증 메일이 전송 되었습니다."));
    }

    @Operation(summary = "인증코드 인증", description = "이메일로 받은 인증코드를 확인합니다.")
    @PostMapping("/email/check")
    public ResponseEntity<VerifyResponseDto> verify(@RequestBody EmailVerifyRequestDto emailVerifyRequestDto) {
        log.info(">>> [POST] /user/email/check - 인증코드 검증 요청: {}", emailVerifyRequestDto.toString());
        VerifyResponseDto verifyResponseDto;
        try {
            boolean isVerify = emailService.verifyEmailCode(emailVerifyRequestDto.getEmail(), emailVerifyRequestDto.getVerifyCode());
            log.info(">>> [POST] /user/email/check - 인증코드 검증 결과: {}", isVerify);
            verifyResponseDto = new VerifyResponseDto(isVerify, "", -1L);
            if(isVerify) {
                verifyResponseDto.setMessage("인증코드가 확인 되었습니다.");
                return ResponseEntity.status(HttpStatus.OK).body(verifyResponseDto);
            } else {
                verifyResponseDto.setMessage("인증코드가 틀립니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(verifyResponseDto);
            }
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
                log.warn(">>> [POST] /user/email/check - 인증코드 만료: {}", emailVerifyRequestDto.getEmail());
                verifyResponseDto = new VerifyResponseDto(false, "인증코드가 만료되었습니다.", -1L);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(verifyResponseDto);
            } else {
                log.error(">>> [POST] /user/email/check - 인증코드 검증 중 예외 발생", e);
                throw e;
            }
        }
    }

    @Operation(summary = "비밀번호 찾기 인증코드 메일 발송", description = "비밀번호 찾기를 위한 인증코드를 이메일로 발송합니다.")
    @PostMapping("/password/send")
    public ResponseEntity<BaseResponseBody> findPassword(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        log.info(">>> [POST] /user/password/send - 비밀번호 찾기 인증코드 메일 발송 요청: {}", emailRequestDto);
        emailService.sendEmail(emailRequestDto.getEmail());
        log.info(">>> [POST] /user/password/send - 비밀번호 찾기 인증 메일 전송 완료: {}", emailRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200,"인증 메일이 전송 되었습니다."));
    }

    @Operation(summary = "비밀번호 찾기 인증코드 검증", description = "비밀번호 찾기 위해 이메일로 받은 인증코드를 확인합니다.")
    @PostMapping("/password/check")
    public ResponseEntity<VerifyResponseDto> findPasswordVerify(@RequestBody EmailVerifyRequestDto emailVerifyRequestDto)  {
        log.info(">>> [POST] /user/password/check - 비밀번호 찾기 인증코드 검증 요청: {}", emailVerifyRequestDto);
        VerifyResponseDto verifyResponseDto;
        try {
            boolean isVerify = emailService.verifyEmailCode(emailVerifyRequestDto.getEmail(), emailVerifyRequestDto.getVerifyCode());
            log.info(">>> [POST] /user/password/check - 비밀번호 찾기 인증코드 검증 결과: {}", isVerify);
            verifyResponseDto = new VerifyResponseDto(isVerify, "", userRepositorySupport.findByEmail(emailVerifyRequestDto.getEmail()).getUserId());
            if(isVerify) {
                verifyResponseDto.setMessage("인증코드가 확인 되었습니다.");
                return ResponseEntity.status(HttpStatus.OK).body(verifyResponseDto);
            } else {
                verifyResponseDto.setMessage("인증코드가 틀립니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(verifyResponseDto);
            }
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
                log.warn(">>> [POST] /user/password/check - 인증코드 만료: {}", emailVerifyRequestDto.getEmail());
                verifyResponseDto = new VerifyResponseDto(false, "인증코드가 만료되었습니다.", -1L);
                // 이메일로 userId를 반환
                verifyResponseDto.setUserId(userRepositorySupport.findByEmail(emailVerifyRequestDto.getEmail()).getUserId());
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(verifyResponseDto);
            } else {
                log.error(">>> [POST] /user/password/check - 비밀번호 찾기 인증코드 검증 중 예외 발생", e);
                throw e;
            }
        }
    }
}
