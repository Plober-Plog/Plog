package com.plog.backend.domain.area.controller;

import com.plog.backend.domain.area.dto.response.GugunGetResponseDto;
import com.plog.backend.domain.area.dto.response.SidoGetResponseDto;
import com.plog.backend.domain.area.service.AreaService;
import com.plog.backend.global.exception.NotValidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/area")
public class AreaController {
    private final AreaService areaService;

    @GetMapping("/sido")
    public ResponseEntity<List<SidoGetResponseDto>> getSidoList() {
        List<SidoGetResponseDto> sidoGetResponseDtoList = areaService.getSidoList();
        return ResponseEntity.status(200).body(sidoGetResponseDtoList);
    }

    @GetMapping("/gugun/{sidoCode}")
    public ResponseEntity<List<GugunGetResponseDto>> getGugunList(@PathVariable String sidoCode) {
        List<GugunGetResponseDto> gugunGetResponseDtoList = areaService.getGugunList(sidoCode);
        return ResponseEntity.ok().body(gugunGetResponseDtoList);
    }
}
