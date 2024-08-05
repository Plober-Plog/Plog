package com.plog.backend.domain.neighbor.service;

import org.springframework.stereotype.Service;

public interface NeighborService {
    boolean addNeighbor(String token, Long userId);
    boolean deleteNeighbor(String token, Long userId);
    boolean addMutualNeighbor(String token, Long userId);
    boolean deleteMutualNeighbor(String token, Long userId);
}
