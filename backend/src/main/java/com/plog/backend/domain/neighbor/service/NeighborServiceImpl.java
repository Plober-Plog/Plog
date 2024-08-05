package com.plog.backend.domain.neighbor.service;

import com.google.common.base.Optional;
import com.plog.backend.domain.neighbor.entity.Neighbor;
import com.plog.backend.domain.neighbor.entity.NeighborType;
import com.plog.backend.domain.neighbor.repository.NeighborRepository;
import com.plog.backend.domain.neighbor.repository.NeighborRepositorySupport;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("neighborService")
@RequiredArgsConstructor
public class NeighborServiceImpl implements NeighborService {
    private final NeighborRepositorySupport neighborRepositorySupport;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final NeighborRepository neighborRepository;

    @Override
    public void addNeighbor(String token, Long neighborId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 이웃 추가: userId={}, neighborId={}", userId, neighborId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findById(neighborId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        Neighbor neighbor = Neighbor.builder()
                .neighborFrom(user)
                .neighborTo(neighborUser)
                .neighborType(NeighborType.NEIGHBOR.getValue())
                .build();

        neighborRepository.save(neighbor);
        log.info(">> 이웃 추가 성공: userId={}, neighborId={}", userId, neighborId);
    }

    @Override
    public void deleteNeighbor(String token, Long neighborId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 이웃 삭제 : userId={}, neighborId={}", userId, neighborId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findById(neighborId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        int neighborType = NeighborType.NEIGHBOR.getValue();

        Optional<Neighbor> neighbor = neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(user, neighborUser, neighborType);
        if (neighbor.isPresent()) {
            neighborRepository.delete(neighbor.get());
            log.info(">>> 이웃 삭제 성공: userId={}, neighborId={}", userId, neighborId);
        } else {
            log.warn(">>> 해당 이웃 관계를 못 찾음: userId={}, neighborId={}", userId, neighborId);
            throw new NotValidRequestException("해당 이웃 관계를 찾을 수 없습니다.");
        }
    }

    @Override
    public void addMutualNeighbor(String token, Long neighborId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 서로 이웃 추가: userId={}, neighborId={}", userId, neighborId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findById(neighborId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        Neighbor neighbor = Neighbor.builder()
                .neighborFrom(user)
                .neighborTo(neighborUser)
                .neighborType(NeighborType.NEIGHBOR.getValue())
                .build();

        neighborRepository.save(neighbor);
        log.info(">>> 서로 이웃 추가 성공: userId={}, neighborId={}", userId, neighborId);
    }

    @Override
    public void deleteMutualNeighbor(String token, Long neighborId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 서로 이웃 삭제: userId={}, neighborId={}", userId, neighborId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotValidRequestException("사용자를 찾을 수 없습니다."));
        User neighborUser = userRepository.findById(neighborId)
                .orElseThrow(() -> new NotValidRequestException("이웃을 찾을 수 없습니다."));

        int neighborType = NeighborType.NEIGHBOR.getValue();

        Optional<Neighbor> neighbor = neighborRepository.findByNeighborFromAndNeighborToAndNeighborType(user, neighborUser, neighborType);
        if (neighbor.isPresent()) {
            neighborRepository.delete(neighbor.get());
            log.info(">>> 서로 이웃 삭제 성공: userId={}, neighborId={}", userId, neighborId);
        } else {
            log.warn(">>> 서로 이웃 관게를 못 찾음: userId={}, neighborId={}", userId, neighborId);
            throw new NotValidRequestException("해당 이웃 관계를 찾을 수 없습니다.");
        }
    }
}
