package com.plog.backend.domain.neighbor.service;

import com.plog.backend.domain.neighbor.dto.request.NeighborMutualAddRequestDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborCheckResponseDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborFromResponseDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborToResponseDto;
import com.plog.backend.domain.neighbor.entity.Neighbor;
import com.plog.backend.domain.neighbor.entity.NeighborType;
import com.plog.backend.domain.neighbor.repository.NeighborRepository;
import com.plog.backend.domain.neighbor.repository.NeighborRepositorySupport;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("neighborService")
@RequiredArgsConstructor
public class NeighborServiceImpl implements NeighborService {

    @Value("${server.url}")
    private String serverUrl;

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final NeighborRepository neighborRepository;
    private final NeighborRepositorySupport neighborRepositorySupport;

    private NeighborToResponseDto mapToDto(Neighbor neighbor) {
        return NeighborToResponseDto.builder()
                .searchId(neighbor.getNeighborFrom().getSearchId())
                .profile(neighbor.getNeighborFrom().getImage().getImageUrl()) // User 엔티티에 image 필드가 있다고 가정
                .nickname(neighbor.getNeighborFrom().getNickname())
                .neighborType(neighbor.getNeighborType().getValue())
                .build();
    }

    private NeighborFromResponseDto mapFromDto(Neighbor neighbor) {
        return NeighborFromResponseDto.builder()
                .searchId(neighbor.getNeighborTo().getSearchId())
                .profile(neighbor.getNeighborTo().getImage().getImageUrl()) // User 엔티티에 image 필드가 있다고 가정
                .nickname(neighbor.getNeighborTo().getNickname())
                .neighborType(neighbor.getNeighborType().getValue())
                .build();
    }

    @Override
    public void addNeighbor(String token, String neighborSearchId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 이웃 추가: userId={}, neighborId={}", userId, neighborSearchId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findUserBySearchId(neighborSearchId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        if(neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(user, neighborUser, NeighborType.NEIGHBOR.getValue()).isPresent())
            throw new NotValidRequestException("이미 있는 이웃입니다.");

        Neighbor neighbor = Neighbor.builder()
                .neighborFrom(user)
                .neighborTo(neighborUser)
                .neighborType(NeighborType.NEIGHBOR.getValue())
                .build();

        neighborRepository.save(neighbor);
        log.info(">> 이웃 추가 성공: userId={}, neighborId={}", userId, neighborSearchId);

        // 이웃 신청 알림 보내기
        String sourceSearchId = user.getSearchId();
        String targetSearchId = neighborUser.getSearchId();
        String articleUrl = String.format("%s/profile/%d", serverUrl, sourceSearchId);
        if (!sourceSearchId.equals(targetSearchId)) {
            String type = "NEIGHBOR_REQUEST";
            String urlString = String.format("%s/realtime/notification/send?sourceSearchId=%s&targetSearchId=%s&clickUrl=%s&type=%s",
                    serverUrl, sourceSearchId, targetSearchId, articleUrl, type);
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");

                int responseCode = conn.getResponseCode();
                log.info("알림 전송 HTTP 응답 코드: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                log.error("알림 전송 중 오류 발생", e);
            }
        }
    }

    @Override
    public void deleteNeighbor(String token, String neighborSearchId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 이웃 삭제 : userId={}, neighborId={}", userId, neighborSearchId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findUserBySearchId(neighborSearchId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        Neighbor neighbor = neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(user, neighborUser, NeighborType.NEIGHBOR.getValue())
                .orElseThrow(() -> new NotValidRequestException("해당 이웃 관계를 찾을 수 없습니다."));

        neighborRepository.delete(neighbor);
        log.info(">>> 이웃 삭제 성공: userId={}, neighborId={}", userId, neighborSearchId);
    }

    @Transactional
    @Override
    public void addMutualNeighbor(String token, String neighborSearchId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 서로 이웃 추가: userId={}, neighborId={}", userId, neighborSearchId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findUserBySearchId(neighborSearchId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        if(neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(user, neighborUser, 2).isPresent())
            throw new NotValidRequestException("이미 있는 서로 이웃입니다.");

        Neighbor neighborFrom = Neighbor.builder()
                .neighborFrom(user)
                .neighborTo(neighborUser)
                .neighborType(NeighborType.MUTUAL_NEIGHBOR.getValue())
                .build();
        neighborRepository.save(neighborFrom);

        Neighbor neighborTo = Neighbor.builder()
                .neighborFrom(neighborUser)
                .neighborTo(user)
                .neighborType(NeighborType.MUTUAL_NEIGHBOR.getValue())
                .build();
        neighborRepository.save(neighborTo);

        log.info(">>> 서로 이웃 추가 성공: userId={}, neighborId={}", userId, neighborSearchId);

        // 서로 이웃 신청 알림 보내기
        String sourceSearchId = user.getSearchId();
        String targetSearchId = neighborUser.getSearchId();
        String articleUrl = String.format("%s/profile/%d", serverUrl, sourceSearchId);
        if (!sourceSearchId.equals(targetSearchId)) {
            String type = "M_NEIGHBOR_REQUEST";
            String urlString = String.format("%s/realtime/notification/send?sourceSearchId=%s&targetSearchId=%s&clickUrl=%s&type=%s",
                    serverUrl, sourceSearchId, targetSearchId, articleUrl, type);
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");

                int responseCode = conn.getResponseCode();
                log.info("알림 전송 HTTP 응답 코드: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                log.error("알림 전송 중 오류 발생", e);
            }
        }
    }

