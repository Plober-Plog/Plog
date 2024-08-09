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

    @Scheduled(cron = "0 55 0 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate0() {
        try {
            log.info("내가 바로 00시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 1 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate1() {
        try {
            log.info("내가 바로 01시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 2 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate2() {
        try {
            log.info("내가 바로 02시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 3 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate3() {
        try {
            log.info("내가 바로 03시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 4 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate4() {
        try {
            log.info("내가 바로 04시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 5 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate5() {
        try {
            log.info("내가 바로 05시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 6 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate6() {
        try {
            log.info("내가 바로 06시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 7 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate7() {
        try {
            log.info("내가 바로 07시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 8 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate8() {
        try {
            log.info("내가 바로 08시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 9 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate9() {
        try {
            log.info("내가 바로 09시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 10 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate10() {
        try {
            log.info("내가 바로 10시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 11 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate11() {
        try {
            log.info("내가 바로 11시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 12 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate12() {
        try {
            log.info("내가 바로 12시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 13 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate13() {
        try {
            log.info("내가 바로 13시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 14 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate14() {
        try {
            log.info("내가 바로 14시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 15 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate15() {
        try {
            log.info("내가 바로 15시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 16 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate16() {
        try {
            log.info("내가 바로 16시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 17 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate17() {
        try {
            log.info("내가 바로 17시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 18 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate18() {
        try {
            log.info("내가 바로 18시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 19 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate19() {
        try {
            log.info("내가 바로 19시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 20 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate20() {
        try {
            log.info("내가 바로 20시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 21 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate21() {
        try {
            log.info("내가 바로 21시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 22 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate22() {
        try {
            log.info("내가 바로 22시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }

    @Scheduled(cron = "0 55 23 * * ?")
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 60000))
    public void scheduleWeatherDataUpdate23() {
        try {
            log.info("내가 바로 23시다.");
            weatherService.updateWeatherData();
        } catch (Exception e) {
            log.error("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작", e);
            throw new WeatherUpdateException("날씨 데이터 업데이트중 에러 발생, 1분 뒤 재시작");
        }
    }
}
