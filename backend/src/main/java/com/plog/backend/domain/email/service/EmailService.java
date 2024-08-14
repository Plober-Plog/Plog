package com.plog.backend.domain.email.service;

import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private static final String senderEmail = "sanbyul1@naver.com";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 이메일 내용 초기화
    private String setContext(String code) {
        log.info(">>> setContext - 인증코드 설정: {}", code);
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", code);

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }

    // 이메일 폼 생성
    private MimeMessage createEmailForm(String email) throws MessagingException {
        log.info(">>> createEmailForm - 이메일 폼 생성 시작: {}", email);
        String authCode = createCode();

        log.info("authCode: {}", authCode);

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("안녕하세요. 인증번호입니다.");
        message.setFrom(senderEmail);
        message.setText(setContext(authCode), "utf-8", "html");

        // Redis 에 해당 인증코드 인증 시간 설정
        redisUtil.setDataExpire(email, authCode, 60 * 5L);  // 5분으로 변경
        log.info(">>> createEmailForm - 이메일 폼 생성 완료: {}", email);

        return message;
    }

    // 이메일 유효성 검사
    public boolean isValidEmail(String email) {
        log.info(">>> isValidEmail - 이메일 유효성 검사: {}", email);
        if (email == null) {
            log.warn(">>> isValidEmail - 이메일이 null입니다.");
            return false; // null인 경우 false 반환
        }
        boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        log.info(">>> isValidEmail - 유효성 검사 결과: {} - {}", email, isValid);
        return isValid;
    }

    // 인증코드 이메일 발송
    public void sendEmail(String toEmail) throws MessagingException {
        log.info(">>> sendEmail - 인증코드 이메일 발송 시작: {}", toEmail);

        if (!isValidEmail(toEmail)) {
            log.warn(">>> sendEmail - 유효하지 않은 이메일 주소입니다: {}", toEmail);
            throw new NotValidRequestException("유효하지 않은 이메일 주소입니다: " + toEmail);
        }

        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
            log.info(">>> sendEmail - 기존 인증코드 삭제: {}", toEmail);
        }

        try {
            // 이메일 폼 생성
            MimeMessage emailForm = createEmailForm(toEmail);

            // 이메일 발송
            javaMailSender.send(emailForm);
            log.info(">>> sendEmail - 인증코드 이메일 발송 완료: {}", toEmail);

        } catch (MailSendException ex) {
            log.error(">>> sendEmail - 메일 발송 실패: {}", ex.getMessage());
            throw new MessagingException("메일 발송에 실패했습니다: " + ex.getMessage(), ex);

        } catch (MailException ex) {
            log.error(">>> sendEmail - 메일 예외 발생: {}", ex.getMessage());
            throw new MessagingException("메일 발송 중 오류가 발생했습니다: " + ex.getMessage(), ex);

        } catch (Exception ex) {
            log.error(">>> sendEmail - 알 수 없는 오류 발생: {}", ex.getMessage());
            throw new MessagingException("메일 발송 중 알 수 없는 오류가 발생했습니다: " + ex.getMessage(), ex);
        }
    }


    // 코드 검증
    public Boolean verifyEmailCode(String email, String code) {
        log.info(">>> verifyEmailCode - 인증코드 검증 시작: 이메일={}, 코드={}", email, code);
        String codeFoundByEmail = redisUtil.getData(email);
        log.info(">>> verifyEmailCode - Redis에서 찾은 코드: {}", codeFoundByEmail);
        if (codeFoundByEmail == null) {
            log.warn(">>> verifyEmailCode - 인증코드가 만료되었습니다: {}", email);
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "인증코드가 만료되었습니다.");
        }
        boolean isVerified = codeFoundByEmail.equals(code);
        log.info(">>> verifyEmailCode - 인증코드 검증 결과: 이메일={}, 결과={}", email, isVerified);
        return isVerified;
    }
}
