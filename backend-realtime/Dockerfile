# 빌드 스테이지
FROM amazoncorretto:17-alpine AS build_stage

WORKDIR /app

# 의존성 설치 및 빌드
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

# Gradle 빌드 및 로그 출력
RUN ./gradlew clean build --no-daemon && \
    echo "Gradle build completed successfully" && \
    ls -la /app/build/libs

# 실행 이미지
FROM amazoncorretto:17-alpine

WORKDIR /app

# 빌드 스테이지에서 JAR 파일 복사
COPY --from=build_stage /app/build/libs/realtime-0.0.1-SNAPSHOT.jar /app/plog-realtime.jar

EXPOSE 8082

CMD ["java", "-jar", "/app/plog-realtime.jar"]
