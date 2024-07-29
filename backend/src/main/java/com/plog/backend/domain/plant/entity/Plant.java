package com.plog.backend.domain.plant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plant {
    @Id
    int plantId;
    @Column(nullable = false)
    String nickname;
    LocalDateTime birthDate;
    LocalDateTime deadDate;
    boolean hasNotified;
    int fixed;
    LocalDateTime waterDate;
    LocalDateTime fertilizeDate;
    LocalDateTime repotDate;
    boolean isDeleted;

}
