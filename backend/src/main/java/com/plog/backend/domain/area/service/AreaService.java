package com.plog.backend.domain.area.service;

import com.plog.backend.domain.area.dto.response.GugunGetResponseDto;
import com.plog.backend.domain.area.dto.response.SidoGetResponseDto;

import java.util.List;

public interface AreaService {
    List<SidoGetResponseDto> getSidoList();

    List<GugunGetResponseDto> getGugunList(String sidoCode);
}
