package com.plog.backend.domain.email.controller;

import com.plog.backend.domain.email.dto.RequestEmailDto;
import com.plog.backend.domain.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/email")
public class EmailController {
    private final EmailService emailService;

    // 인증코드 메일 발송
    @PostMapping("/send")
    public String mailSend(@RequestBody RequestEmailDto request) throws MessagingException {
        log.info("EmailController.mailSend() : {}", request.toString());
        emailService.sendEmail(request.getMail());
        return "인증코드가 발송되었습니다.";
    }

    // 인증코드 인증
    @PostMapping("/check")
    public String verify(@RequestBody RequestEmailDto request) {
        log.info("EmailController.verify() : {}", request.toString());
        boolean isVerify = emailService.verifyEmailCode(request.getMail(), request.getVerifyCode());
        return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }
}