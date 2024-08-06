//package com.plog.backend.service;
//
//import com.plog.backend.domain.image.entity.Image;
//import com.plog.backend.domain.image.service.ImageServiceImpl;
//import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
//import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
//import com.plog.backend.domain.plant.entity.OtherPlantType;
//import com.plog.backend.domain.plant.entity.Plant;
//import com.plog.backend.domain.plant.entity.PlantType;
//import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
//import com.plog.backend.domain.plant.repository.PlantRepository;
//import com.plog.backend.domain.plant.service.PlantServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchThrowable;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class PlantServiceTest {
//
//    private static final Logger log = LoggerFactory.getLogger(PlantServiceTest.class);
//    @Mock
//    private PlantRepository plantRepository;
//    @Mock
//    private ImageServiceImpl imageService;
//    @InjectMocks
//    private PlantServiceImpl plantService;
//
//    @Nested
//    @DisplayName("식물 등록")
//    class AddPlant {
//        PlantAddRequest plantAddRequest;
//        PlantType plantType;
//        OtherPlantType otherPlantType;
//
//        @Nested
//        @DisplayName("정상 케이스")
//        class SuccessCase {
//            @BeforeEach
//            void setUp() throws ParseException {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantAddRequest = new PlantAddRequest().builder()
//                        .profile("imageUrl")
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .hasNotified(false)
//                        .isFixed(false).build();
//                plantType = new PlantType();
//                otherPlantType = new OtherPlantType();
//            }
//
//            @Test
//            @DisplayName("plantTypeId 객체가 들어와 등록 성공")
//            void addPlantSuccessByPlantTypeId() {
//                // given
//                plantType.setPlantTypeId(1L);
//                plantType.setPlantName("기본 식물");
//
//                plantAddRequest.setPlantTypeId(1L);
//
//                // 예상되는 결과 객체 생성
//                Image mockImage = new Image(plantAddRequest.getProfile());
//                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
//                Plant mockPlant = new Plant(plantType, otherPlantType,
//                        plantAddRequest.getNickname(),
//                        mockImage, plantAddRequest.getBirthDate());
//                when(plantRepository.save(any(Plant.class))).thenReturn(mockPlant);
//
//                // when
//                Plant result = plantService.addPlant(plantAddRequest);
//
//                // then
//                assertThat(result).isNotNull();
//                assertThat(plantType.getPlantName()).isEqualTo("기본 식물");
//                assertThat(result.getPlantType().getPlantTypeId()).isEqualTo(plantType.getPlantTypeId());
//                assertThat(result.getImage()).isEqualTo(mockImage);
//            }
//
//            @Test
//            @DisplayName("otherPlantTypeId 객체가 들어와 등록 성공")
//            void addPlantSuccessByOtherPlantTypeId() {
//                // given
//                otherPlantType.setOtherPlantTypeId(1L);
//                otherPlantType.setPlantName("기타 식물");
//                plantAddRequest.setOtherPlantTypeId(1L);
//
//                // 예상되는 결과 객체 생성
//                Image mockImage = new Image(plantAddRequest.getProfile());
//                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
//                Plant mockPlant = new Plant(plantType, otherPlantType,
//                        plantAddRequest.getNickname(),
//                        mockImage, plantAddRequest.getBirthDate());
//                when(plantRepository.save(any(Plant.class))).thenReturn(mockPlant);
//
//                // when
//                Plant result = plantService.addPlant(plantAddRequest);
//
//                // then
//                assertThat(result).isNotNull();
//                assertThat(otherPlantType.getPlantName()).isEqualTo("기타 식물");
//                assertThat(result.getOtherPlantType().getOtherPlantTypeId()).isEqualTo(otherPlantType.getOtherPlantTypeId());
//                assertThat(result.getImage()).isEqualTo(mockImage);
//            }
//        }
//
//        @Nested
//        @DisplayName("비정상 케이스")
//        class FailureCase {
//            @BeforeEach
//            void setUp() throws ParseException {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantAddRequest = new PlantAddRequest().builder()
//                        .profile("imageUrl")
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .hasNotified(false)
//                        .isFixed(false).build();
//            }
//
//            @Test
//            @DisplayName("plantTypeId의 값이 유효하지 않아 등록 실패")
//            void addPlantInvalidPlantTypeIdThrowsException() {
//                // given
//                plantAddRequest.setPlantTypeId(-100L);
//
//                // 예상되는 결과 객체 생성
//                Image mockImage = new Image(plantAddRequest.getProfile());
//                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
//
//                // when
//                Throwable thrown = catchThrowable(() -> plantService.addPlant(plantAddRequest));
//
//                // then
//                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
//            }
//
//
//            @Test
//            @DisplayName("plantTypeId와 otherPlantTypeId 값이 둘 다 유효하여 등록 실패")
//            void addPlantWithConflictingPlantTypeIdsThrowsException() {
//                // given
//                plantAddRequest.setPlantTypeId(1L);
//                plantAddRequest.setOtherPlantTypeId(1L);
//
//                // 예상되는 결과 객체 생성
//                Image mockImage = new Image(plantAddRequest.getProfile());
//                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
//
//                // when
//                Throwable thrown = catchThrowable(() -> plantService.addPlant(plantAddRequest));
//
//                // then
//                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
//            }
//
//            @Test
//            @DisplayName("plantTypeId와 otherPlantTypeId 값이 둘 다 유효하지 않아 등록 실패")
//            void addPlantWithBothInvalidPlantTypeIdsThrowsException() {
//                // given
//                plantAddRequest.setPlantTypeId(-1L);
//                plantAddRequest.setOtherPlantTypeId(-1L);
//
//                // 예상되는 결과 객체 생성
//                Image mockImage = new Image(plantAddRequest.getProfile());
//                when(imageService.uploadImage(any(String.class))).thenReturn(mockImage);
//
//                // when
//                Throwable thrown = catchThrowable(() -> plantService.addPlant(plantAddRequest));
//
//                // then
//                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
//            }
//        }
//    }
//
//    @Nested
//    @DisplayName("식물 조회")
//    class GetPlant {
//        PlantGetResponse plantGetResponse;
//        Plant plant;
//        PlantType plantType;
//        OtherPlantType otherPlantType;
//        Image image;
//
//        @Nested
//        @DisplayName("정상 케이스")
//        class SuccessCase {
//            @BeforeEach
//            void setUp() throws ParseException {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantGetResponse = new PlantGetResponse().builder()
//                        .profile("imageUrl")
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .hasNotified(false)
//                        .fixed(1)
//                        .build();
//                image = new Image();
//                image.setImageUrl("imageUrl");
//                plant = Plant.builder()
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .image(image)
//                        .build();
//            }
//
//            @ParameterizedTest
//            @ValueSource(longs = {1L, 2L, 3L})
//            @DisplayName("다양한 plantId로 PlantType 객체 반환 성공")
//            void getPlantSuccessByPlantTypeId(Long plantId) throws ParseException {
//                // given
//                plantType = new PlantType();
//                plantType.setPlantTypeId(1L);
//
//                plantGetResponse.setPlantTypeId(1L);
//
//                plant.setPlantId(plantId);
//                plant.setPlantType(plantType);
//                plant.setHasNotified(false);
//                plant.setFixed(1);
//
//                when(plantRepository.findById(anyLong())).thenReturn(Optional.of(plant));
//
//                // when
//                PlantGetResponse result = plantService.getPlant(plantId);
//
//                // then
//                assertThat(result).isNotNull();
//                assertThat(result.getPlantTypeId()).isEqualTo(plantGetResponse.getPlantTypeId());
//                assertThat(result.getOtherPlantId()).isEqualTo(plantGetResponse.getOtherPlantId());
//                assertThat(result.getProfile()).isEqualTo(plantGetResponse.getProfile());
//                assertThat(result.getNickname()).isEqualTo(plantGetResponse.getNickname());
//                assertThat(result.getBirthDate()).isEqualTo(plantGetResponse.getBirthDate());
//                assertThat(result.isHasNotified()).isEqualTo(plantGetResponse.isHasNotified());
//                assertThat(result.getFixed()).isEqualTo(plantGetResponse.getFixed());
//            }
//
//            @ParameterizedTest
//            @ValueSource(longs = {1L, 2L, 3L})
//            @DisplayName("다양한 plantId로 OtherPlantType 객체 반환 성공")
//            void getPlantSuccessByOtherPlantTypeId(Long plantId) throws ParseException {
//                // given
//                otherPlantType = new OtherPlantType();
//                otherPlantType.setOtherPlantTypeId(1L);
//
//                plantGetResponse.setOtherPlantId(1L);
//
//                plant.setPlantId(plantId);
//                plant.setOtherPlantType(otherPlantType);
//                plant.setHasNotified(false);
//                plant.setFixed(1);
//
//                when(plantRepository.findById(anyLong())).thenReturn(Optional.of(plant));
//
//                // when
//                PlantGetResponse result = plantService.getPlant(plantId);
//
//                // then
//                assertThat(result).isNotNull();
//                assertThat(result.getPlantTypeId()).isEqualTo(plantGetResponse.getPlantTypeId());
//                assertThat(result.getOtherPlantId()).isEqualTo(plantGetResponse.getOtherPlantId());
//                assertThat(result.getProfile()).isEqualTo(plantGetResponse.getProfile());
//                assertThat(result.getNickname()).isEqualTo(plantGetResponse.getNickname());
//                assertThat(result.getBirthDate()).isEqualTo(plantGetResponse.getBirthDate());
//                assertThat(result.isHasNotified()).isEqualTo(plantGetResponse.isHasNotified());
//                assertThat(result.getFixed()).isEqualTo(plantGetResponse.getFixed());
//            }
//        }
//
//        @Nested
//        @DisplayName("비정상 케이스")
//        class FailureCase {
//            @BeforeEach
//            void setUp() throws ParseException {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantGetResponse = new PlantGetResponse().builder()
//                        .profile("imageUrl")
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .hasNotified(false)
//                        .fixed(1)
//                        .build();
//                image = new Image();
//                image.setImageUrl("imageUrl");
//                plant = Plant.builder()
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .image(image)
//                        .build();
//            }
//
//            @ParameterizedTest
//            @ValueSource(longs = {101L, 102L, 103L})
//            @DisplayName("일치하는 plantId 가 없어 반환 실패")
//            void shouldReturnNullWhenNoMatchingPlantId(Long plantId) throws ParseException {
//                // given
//                plantType = new PlantType();
//                plantType.setPlantTypeId(1L);
//
//                plantGetResponse.setPlantTypeId(1L);
//
//                plant.setPlantId(1L); // 임시 plantId 값 설정
//
//                when(plantRepository.findById(anyLong())).thenReturn(Optional.of(plant));
//
//                // when
//                Throwable thrown = catchThrowable(() -> plantService.getPlant(plantId));
//
//                // then
//                assertThat(thrown).isInstanceOf(NotValidPlantTypeIdsException.class);
//
//            }
//            // - response 값의 plantTypeId, otherPlantTypeId 가 모두 유효
//            // - 둘 다 유효 X
//
//        }
//    }
//}
