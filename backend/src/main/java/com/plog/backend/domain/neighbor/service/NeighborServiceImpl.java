package com.plog.backend.domain.neighbor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("neighborService")
@RequiredArgsConstructor
public class NeighborServiceImpl implements NeighborService {

    @Override
    public boolean addNeighbor(String token, Long userId) {
        return false;
    }

    @Override
    public boolean deleteNeighbor(String token, Long userId) {
        return false;
    }

    @Override
    public boolean addMutualNeighbor(String token, Long userId) {
        return false;
    }

    @Override
    public boolean deleteMutualNeighbor(String token, Long userId) {
        return false;
    }
}
