package com.plog.backend.domain.neighbor.service;

import com.plog.backend.domain.neighbor.dto.NeighborMutualAddRequestDto;
import org.springframework.stereotype.Service;

public interface NeighborService {
    void addNeighbor(String token, String neighborSearchId);
    void deleteNeighbor(String token, String neighborSearchId);
    void addMutualNeighbor(String token, String neighborSearchId);
    void deleteMutualNeighbor(String token, NeighborMutualAddRequestDto neighborMutualAddRequestDto);
}
