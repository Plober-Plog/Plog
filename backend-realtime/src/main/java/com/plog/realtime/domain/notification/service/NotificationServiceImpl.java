package com.plog.realtime.domain.notification.service;

import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.Notification;
import com.plog.realtime.domain.notification.entity.NotificationType;
import com.plog.realtime.domain.notification.repository.NotificationRepository;
import com.plog.realtime.domain.plant.entity.Plant;
import com.plog.realtime.domain.plant.repository.PlantRepository;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final FCMService fcmService;

    @Override
    public List<NotificationMessageResponseDto> getNotifications(String searchId) {
        log.info("getNotifications 시작 - searchId: {}", searchId);
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> {
            log.error("getNotifications 실패 - User not found, searchId: {}", searchId);
            return new RuntimeException("User not found");
        });
        List<NotificationMessageResponseDto> notificationMessageResponseDtos = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        for (Notification notification : notifications) {
            notificationMessageResponseDtos.add(NotificationMessageResponseDto.toNotificationDTO(notification));
        }
        log.info("getNotifications 완료 - searchId: {}", searchId);
        return notificationMessageResponseDtos;
    }

    @Override
    @Transactional
    public NotificationMessageResponseDto sendNotification(String requireSource, String targetSearchId, NotificationType type) {
        log.info("sendNotification 시작 - requireSource: {}, targetSearchId: {}, type: {}", requireSource, targetSearchId, type);
        User targetUser = userRepository.findUserBySearchId(targetSearchId).orElseThrow(() -> {
            log.error("sendNotification 실패 - User not found, targetSearchId: {}", targetSearchId);
            return new RuntimeException("User not found");
        });
        log.info("targetUser 정보: {}", targetUser);

        String messageTemplate = type.getDefaultMessage();
        String formattedMessage;

        log.info("messageTemplate: {}", messageTemplate);

        if (type.requiresSource()) {
            User sourceUser = userRepository.findUserBySearchId(requireSource).orElseThrow(() -> {
                log.error("sendNotification 실패 - Source user not found, requireSource: {}", requireSource);
                return new RuntimeException("Source user not found");
            });
            formattedMessage = String.format(messageTemplate, requireSource, targetSearchId);
        } else if (type.requiresPlant()) {
            Plant plant = plantRepository.findById((long) Integer.parseInt(requireSource)).orElseThrow(() -> {
                log.error("sendNotification 실패 - Plant not found, requireSource: {}", requireSource);
                return new RuntimeException("Plant not found");
            });
            formattedMessage = String.format(messageTemplate, targetSearchId, plant.getNickname());
        } else {
            formattedMessage = String.format(messageTemplate, targetSearchId);
        }

        Notification notification = new Notification();
        notification.setUser(targetUser);
        notification.setContent(formattedMessage);
        notification.setType(type.getValue());
        notificationRepository.save(notification);
        log.info("알림 저장 완료 - notificationId: {}", notification.getNotificationId());

        NotificationMessageResponseDto notificationMessageResponseDto = NotificationMessageResponseDto.toNotificationDTO(notification);

        // FCM을 통해 메시지를 전송합니다.
        if (targetUser.isPushNotificationEnabled() && targetUser.getNotificationToken() != null) {
            String title = type.name(); // 여기에 알림 제목을 설정합니다.
            fcmService.sendNotification(targetUser.getNotificationToken(), title, formattedMessage);
        }

        log.info("sendNotification 완료 - requireSource: {}, targetSearchId: {}, type: {}", requireSource, targetSearchId, type);

        return notificationMessageResponseDto;
    }

    @Override
    public void checkPlantNotifications() {
        log.info("checkPlantNotifications 시작");
        List<Plant> plants = plantRepository.findAll();

        for (Plant plant : plants) {
            User user = plant.getUser();
            LocalDate today = LocalDate.now();
            int notifySettings = plant.getNotifySetting(); // 비트마스크 값

            if ((notifySettings & (1 << 0)) > 0) { // 분갈이 체크
                checkRepotNotification(plant, user, today);
            }
            if ((notifySettings & (1 << 1)) > 0) { // 영양제 체크
                checkFertilizeNotification(plant, user, today);
            }
            if ((notifySettings & (1 << 2)) > 0) { // 물 체크
                checkWaterNotification(plant, user, today);
            }
        }
        log.info("checkPlantNotifications 완료");
    }

    private void checkWaterNotification(Plant plant, User user, LocalDate today) {
        log.info("checkWaterNotification 시작 - plantId: {}, userId: {}", plant.getPlantId(), user.getUserId());
        LocalDate waterDate = plant.getWaterDate();
        int waterMid = plant.getPlantType().getWaterMid();
        int waterInterval = plant.getPlantType().getWaterInterval();
        LocalDate startWaterDate = waterDate.minusDays(waterMid - waterInterval);
        LocalDate endWaterDate = waterDate.plusDays(waterMid + waterInterval);

        if (!today.isBefore(startWaterDate) && !today.isAfter(endWaterDate)) {
            sendNotification(plant.getNickname(), user.getSearchId(), NotificationType.WATER_REMINDER);
        }
        log.info("checkWaterNotification 완료 - plantId: {}, userId: {}", plant.getPlantId(), user.getUserId());
    }

    private void checkFertilizeNotification(Plant plant, User user, LocalDate today) {
        log.info("checkFertilizeNotification 시작 - plantId: {}, userId: {}", plant.getPlantId(), user.getUserId());
        LocalDate fertilizeDate = plant.getFertilizeDate();
        int fertilizeMid = plant.getPlantType().getFertilizeMid();
        int fertilizeInterval = plant.getPlantType().getFertilizeInterval();
        LocalDate startFertilizeDate = fertilizeDate.minusDays(fertilizeMid - fertilizeInterval);
        LocalDate endFertilizeDate = fertilizeDate.plusDays(fertilizeMid + fertilizeInterval);

        if (!today.isBefore(startFertilizeDate) && !today.isAfter(endFertilizeDate)) {
            sendNotification(plant.getNickname(), user.getSearchId(), NotificationType.FERTILIZE_REMINDER);
        }
        log.info("checkFertilizeNotification 완료 - plantId: {}, userId: {}", plant.getPlantId(), user.getUserId());
    }

    private void checkRepotNotification(Plant plant, User user, LocalDate today) {
        log.info("checkRepotNotification 시작 - plantId: {}, userId: {}", plant.getPlantId(), user.getUserId());
        LocalDate repotDate = plant.getRepotDate();
        int repotMid = plant.getPlantType().getRepotMid();
        int repotInterval = plant.getPlantType().getRepotInterval();
        LocalDate startRepotDate = repotDate.minusDays(repotMid - repotInterval);
        LocalDate endRepotDate = repotDate.plusDays(repotMid + repotInterval);

        if (!today.isBefore(startRepotDate) && !today.isAfter(endRepotDate)) {
            sendNotification(plant.getNickname(), user.getSearchId(), NotificationType.REPOT_REMINDER);
        }
        log.info("checkRepotNotification 완료 - plantId: {}, userId: {}", plant.getPlantId(), user.getUserId());
    }

    @Override
    public List<NotificationMessageResponseDto> getUnreadNotifications(String searchId) {
        log.info("getUnreadNotifications 시작 - searchId: {}", searchId);
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> {
            log.error("getUnreadNotifications 실패 - User not found, searchId: {}", searchId);
            return new RuntimeException("User not found");
        });
        List<Notification> notifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        List<NotificationMessageResponseDto> notificationMessageResponseDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationMessageResponseDtos.add(NotificationMessageResponseDto.toNotificationDTO(notification));
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
        log.info("getUnreadNotifications 완료 - searchId: {}", searchId);
        return notificationMessageResponseDtos;
    }

    @Override
    public void markAsRead(Long notificationId) {
        log.info("markAsRead 시작 - notificationId: {}", notificationId);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> {
            log.error("markAsRead 실패 - Notification not found, notificationId: {}", notificationId);
            return new RuntimeException("Notification not found");
        });
        notification.setIsRead(true);
        notificationRepository.save(notification);
        log.info("markAsRead 완료 - notificationId: {}", notificationId);
    }
}
