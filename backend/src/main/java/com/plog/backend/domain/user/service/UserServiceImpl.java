package com.plog.backend.domain.user.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.service.ImageServiceImpl;
import com.plog.backend.domain.user.dto.request.*;
import com.plog.backend.domain.user.dto.response.UserCheckPasswordResponseDto;
import com.plog.backend.domain.user.dto.response.UserGetResponseDto;
import com.plog.backend.domain.user.dto.response.UserProfileResponseDto;
import com.plog.backend.domain.user.dto.response.UserPushResponseDto;
import com.plog.backend.domain.user.entity.*;
import com.plog.backend.global.auth.PloberUserDetails;
import com.plog.backend.global.exception.DuplicateEntityException;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import com.plog.backend.global.auth.JwtTokenProvider;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import com.plog.backend.global.util.JwtTokenUtil;
import com.plog.backend.global.util.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;
    private final ImageRepository imageRepository;
    private final ImageServiceImpl imageService;

    @Override
    public User getUserBySearchId(String searchId) {
        log.info(">>> getUserBySearchId - 검색 ID: {}", searchId);
        return userRepository.findUserBySearchId(searchId)
                .orElseThrow(() -> {
                    log.error(">>> getUserBySearchId - 사용자 찾을 수 없음: {}", searchId);
                    return new NotValidRequestException("User not found with searchId: " + searchId);
                });
    }

    @Override
    public UserGetResponseDto getUser(String token) {

        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> getUser - 추출된 사용자 ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(">>> getUser - 사용자를 찾을 수 없음: {}", userId);
            return new NotValidRequestException("사용자를 찾을 수 없습니다.");
        });

        log.info(">>> getUser - 사용자 정보: {}", user);

        return UserGetResponseDto.builder()
                .email(user.getEmail())
                .searchId(user.getSearchId())
                .nickname(user.getNickname())
                .profile(user.getImage().getImageUrl())
                .gender(user.getGender().getValue())
                .birthDate(user.getBirthDate())
                .sidoCode(user.getSidoCode())
                .gugunCode(user.getGugunCode())
                .profileInfo(user.getProfileInfo())
                .isAd(user.isAd())
                .build();
    }

    public Map<String, String> userSignIn(String email, String password, String notificationToken) {
        log.info(">>> [USER SIGN IN] - 사용자 로그인 요청: 이메일 = {}", email);

        // 이메일로 사용자 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(">>> [USER SIGN IN] - 이메일 잘못됨: {}", email);
                    return new NotValidRequestException("이메일 혹은 패스워드가 잘 못 되었습니다.");
                });

        if(user.getState().equals(State.DELETED)) {
            throw new NotValidRequestException("삭제된 유저 입니다.");
        }

        // 패스워드 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error(">>> [USER SIGN IN] - 패스워드 잘못됨: {}", email);
            throw new NotValidRequestException("이메일 혹은 패스워드가 잘 못되었습니다.");
        }

        log.info(">>> login - 사용자 찾음: {}", user);
        // 인증 객체 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), password)
        );

        log.info(">>> login - 인증된 사용자: {}", authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 토큰 생성
        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Redis에 토큰 저장 (Access 토큰: 1시간, Refresh 토큰: 7일)
        redisUtil.setDataExpire("accessToken:" + user.getUserId(), accessToken, 3600);
        redisUtil.setDataExpire("refreshToken:" + user.getUserId(), refreshToken, 604800);

        log.info(">>> [USER SIGN IN] - 사용자 로그인 성공: 유저 ID = {}", user.getUserId());
        log.info(">>> [USER SIGN IN] - Access 토큰: {}", accessToken);
        log.info(">>> [USER SIGN IN] - Refresh 토큰: {}", refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        user.setNotificationToken(notificationToken);
        userRepository.save(user);
        log.info(">>> [USER SIGN IN] - notification 토큰 DB에 업데이트 완료: {}", notificationToken);

        return tokens;
    }

    public void userSignOut(String token) {
        log.info(">>> [USER SIGN OUT] - 사용자 로그아웃 요청: 토큰 = {}", token);

        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        // FCM 토큰 null로 변경
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setNotificationToken(null); // FCM 토큰을 null로 변경
        userRepository.save(user); // 변경된 내용을 DB에 저장

        redisUtil.deleteData("accessToken:" + userId);
        redisUtil.deleteData("refreshToken:" + userId);

        log.info(">>> [USER SIGN OUT] - Redis에서 토큰 삭제 완료: 유저 ID = {}", userId);

        SecurityContextHolder.clearContext();

        log.info(">>> [USER SIGN OUT] - SecurityContextHolder 초기화 완료");
    }

    @Override
    public User createUser(UserSignUpRequestDto userSignUpRequestDto, String imageUrl) {
        log.info(">>> createUser - 사용자 회원가입 데이터: {}, Image url {}", userSignUpRequestDto, imageUrl);

        if(userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent())
            throw new DuplicateEntityException("중복 가입입니다.");

        Image image = new Image(imageUrl);
        imageRepository.save(image);

        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .gender(userSignUpRequestDto.getGender())
                .role(Role.USER.getValue())
                .state(State.ACTIVTE.getValue())
                .profileInfo("안녕하세요!")
                .isAd(userSignUpRequestDto.isAd())
                .nickname(userSignUpRequestDto.getNickname())
                .totalExp(0)
                .image(image)
                .chatAuth(ChatAuth.PUBLIC.getValue())
                .searchId(userSignUpRequestDto.getSearchId())
                .password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
                .sidoCode(userSignUpRequestDto.getSidoCode())
                .gugunCode(userSignUpRequestDto.getGugunCode())
                .source(userSignUpRequestDto.getSource())
                .birthDate(userSignUpRequestDto.getBirthDate())
                .build();
        User savedUser = userRepository.save(user);
        log.info(">>> createUser - 사용자 생성됨: {}", savedUser);
        return savedUser;
    }

    @Override
    public Boolean checkUserSearchId(String searchId) {
        log.info(">>> checkUser - 검색 ID: {}", searchId);
        Optional<User> user = userRepository.findUserBySearchId(searchId);
        boolean isPresent = user.isPresent();

        log.info(">>> checkUser - 사용자 존재 여부: {}", isPresent);
        return isPresent;
    }

    @Override
    public Boolean checkUserEmail(String email) {
        log.info(">>> checkEmail - 이메일: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        boolean isPresent = user.isPresent();
        log.info(">>> checkEmail - 이메일 존재 여부: {}", isPresent);
        return isPresent;
    }

    @Transactional
    @Override
    public User updateUser(String token, UserUpdateRequestDto request, MultipartFile[] profile) {
        log.info(">>> updateUser - 토큰: {}, 요청 데이터: {}", token, request);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> updateUser - 추출된 사용자 ID: {}", userId);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info(">>> updateUser - 사용자 찾음: {}", user);
            // 회원 대표 사진 변경
            if (profile != null) {
                if (profile.length > 1)
                    throw new NotValidRequestException("회원 프로필 사진은 한 장만 등록할 수 있습니다.");
                String[] imageUrl = imageService.uploadImages(profile);
                Image userImage = imageRepository.findByImageUrl(imageUrl[0])
                        .orElseThrow(() -> new EntityNotFoundException("회원의 수정한 대표 사진을 불러오는 데 실패하였습니다."));
                user.setImage(userImage);
            }
            user.setNickname(request.getNickname());
            user.setProfileInfo(request.getProfileInfo());
            user.setGender(Gender.gender(request.getGender()));
            user.setBirthDate(request.getBirthDate());
            user.setSource(request.getSource());
            user.setSidoCode(request.getSidoCode());
            user.setGugunCode(request.getGugunCode());
            user.setSearchId(request.getSearchId());
            User updatedUser = userRepository.save(user);
            log.info(">>> updateUser - 사용자 업데이트됨: {}", updatedUser);
            return updatedUser;
        } else {
            log.error(">>> updateUser - 사용자를 찾을 수 없음: {}", userId);
            throw new EntityNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    @Transactional
    @Override
    public void deleteUser(String token) {
        log.info(">>> deleteUser - 토큰: {}", token);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> deleteUser - 추출된 사용자 ID: {}", userId);

        // 사용자 조회
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(State.DELETED);
            userRepository.save(user);
            log.info(">>> deleteUser - 사용자 삭제 완료: {}", user);
        } else {
            log.error(">>> deleteUser - 사용자를 찾을 수 없음: {}", userId);
            throw new NotValidRequestException("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public UserCheckPasswordResponseDto checkPassword(String token, UserPasswordCheckRequestDto userPasswordCheckRequestDto) {
        log.info(">>> checkPassword - 토큰, RequestDto: {}, {}", token, userPasswordCheckRequestDto.toString());
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> checkPassword - 추출된 사용자 ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));

        boolean result = passwordEncoder.matches(userPasswordCheckRequestDto.getPassword(), user.getPassword());

        log.info(">>> checkPassword - 비교: {}", result);
        UserCheckPasswordResponseDto responseDto = new UserCheckPasswordResponseDto();
        if (result)
            return UserCheckPasswordResponseDto.of(user.getUserId(), 200, "비밀번호가 확인 되었습니다.");
        else
            return UserCheckPasswordResponseDto.of(-1L, 401, "비밀번호가 틀립니다.");
    }

    @Transactional
    @Override
    public void updatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
        log.info(">>> updatePassword - RequestDto: {}", userPasswordUpdateRequestDto);

        User user = userRepository.findById(userPasswordUpdateRequestDto.getUserId())
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(userPasswordUpdateRequestDto.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        log.info(">>> updatePassword - 비밀번호 변경 성공");
    }

    @Override
    public UserProfileResponseDto getMyProfile(String token) {
        log.info(">>> getMyProfile - 토큰: {}", token);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> getMyProfile - 추출된 사용자 ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));

        log.info(">>> getMyProfile - 추출된 사용자 정보: {}", user);

        UserProfileResponseDto responseDto = UserProfileResponseDto.builder()
                .searchId(user.getSearchId())
                .title("기본 칭호")
                .profile_info(user.getProfileInfo())
                .total_exp(user.getTotalExp())
                .nickname(user.getNickname())
                .profile(user.getImage().getImageUrl())
                .build();

        log.info(">>> getMyProfile - 프로필 정보: {}", responseDto);
        return responseDto;
    }

    @Override
    public UserProfileResponseDto getProfile(String searchId) {
        log.info(">>> getProfile - 검색 ID: {}", searchId);

        User user = userRepository.findUserBySearchId(searchId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));

        UserProfileResponseDto responseDto = UserProfileResponseDto.builder()
                .searchId(user.getSearchId())
                .title("기본 칭호")
                .profile_info(user.getProfileInfo())
                .total_exp(user.getTotalExp())
                .nickname(user.getNickname())
                .profile(user.getImage().getImageUrl())
                .build();

        log.info(">>> getProfile - 프로필 정보: {}", responseDto);
        return responseDto;
    }

    @Override
    public UserPushResponseDto getPushUser(String token) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> getPushUser - 추출된 사용자 ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(">>> getPushUser - 사용자를 찾을 수 없음: {}", userId);
            return new NotValidRequestException("사용자를 찾을 수 없습니다.");
        });

        return UserPushResponseDto.builder()
                .isPushNotificationEnabled(user.isPushNotificationEnabled())
                .build();
    }

    @Override
    public User updatePushUser(String token, UserPushRequestDto userPushRequestDto) {
        log.info(">>> updatePushUser - 토큰: {}, 요청 데이터: {}", token, userPushRequestDto);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> updatePushUser - 추출된 사용자 ID: {}", userId);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info(">>> updatePushUser - 사용자 찾음: {}", user);
            user.setPushNotificationEnabled(userPushRequestDto.isPushNotificationEnabled());
            User updatedUser = userRepository.save(user);
            log.info(">>> updatePushUser - 사용자 업데이트됨: {}", updatedUser);
            return updatedUser;
        } else {
            log.error(">>> updatePushUser - 사용자를 찾을 수 없음: {}", userId);
            throw new EntityNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public void notificationUpdate(UserNotificationRequestDto userNotificationRequestDto) {
        JwtTokenUtil jwtTokenUtil = JwtTokenUtil.getInstance();

        Long userId = jwtTokenUtil.getUserIdFromToken(userNotificationRequestDto.getAccessToken());

        User user = userRepository.findById(userId).orElseThrow(() -> new NotValidRequestException("없는 회원입니다."));

        user.setNotificationToken(userNotificationRequestDto.getNotificationToken());
    }

    @Transactional
    public ResponseEntity<?> loginOrRegister(String email, String name, String profileImage, String providerId, int provider) {
        log.info(">>> 소셜로그인 Google 정보 - email {}, name {}, profileImage {}, providerId {}, provider {}",
                email, name, profileImage, providerId, provider);
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isEmpty()) {
            // 회원가입 로직
            Image image = new Image(profileImage);
            imageRepository.save(image);

            user = User.builder()
                    .email(email)
                    .searchId(generateSearchId(email, provider))
                    .nickname(generateSearchId(email, provider))
                    .password(passwordEncoder.encode(providerId))  // OAuth2 로그인의 경우 실제 비밀번호는 사용되지 않음
                    .provider(provider)
                    .providerId(providerId)
                    .image(image)
                    .role(Role.USER.getValue())
                    .gender(Gender.NA.getValue())
                    .state(State.ACTIVTE.getValue())
                    .totalExp(0)
                    .chatAuth(ChatAuth.PUBLIC.getValue())
                    .isPushNotificationEnabled(true)
                    .profileInfo("안녕하세요")
                    .build();
            userRepository.save(user);
            log.info(">>> 회원가입 성공: {}", user);
        } else {
            user = userOptional.get();
            log.info(">>> 로그인 성공: {}", user);
        }

        log.info(">>> login - 사용자 찾음: {}", user);
        // OAuth2 소셜 로그인인 경우 비밀번호 없이 인증 수행
        PloberUserDetails userDetails = new PloberUserDetails(user);  // user는 User 엔티티 객체

        // 인증 객체 생성 시, userId 대신 userDetails 사용
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        // 토큰 생성
        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Redis에 토큰 저장 (Access 토큰: 1시간, Refresh 토큰: 7일)
        redisUtil.setDataExpire("accessToken:" + user.getUserId(), accessToken, 3600);
        redisUtil.setDataExpire("refreshToken:" + user.getUserId(), refreshToken, 604800);

        log.info(">>> [USER SIGN IN] - 사용자 로그인 성공: 유저 ID = {}", user.getUserId());
        log.info(">>> [USER SIGN IN] - Access 토큰: {}", accessToken);
        log.info(">>> [USER SIGN IN] - Refresh 토큰: {}", refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }


    private String generateSearchId(String email, int providerId) {
        if (providerId == 1)
            return email.split("@")[0] + "G";
        else if (providerId == 2)
            return email.split("@")[0] + "K";
        else if (providerId == 3)
            return email.split("@")[0] + "N";
        else
            return email.split("@")[0] + "S";
    }
}
