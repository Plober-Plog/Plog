package com.plog.backend.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.service.ImageServiceImpl;
import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.entity.OtherPlantType;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.domain.plant.service.PlantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlantServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PlantServiceTest.class);
    @Mock
    private PlantRepository plantRepository;
    @Mock
    private ImageServiceImpl imageService;
    @InjectMocks
    private PlantServiceImpl plantService;

    @Nested
    @DisplayName("식물 등록")
    class AddPlant {
        private PlantAddRequest plantAddRequest;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @BeforeEach
            void setUp() throws ParseException {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = dateFormat.parse("2024-07-30");
                plantAddRequest = new PlantAddRequest().builder()
                        .profile("imageUrl")
                        .nickname("nickname")
                        .birthDate(birthDate)
                        .hasNotified(false)
                        .isFixed(false).build();
            }

            @Test
            @DisplayName("plantTypeId 객체가 들어와 등록 성공")
            void addPlantSuccessByPlantTypeId() {
                // given
                PlantType plantType = new PlantType();
                plantType.setPlantTypeId(1);
                plantType.setPlantName("기본 식물");
                plantAddRequest.setPlantTypeId(1);

                // 예상되는 결과 객체 생성
                Image mockImage = new Image(plantAddRequest.getProfile());
                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
                Plant mockPlant = new Plant(plantType,
                        plantAddRequest.getNickname(),
                        mockImage, plantAddRequest.getBirthDate());
                when(plantRepository.save(any(Plant.class))).thenReturn(mockPlant);

                // when
                Plant result = plantService.addPlant(plantAddRequest);

                // then
                assertThat(result).isNotNull();
                assertThat(plantType.getPlantName()).isEqualTo("기본 식물");
                assertThat(result.getPlantType().getPlantTypeId()).isEqualTo(plantType.getPlantTypeId());
                assertThat(result.getImage()).isEqualTo(mockImage);
            }

            @Test
            @DisplayName("otherPlantTypeId 객체가 들어와 등록 성공")
            void addPlantSuccessByOtherPlantTypeId() {
                // given
                OtherPlantType otherPlantType = new OtherPlantType();
                otherPlantType.setOtherPlantTypeId(1);
                otherPlantType.setPlantName("기타 식물");
                plantAddRequest.setOtherPlantTypeId(1);

                // 예상되는 결과 객체 생성
                Image mockImage = new Image(plantAddRequest.getProfile());
                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
                Plant mockPlant = new Plant(otherPlantType,
                        plantAddRequest.getNickname(),
                        mockImage, plantAddRequest.getBirthDate());
                when(plantRepository.save(any(Plant.class))).thenReturn(mockPlant);

                // when
                Plant result = plantService.addPlant(plantAddRequest);

                // then
                assertThat(result).isNotNull();
                assertThat(otherPlantType.getPlantName()).isEqualTo("기타 식물");
                assertThat(result.getOtherPlantType().getOtherPlantTypeId()).isEqualTo(otherPlantType.getOtherPlantTypeId());
                assertThat(result.getImage()).isEqualTo(mockImage);
            }
        }

        @Nested
        @DisplayName("비정상 케이스")
        class FailureCase {
            @BeforeEach
            void setUp() throws ParseException {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = dateFormat.parse("2024-07-30");
                plantAddRequest = new PlantAddRequest().builder()
                        .profile("imageUrl")
                        .nickname("nickname")
                        .birthDate(birthDate)
                        .hasNotified(false)
                        .isFixed(false).build();
            }

            @Test
            @DisplayName("plantTypeId의 값이 유효하지 않아 등록 실패")
            void addPlantInvalidPlantTypeIdThrowsException() {
                // given
                plantAddRequest.setPlantTypeId(-100);

                // 예상되는 결과 객체 생성
                Image mockImage = new Image(plantAddRequest.getProfile());
                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);

                // when
                Throwable thrown = catchThrowable(() -> plantService.addPlant(plantAddRequest));

                // then
                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
            }

            @Test
            @DisplayName("plantTypeId와 otherPlantTypeId 값이 둘 다 유효하여 등록 실패")
            void addPlantWithConflictingPlantTypeIdsThrowsException() {
                // given
                plantAddRequest.setPlantTypeId(1);
                plantAddRequest.setOtherPlantTypeId(1);

                // 예상되는 결과 객체 생성
                Image mockImage = new Image(plantAddRequest.getProfile());
                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);

                // when
                Throwable thrown = catchThrowable(() -> plantService.addPlant(plantAddRequest));

                // then
                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
            }

            @Test
            @DisplayName("plantTypeId와 otherPlantTypeId 값이 둘 다 유효하지 않아 등록 실패")
            void addPlantWithBothInvalidPlantTypeIdsThrowsException() {
                // given
                plantAddRequest.setPlantTypeId(-1);
                plantAddRequest.setOtherPlantTypeId(-1);

                // 예상되는 결과 객체 생성
                Image mockImage = new Image(plantAddRequest.getProfile());
                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);

                // when
                Throwable thrown = catchThrowable(() -> plantService.addPlant(plantAddRequest));

                // then
                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
            }
        }
    }
}
