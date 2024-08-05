package com.plog.backend.domain.user.controller;

import com.plog.backend.domain.user.dto.request.*;
import com.plog.backend.domain.user.dto.response.UserCheckPasswordResponseDto;
import com.plog.backend.domain.user.dto.response.UserGetResponseDto;
import com.plog.backend.domain.user.dto.response.UserProfileResponseDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.exception.InvalidEmailFormatException;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import com.plog.backend.global.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User API", description = "User 관련 API")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PUT })
public class UserController {
    private final UserServiceImpl userService;
    private final JwtTokenUtil jwtTokenUtil;

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Operation(summary = "회원 가입", description = "회원 가입을 처리합니다.")
    @PostMapping
    public ResponseEntity<BaseResponseBody> createUser(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        log.info(">>> [POST] /user - 회원 가입 요청 데이터: {}", userSignUpRequestDto);
        if(userSignUpRequestDto.getEmail() == null || userSignUpRequestDto.getEmail().trim().isEmpty()) {
            log.error(">>> [POST] /user - 이메일이 필수 필드입니다.");
            throw new NotValidRequestException("email은 필수 필드입니다.");
        }
        if (!userSignUpRequestDto.getEmail().matches(EMAIL_PATTERN)) {
            log.error(">>> [POST] /user - 유효하지 않은 이메일 형식입니다: {}", userSignUpRequestDto.getEmail());
            throw new InvalidEmailFormatException("Invalid email format");
        }
        if(userSignUpRequestDto.getPassword() == null || userSignUpRequestDto.getPassword().trim().isEmpty()) {
            log.error(">>> [POST] /user - 비밀번호가 필수 필드입니다.");
            throw new NotValidRequestException("password는 필수 필드입니다.");
        }
        if(userSignUpRequestDto.getSearchId() == null || userSignUpRequestDto.getSearchId().trim().isEmpty()) {
            log.error(">>> [POST] /user - 검색 ID가 필수 필드입니다.");
            throw new NotValidRequestException("검색 ID는 필수 입력 값입니다.");
        }
        if(userSignUpRequestDto.getNickname() == null || userSignUpRequestDto.getNickname().trim().isEmpty()) {
            log.error(">>> [POST] /user - 닉네임이 필수 필드입니다.");
            throw new NotValidRequestException("닉네임은 필수 입력 값입니다.");
        }

        User user = userService.createUser(userSignUpRequestDto);
        log.info(">>> [POST] /user - 회원 가입 완료: {}", user);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원가입이 완료되었습니다."));
    }

    @Operation(summary = "회원 수정", description = "회원 정보를 수정합니다.")
    @PatchMapping
    public ResponseEntity<BaseResponseBody> updateUser(@RequestHeader("Authorization") String token, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info(">>> [PATCH] /user - 회원 수정 요청 데이터: {}", userUpdateRequestDto);

        if(userUpdateRequestDto.getNickname() == null || userUpdateRequestDto.getNickname().trim().isEmpty()) {
            log.error(">>> [PATCH] /user - 닉네임이 필수 필드입니다.");
            throw new NotValidRequestException("닉네임은 필수 입력 값입니다.");
        }

        if(userUpdateRequestDto.getSearchId() == null || userUpdateRequestDto.getSearchId().trim().isEmpty()) {
            log.error(">>> [PATCH] /user - 검색 ID가 필수 필드입니다.");
            throw new NotValidRequestException("검색 ID는 필수 입력 값입니다.");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.info(">>> [PATCH] /user - Bearer 제거 후 토큰: {}", token);
        }

        User user = userService.updateUser(token, userUpdateRequestDto);
        log.info(">>> [PATCH] /user - 회원 수정 완료: {}", user);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정이 완료되었습니다."));
    }

