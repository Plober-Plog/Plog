package com.plog.backend.domain.area.service;

import com.plog.backend.domain.area.dto.response.GugunGetResponseDto;
import com.plog.backend.domain.area.dto.response.SidoGetResponseDto;
import com.plog.backend.domain.area.entity.Gugun;
import com.plog.backend.domain.area.entity.Sido;
import com.plog.backend.domain.area.repository.GugunRepository;
import com.plog.backend.domain.area.repository.SidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service("areaService")
public class AreaServiceImpl implements AreaService {
    private final SidoRepository sidoRepository;
    private final GugunRepository gugunRepository;

    @Override
    public List<SidoGetResponseDto> getSidoList() {
        List<SidoGetResponseDto> sidoGetResponseDtoList = new ArrayList<>();
        List<Sido> sidoList = sidoRepository.findAll();
        for (Sido sido : sidoList) {
            sidoGetResponseDtoList.add(
                    new SidoGetResponseDto(
                            sido.getSidoCode(), sido.getSidoName()
                    )
            );
        }
        return sidoGetResponseDtoList;
    }

    @Override
    public List<GugunGetResponseDto> getGugunList(String sidoCode) {
        List<GugunGetResponseDto> gugunGetResponseDtoList = new ArrayList<>();
        List<Gugun> gugunList = gugunRepository.findBySidoSidoCode(Integer.valueOf(sidoCode));
        for (Gugun gugun : gugunList) {
            gugunGetResponseDtoList.add(
                    new GugunGetResponseDto(
                            gugun.getGugunCode(), gugun.getGugunName(), gugun.getSido().getSidoCode()
                    ));
        }
        return gugunGetResponseDtoList;
    }
}
