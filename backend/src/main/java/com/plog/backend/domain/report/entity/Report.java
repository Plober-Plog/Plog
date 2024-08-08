package com.plog.backend.domain.report.entity;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Report {

    // 외부 데이터
    List<Integer> humidity;
    List<Integer> temperature;
    List<Integer> rainy;

    // 식물 데이터
    List<Integer> water;
    List<Integer> fertilize;
    List<Integer> repot;

    // 결과값 저장
    Map<String, String> result;
}
