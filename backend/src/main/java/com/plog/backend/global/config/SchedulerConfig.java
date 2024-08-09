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

    @Scheduled(cron = "0 48 0 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 00시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 1 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 01시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 2 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 02시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 3 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 03시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 4 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 04시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 5 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 05시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 6 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 06시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 7 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 07시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 8 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 08시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 9 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 09시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 10 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 10시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 11 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 11시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 12 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 12시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 13 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 13시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 14 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 14시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 15 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 15시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 16 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 16시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 17 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 17시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 18 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 18시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 19 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 19시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 20 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 20시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 21 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 21시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 22 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 22시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 48 23 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate() {
        try {
            log.info("내가 바로 23시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }
}
