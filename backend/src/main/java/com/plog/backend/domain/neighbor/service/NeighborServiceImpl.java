package com.plog.backend.domain.neighbor.service;

import com.plog.backend.domain.neighbor.dto.NeighborMutualAddRequestDto;
import com.plog.backend.domain.neighbor.entity.Neighbor;
import com.plog.backend.domain.neighbor.entity.NeighborType;
import com.plog.backend.domain.neighbor.repository.NeighborRepository;
import com.plog.backend.domain.neighbor.repository.NeighborRepositorySupport;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.exception.InvalidEmailFormatException;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
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
            neighborRepository.delete(neighborTo);
            log.info(">>> 서로 이웃 삭제 성공: userId={}, neighborId={}", userId, neighborSearchId);
        } else {
            neighborFrom.setNeighborType(NeighborType.NEIGHBOR);
            neighborTo.setNeighborType(NeighborType.NEIGHBOR);
            neighborRepository.save(neighborFrom);
            neighborRepository.save(neighborTo);
            log.info(">>> 서로 이웃 관계를 일반 이웃으로 변경: userId={}, neighborId={}", userId, neighborSearchId);
        }
    }
}
