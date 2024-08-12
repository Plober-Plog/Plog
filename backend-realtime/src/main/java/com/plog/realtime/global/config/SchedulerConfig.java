package com.plog.realtime.global.config;

import com.plog.realtime.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final NotificationService notificationService;

    // @Scheduled(cron = "0 0 23 * * *")  // 매일 오전 8시, 11시에 실행
    @Scheduled(cron = "0 45 7 * * *")
    public void schedulePlantNotifications() {
        notificationService.checkPlantNotifications();
    }
}
