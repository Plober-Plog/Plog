package com.plog.backend.domain;

import com.plog.backend.domain.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forever")
public class CheckController {

    @Autowired
    WeatherService weatherService;

    @GetMapping
    public String test() {
        StringBuilder sb = new StringBuilder();
        sb.append("처음 하는 거라 힘들텐데 아주 잘 해주어 항상 감사하군요\n");
        sb.append("피해가 되지 않게 저도 열심히 한 번 해보겠습니다 !!!!!!!!!!!!!!!!!! \n");
        sb.append("우리팀 파이팅 힘내세요 파이팅~~!!!!!!!!!!!!!");
        return sb.toString();
    }

    @GetMapping("/weather/test")
    public String updateWeatherData() {
        weatherService.updateWeatherData();
        return "Weather data updated successfully.";
    }


}
