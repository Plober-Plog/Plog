package com.plog.backend.domain.neighbor.service;

import org.springframework.stereotype.Service;

public interface NeighborService {
    void addNeighbor(String token, Long userId);
    void deleteNeighbor(String token, Long userId);
    void addMutualNeighbor(String token, Long userId);
    void deleteMutualNeighbor(String token, Long userId);
}