    @Transactional
    @Override
    public void deleteMutualNeighbor(String token, NeighborMutualAddRequestDto neighborMutualAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        String neighborSearchId = neighborMutualAddRequestDto.getNeighborSearchId();
        boolean isDelete = neighborMutualAddRequestDto.getIsDelete();

        log.info(">>> 서로 이웃 삭제: userId={}, neighborId={}", userId, neighborSearchId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findUserBySearchId(neighborSearchId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        Neighbor neighborFrom = neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(user, neighborUser, NeighborType.MUTUAL_NEIGHBOR.getValue())
                .orElseThrow(() -> new NotValidRequestException("해당 서로 이웃 관계를 찾을 수 없습니다."));
        Neighbor neighborTo = neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(neighborUser, user, NeighborType.MUTUAL_NEIGHBOR.getValue())
                .orElseThrow(() -> new NotValidRequestException("해당 서로 이웃 관계를 찾을 수 없습니다."));

        if (isDelete) {
            neighborRepository.delete(neighborFrom);
            neighborTo.setNeighborType(NeighborType.NEIGHBOR);
            neighborRepository.save(neighborTo);
            log.info(">>> 서로 이웃 삭제 성공: userId={}, neighborId={}", userId, neighborSearchId);
        } else {
            neighborFrom.setNeighborType(NeighborType.NEIGHBOR);
            neighborTo.setNeighborType(NeighborType.NEIGHBOR);
            neighborRepository.save(neighborFrom);
            neighborRepository.save(neighborTo);
            log.info(">>> 서로 이웃 관계를 일반 이웃으로 변경: userId={}, neighborId={}", userId, neighborSearchId);
        }
    }

    @Override
    public List<NeighborToResponseDto> getNeighborTo(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new NotValidRequestException("NeighborService : 이웃이 없습니다."));
        return neighborRepository.findByNeighborTo(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getNeighborToCount(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new NotValidRequestException("NeighborService : 이웃이 없습니다."));
        return neighborRepository.countByNeighborTo(user);
    }

    @Override
    public List<NeighborFromResponseDto> getNeighborFrom(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new NotValidRequestException("User not found"));
        return neighborRepository.findByNeighborFrom(user).stream()
                .map(this::mapFromDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getNeighborFromCount(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new NotValidRequestException("User not found"));
        return neighborRepository.countByNeighborFrom(user);
    }

    @Override
    public List<NeighborToResponseDto> getMutualNeighborFrom(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new NotValidRequestException("User not found"));
        return neighborRepository.findByNeighborToAndNeighborType(user, NeighborType.MUTUAL_NEIGHBOR.getValue()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getMutualNeighborFromCount(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new NotValidRequestException("User not found"));
        return neighborRepository.findByNeighborToAndNeighborType(user, NeighborType.MUTUAL_NEIGHBOR.getValue()).size();
    }

    @Override
    public NeighborCheckResponseDto checkNeighbor(String token, String searchId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        User neighbor = userRepository.findUserBySearchId(searchId)
                .orElseThrow(() -> new NotValidRequestException("NeighborService : 이웃이 없습니다."));

        Integer requestUserRel = neighborRepositorySupport.findNeighborTypeByNeighborToAndNeighborFrom(userId, neighbor.getUserId());
        Integer profileUserRel = neighborRepositorySupport.findNeighborTypeByNeighborToAndNeighborFrom(neighbor.getUserId(), userId);

        log.info(">>> checkNaighbor requestUserRel: {}", requestUserRel);
        log.info(">>> checkNaighbor profileUserRel: {}", profileUserRel);

        NeighborCheckResponseDto neighborCheckResponseDto = NeighborCheckResponseDto
                .builder()
                .requestUserRel(requestUserRel != null ? requestUserRel : 0)
                .profileUserRel(profileUserRel != null ? profileUserRel : 0)
                .build();
        return neighborCheckResponseDto;
    }
}
