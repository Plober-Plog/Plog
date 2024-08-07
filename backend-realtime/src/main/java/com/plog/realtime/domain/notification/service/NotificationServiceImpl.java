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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
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
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;


    @Override
    public List<NotificationMessageResponseDto> getNotifications(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new RuntimeException("User not found"));
        List<NotificationMessageResponseDto> notificationMessageResponseDtos = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        for (Notification notification : notifications) {
            notificationMessageResponseDtos.add(NotificationMessageResponseDto
                    .toNotificationDTO(notification));
        }
        return notificationMessageResponseDtos;
    }

    @Override
    @Transactional
    public NotificationMessageResponseDto sendNotification(String requireSource, String targetSearchId, NotificationType type) {
        log.info("sendNotification");
        User targetUser = userRepository.findUserBySearchId(targetSearchId).orElseThrow(() -> new RuntimeException("User not found"));
        log.info(targetUser.toString());
        String messageTemplate = type.getDefaultMessage();
        String formattedMessage;
        log.info(messageTemplate);

        if (type.requiresSource()) {
            User sourceUser = userRepository.findUserBySearchId(requireSource).orElseThrow(() -> new RuntimeException("Source user not found"));
            formattedMessage = String.format(messageTemplate, requireSource, targetSearchId);
        } else if (type.requiresPlant()) {
            Plant plant = plantRepository.findById((long) Integer.parseInt(requireSource)).orElseThrow(() -> new RuntimeException("Plant not found"));
            formattedMessage = String.format(messageTemplate, targetSearchId, plant.getNickname());
        } else {
            formattedMessage = String.format(messageTemplate, targetSearchId);
        }

        Notification notification = new Notification();
        notification.setUser(targetUser);
        notification.setContent(formattedMessage);
        notification.setType(type.getValue());
        notificationRepository.save(notification);
        log.info("완료?");

        NotificationMessageResponseDto notificationMessageResponseDto = NotificationMessageResponseDto.toNotificationDTO(notification);

        // Redis를 통해 메시지를 전송합니다.
        redisTemplate.convertAndSend(topic.getTopic(), notificationMessageResponseDto);
        log.info("완료!");

        return notificationMessageResponseDto;
    }

    @Override
    public void checkPlantNotifications() {
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
    }

    private void checkWaterNotification(Plant plant, User user, LocalDate today) {
        LocalDate waterDate = plant.getWaterDate();
        int waterMid = plant.getPlantType().getWaterMid();
        int waterInterval = plant.getPlantType().getWaterInterval();
        LocalDate startWaterDate = waterDate.minusDays(waterMid - waterInterval);
        LocalDate endWaterDate = waterDate.plusDays(waterMid + waterInterval);

        if (!today.isBefore(startWaterDate) && !today.isAfter(endWaterDate)) {
            sendNotification(plant.getNickname(), user.getSearchId(), NotificationType.WATER_REMINDER);
        }
    }

    private void checkFertilizeNotification(Plant plant, User user, LocalDate today) {
        LocalDate fertilizeDate = plant.getFertilizeDate();
        int fertilizeMid = plant.getPlantType().getFertilizeMid();
        int fertilizeInterval = plant.getPlantType().getFertilizeInterval();
        LocalDate startFertilizeDate = fertilizeDate.minusDays(fertilizeMid - fertilizeInterval);
        LocalDate endFertilizeDate = fertilizeDate.plusDays(fertilizeMid + fertilizeInterval);

        if (!today.isBefore(startFertilizeDate) && !today.isAfter(endFertilizeDate)) {
            sendNotification(plant.getNickname(), user.getSearchId(), NotificationType.FERTILIZE_REMINDER);
        }
    }

    private void checkRepotNotification(Plant plant, User user, LocalDate today) {
        LocalDate repotDate = plant.getRepotDate();
        int repotMid = plant.getPlantType().getRepotMid();
        int repotInterval = plant.getPlantType().getRepotInterval();
        LocalDate startRepotDate = repotDate.minusDays(repotMid - repotInterval);
        LocalDate endRepotDate = repotDate.plusDays(repotMid + repotInterval);

        if (!today.isBefore(startRepotDate) && !today.isAfter(endRepotDate)) {
            sendNotification(plant.getNickname(), user.getSearchId(), NotificationType.REPOT_REMINDER);
        }
    }

    @Override
    public List<NotificationMessageResponseDto> getUnreadNotifications(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Notification> notifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        List<NotificationMessageResponseDto> notificationMessageResponseDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationMessageResponseDtos.add(NotificationMessageResponseDto
                    .toNotificationDTO(notification));
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
        return notificationMessageResponseDtos;
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationMessageResponseDto> getUnsentNotifications(String searchId) {
        User user = userRepository.findUserBySearchId(searchId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Notification> notifications = notificationRepository.findByUserAndIsSentFalseOrderByCreatedAtDesc(user);
        List<NotificationMessageResponseDto> notificationMessageResponseDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationMessageResponseDtos.add(NotificationMessageResponseDto
                    .toNotificationDTO(notification));
            notification.setIsSent(true);
            notificationRepository.save(notification);
        }
        return notificationMessageResponseDtos;
    }

    @Override
    public void markAsSent(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsSent(true);
        notificationRepository.save(notification);
    }


}
