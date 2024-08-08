package com.plog.backend.domain.neighbor.service;

import com.plog.backend.domain.neighbor.dto.request.NeighborMutualAddRequestDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborCheckResponseDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborFromResponseDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborToResponseDto;

import java.util.List;

public interface NeighborService {
    void addNeighbor(String token, String neighborSearchId);
    void deleteNeighbor(String token, String neighborSearchId);
    void addMutualNeighbor(String token, String neighborSearchId);
    void deleteMutualNeighbor(String token, NeighborMutualAddRequestDto neighborMutualAddRequestDto);
    List<NeighborToResponseDto>  getNeighborTo(String searchId);
    int getNeighborToCount(String searchId);
    List<NeighborFromResponseDto>  getNeighborFrom(String searchId);
    int getNeighborFromCount(String searchId);
    List<NeighborToResponseDto>  getMutualNeighborFrom(String searchId);
    int getMutualNeighborFromCount(String searchId);
    NeighborCheckResponseDto checkNeighbor(String token, String searchId);
}