    @Operation(summary = "중복 아이디 확인", description = "사용 가능한 검색 ID인지 확인합니다.")
    @GetMapping("/{searchId}")
    public ResponseEntity<BaseResponseBody> checkSearchId(@PathVariable("searchId") String searchId) {
        log.info(">>> [GET] /user/{} - 아이디 확인 요청", searchId);
        boolean result = userService.checkUserSearchId(searchId);

        if(result) {
            log.warn(">>> [GET] /user/{} - 이미 존재하는 ID 입니다.", searchId);
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 존재하는 ID 입니다."));
        } else {
            log.info(">>> [GET] /user/{} - 없는 검색 ID 입니다.", searchId);
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "없는 검색 ID 입니다."));
        }
    }

    @Operation(summary = "이메일 중복 확인", description = "사용 가능한 이메일인지 확인합니다.")
    @PostMapping("/email")
    public ResponseEntity<BaseResponseBody> checkEmail(@RequestBody UserEmailCheckRequestDto userEmailCheckRequestDto) {
        log.info(">>> [POST] /user/email - 이메일 중복 확인 요청 데이터: {}", userEmailCheckRequestDto);
        boolean result = userService.checkUserEmail(userEmailCheckRequestDto.getEmail());
        if(result) {
            log.warn(">>> [POST] /user/email - 이미 존재하는 Email 입니다.");
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 존재하는 Email 입니다."));
        } else {
            log.info(">>> [POST] /user/email - 없는 검색 Email 입니다.");
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "없는 검색 Email 입니다."));
        }
    }

    @Operation(summary = "로그인", description = "로그인을 처리하고 JWT 토큰을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        log.info(">>> [POST] /user/login - 로그인 요청 데이터: {}", userSignInRequestDto);
        try {
            Map<String, String> tokens = userService.userSignIn(userSignInRequestDto.getEmail(), userSignInRequestDto.getPassword());
            log.info(">>> [POST] /user/login - 로그인 성공, 토큰: {}", tokens);
            return ResponseEntity.status(200).body(tokens);
        } catch (Exception e) {
            log.error(">>> [POST] /user/login - 로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(401).body(BaseResponseBody.of(401, "아이디 혹은 비밀번호가 맞지 않습니다."));
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 처리하고 Redis에서 토큰을 삭제합니다.")
    @GetMapping("/logout")
    public ResponseEntity<BaseResponseBody> logout(@RequestHeader("Authorization") String token) {
        log.info(">>> [GET] /user/logout - 로그아웃 요청: {}", token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.info(">>> [GET] /user/logout - Bearer 제거 후 토큰: {}", token);
        }
        userService.userSignOut(token);
        log.info(">>> [GET] /user/logout - 로그아웃 완료");
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "로그아웃이 완료되었습니다."));
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<UserGetResponseDto> getUser(@RequestHeader("Authorization") String token) {
        log.info(">>> [GET] /user - 회원 정보 조회 요청: {}", token);
        UserGetResponseDto userGetResponseDto = userService.getUser(token);
        log.info(">>> [GET] /user - 회원 정보 조회 완료: {}", userGetResponseDto);
        return ResponseEntity.status(200).body(userGetResponseDto);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 처리합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponseBody> deleteUser(@RequestHeader("Authorization") String token) {
        log.info(">>> [DELETE] /user - 회원 탈퇴 요청: {}", token);

        userService.deleteUser(token);

        log.info(">>> [DELETE] /user - 회원 탈퇴 완료");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponseBody.of(204, "회원 탈퇴 되었습니다."));
    }

    @Operation(summary = "현재 비밀번호 확인", description = "현재 비밀번호를 확인합니다.")
    @PostMapping("/password")
    public ResponseEntity<UserCheckPasswordResponseDto> checkPassword(@RequestHeader("Authorization") String token, @RequestBody UserPasswordCheckRequestDto userPasswordCheckRequestDto) {
        log.info(">>> [POST] /user/password - 현재 비밀번호 확인 요청 : {}", token);
        UserCheckPasswordResponseDto userCheckPasswordResponseDto = userService.checkPassword(token, userPasswordCheckRequestDto);
        log.info(">>> [POST] /user/password - 비밀번호 확인 결과: {}", userCheckPasswordResponseDto);
        return ResponseEntity.status(userCheckPasswordResponseDto.getStatusCode()).body(userCheckPasswordResponseDto);
    }

    @Operation(summary = "비밀번호 수정", description = "비밀번호를 수정합니다.")
    @PatchMapping("/password")
    public ResponseEntity<BaseResponseBody> updatePassword(@RequestBody UserPasswordUpdateRequestDto requestDto) {
        log.info(">>> [PATCH] /user/password - 비밀번호 수정 요청: {}", requestDto);
        try {
            userService.updatePassword(requestDto);
            log.info(">>> [PATCH] /user/password - 비밀번호 수정 완료");
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "비밀번호가 성공적으로 변경되었습니다."));
        } catch (IllegalArgumentException e) {
            log.error(">>> [PATCH] /user/password - 비밀번호 수정 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, e.getMessage()));
        } catch (Exception e) {
            log.error(">>> [PATCH] /user/password - 내부 서버 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseBody.of(500, "내부 서버 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "내 프로필 조회", description = "내 프로필을 조회합니다.")
    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(@RequestHeader("Authorization") String token) {
        UserProfileResponseDto responseDto = userService.getMyProfile(token);
        log.info(">>> 내 프로필 조회 반환값 : {}",responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "프로필 조회", description = "검색 ID 기준으로 프로필을 조회합니다.")
    @GetMapping("/profile/{searchId}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable("searchId") String searchId) {
        UserProfileResponseDto responseDto = userService.getProfile(searchId);
        log.info(">>> 상대 프로필 조회, 아이디, 반환값 : {}, {}", searchId, responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "이메일 사용자 조회", description = "이메일 기준으로 사용자를 조회합니다.")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserGetResponseDto> getUserByEmail(@PathVariable("email") String email) {
        UserGetResponseDto responseDto = userService.getUser(email);
        log.info(">>> 상대 프로필 조회, 이메일, 반환값 : {}, {}", email, responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
