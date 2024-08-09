package com.plog.backend.global.config;

import com.plog.backend.domain.weather.exception.WeatherUpdateException;
import com.plog.backend.domain.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final WeatherService weatherService;

    @Scheduled(cron = "0 0 11 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }
}
