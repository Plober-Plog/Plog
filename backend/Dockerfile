FROM amazoncorretto:17-alpine AS build

WORKDIR /app

# 의존성 설치 및 빌드
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew build --no-daemon

# 빌드 결과물을 실행 이미지로 복사
FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/plog.jar /app/plog.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/plog.jar"]
