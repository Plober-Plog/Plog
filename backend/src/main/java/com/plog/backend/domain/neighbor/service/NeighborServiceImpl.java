package com.plog.backend.domain.neighbor.service;

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
        User user = userRepository.findById(userId).get();
        User neighborUser = userRepository.findById(neighborId)
                .orElseThrow(() -> {
                    return new NotValidRequestException("이웃을 찾을 수 없습니다.");
                });

        Neighbor neighbor = Neighbor.builder()
                .neighborFrom(user)
                .neighborTo(neighborUser)
                .neighborType(NeighborType.NEIGHBOR.getValue())
                .build();
        
        neighborRepository.save(neighbor);
    }

    @Override
    public void deleteNeighbor(String token, Long userId) {

    }

    @Override
    public void addMutualNeighbor(String token, Long userId) {

    }

    @Override
    public void deleteMutualNeighbor(String token, Long userId) {

    }
}
