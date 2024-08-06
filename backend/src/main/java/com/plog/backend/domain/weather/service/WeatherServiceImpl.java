package com.plog.backend.domain.weather.service;

import com.plog.backend.domain.area.entity.Gugun;
import com.plog.backend.domain.area.repository.GugunRepository;
import com.plog.backend.domain.weather.dto.WeatherResponseDto;
import com.plog.backend.domain.weather.entity.Weather;
import com.plog.backend.domain.weather.exception.WeatherUpdateException;
import com.plog.backend.domain.weather.repository.WeatherRepository;
import com.plog.backend.global.util.LatXLngY;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("weatherService")
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RedisTemplate<String, Object> redisTemplate;
    private final WeatherRepository weatherRepository;
    private final GugunRepository gugunRepository;
    private final LatXLngY latXLngY;

    @Transactional
    public void updateAllWeatherForecast() {
        List<Gugun> guguns = gugunRepository.findAll();
        for (Gugun gugun : guguns) {
            WeatherResponseDto weatherResult;
            try {
                weatherResult = getWeatherForecast(gugun.getLatitude(), gugun.getLongitude());
            } catch (Exception e) {
                log.error("날씨 데이터 조회 중 에러 발생 - Gugun: {}", gugun.getGugunId(), e);
                throw new WeatherUpdateException("날씨 데이터 조회 중 에러 발생 - Gugun: " + gugun.getGugunId());
            }

            if (weatherResult.getAvgTempToday() != 0) {
                LocalDate today = LocalDate.now();
                String todayKey = "weather:" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ":" + gugun.getGugunId();

                log.info("Storing today's weather data in Redis for key: {}", todayKey);
                try {
                    redisTemplate.opsForHash().put(todayKey, "avgTempToday", weatherResult.getAvgTempToday());
                    redisTemplate.opsForHash().put(todayKey, "avgHumidityToday", weatherResult.getAvgHumidityToday());
                    redisTemplate.opsForHash().put(todayKey, "weatherToday", weatherResult.getWeatherToday());
                    redisTemplate.opsForHash().put(todayKey, "avgTempTomorrow", weatherResult.getAvgTempTomorrow());
                    redisTemplate.opsForHash().put(todayKey, "avgHumidityTomorrow", weatherResult.getAvgHumidityTomorrow());
                    redisTemplate.opsForHash().put(todayKey, "weatherTomorrow", weatherResult.getWeatherTomorrow());

                    // Set expiration for today's key to 25 hours
                    redisTemplate.expire(todayKey, 25, TimeUnit.HOURS);
                    log.info("Set expiration for key {} to 25 hours", todayKey);

                    // Verify data in Redis
                    log.info("Verifying data stored in Redis for key: {}", todayKey);
                    Map<Object, Object> todayData = redisTemplate.opsForHash().entries(todayKey);
                    todayData.forEach((key, value) -> log.info("Redis key: {}, field: {}, value: {}", todayKey, key, value));

                } catch (Exception e) {
                    log.error("Redis 처리 중 에러 발생 - Gugun: {}", gugun.getGugunId(), e);
                    throw new WeatherUpdateException("Redis 처리 중 에러 발생 - Gugun: " + gugun.getGugunId());
                }

                // Process previous day's data
                LocalDate yesterday = today.minusDays(1);
                String yesterdayKey = "weather:" + yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ":" + gugun.getGugunId();

                Double avgTempYesterday;
                Double avgHumidityYesterday;
                Integer weatherYesterdayValue;

                try {
                    avgTempYesterday = (Double) redisTemplate.opsForHash().get(yesterdayKey, "avgTempToday");
                    avgHumidityYesterday = (Double) redisTemplate.opsForHash().get(yesterdayKey, "avgHumidityToday");
                    weatherYesterdayValue = (Integer) redisTemplate.opsForHash().get(yesterdayKey, "weatherToday");

                    // 기본값 설정 및 타입 변환
                    if (avgTempYesterday == null) {
                        avgTempYesterday = 0.0;
                    }
                    if (avgHumidityYesterday == null) {
                        avgHumidityYesterday = 0.0;
                    }
                    if (weatherYesterdayValue == null) {
                        weatherYesterdayValue = 1;
                    }

                } catch (Exception e) {
                    log.error("Redis에서 데이터 가져오기 중 에러 발생 - Gugun: {}", gugun.getGugunId(), e);
                    throw new WeatherUpdateException("Redis에서 데이터 가져오기 중 에러 발생 - Gugun: " + gugun.getGugunId());
                }

                log.info("Fetched yesterday's weather data from Redis for key: {}", yesterdayKey);
                try {
                    if (!weatherRepository.existsByDateAndGugun(yesterday, gugun)) {
                        // Save to DB if not exists
                        Weather weather = new Weather();
                        weather.setDate(yesterday);
                        weather.setAvgTemp(avgTempYesterday);
                        weather.setAvgHumidity(avgHumidityYesterday);
                        weather.setWeather(weatherYesterdayValue);
                        weather.setGugun(gugun);
                        weatherRepository.save(weather);
                        log.info("Saved yesterday's weather data to DB: {}", weather);
                    } else {
                        // Update DB if exists
                        Weather weather = weatherRepository.findByDateAndGugun(yesterday, gugun).orElse(null);
                        if (weather != null) {
                            weather.setAvgTemp(avgTempYesterday);
                            weather.setAvgHumidity(avgHumidityYesterday);
                            weather.setWeather(weatherYesterdayValue);
                            weatherRepository.save(weather);
                            log.info("Updated yesterday's weather data in DB: {}", weather);
                        }
                    }

                    // Remove yesterday's data from Redis
                    redisTemplate.delete(yesterdayKey);
                    log.info("Removed yesterday's weather data from Redis for key: {}", yesterdayKey);
                } catch (Exception e) {
                    log.error("DB 저장 또는 Redis 삭제 중 에러 발생 - Gugun: {}", gugun.getGugunId(), e);
                    throw new WeatherUpdateException("DB 저장 또는 Redis 삭제 중 에러 발생 - Gugun: " + gugun.getGugunId());
                }
            }
        }
    }

    public WeatherResponseDto getWeatherForecast(double latitude, double longitude) {
        try {
            LatXLngY grid = latXLngY.convertGRID_GPS(latitude, longitude, "toXY");
            String nx = String.valueOf((int) grid.x);
            String ny = String.valueOf((int) grid.y);

            LocalDate today = LocalDate.now().minusDays(1);
            String baseDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = "2300"; // 23시 발표

            int numOfRows = 700; // 충분히 큰 값 설정
            int pageNo = 1;

            List<JSONObject> allItems = new ArrayList<>();

            while (true) {
                StringBuilder urlBuilder = new StringBuilder(apiUrl + "/getVilageFcst");
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + pageNo);
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + numOfRows);
                urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
                urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray items = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

                for (int i = 0; i < items.length(); i++) {
                    allItems.add(items.getJSONObject(i));
                }

                if (items.length() < numOfRows) {
                    break;
                }
                pageNo++;
            }

            return processWeatherData(allItems);
        } catch (Exception e) {
            log.error("날씨 데이터 조회 중 에러 발생", e);
            throw new WeatherUpdateException("날씨 데이터 조회 중 에러 발생");
        }
    }

    private WeatherResponseDto processWeatherData(List<JSONObject> items) {
        List<Double> temperatures = new ArrayList<>();
        List<Double> humidities = new ArrayList<>();
        List<Integer> skyCodes = new ArrayList<>();
        List<Integer> ptyCodes = new ArrayList<>();

        for (JSONObject item : items) {
            String category = item.getString("category");
            String value = item.getString("fcstValue");

            switch (category) {
                case "TMP":
                    temperatures.add(Double.parseDouble(value));
                    break;
                case "REH":
                    humidities.add(Double.parseDouble(value));
                    break;
                case "SKY":
                    skyCodes.add(Integer.parseInt(value));
                    break;
                case "PTY":
                    ptyCodes.add(Integer.parseInt(value));
                    break;
            }
        }

        if (temperatures.size() >= 48 && humidities.size() >= 48 && skyCodes.size() >= 48 && ptyCodes.size() >= 48) {
            double avgTempToday = calculateAverage(temperatures.subList(0, 24));
            double avgHumidityToday = calculateAverage(humidities.subList(0, 24));
            int weatherTodayValue = calculateDailyWeatherValue(skyCodes.subList(0, 24), ptyCodes.subList(0, 24));

            double avgTempTomorrow = calculateAverage(temperatures.subList(24, 48));
            double avgHumidityTomorrow = calculateAverage(humidities.subList(24, 48));
            int weatherTomorrowValue = calculateDailyWeatherValue(skyCodes.subList(24, 48), ptyCodes.subList(24, 48));

            return new WeatherResponseDto(avgTempToday, avgHumidityToday, weatherTodayValue,
                    avgTempTomorrow, avgHumidityTomorrow, weatherTomorrowValue);
        } else {
            throw new WeatherUpdateException("Insufficient weather data received");
        }
    }

    private double calculateAverage(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    private int calculateDailyWeatherValue(List<Integer> skyCodes, List<Integer> ptyCodes) {
        Map<Integer, Integer> weatherCount = new HashMap<>();
        weatherCount.put(com.plog.backend.domain.diary.entity.Weather.SUNNY.getValue(), 0);
        weatherCount.put(com.plog.backend.domain.diary.entity.Weather.CLOUDY.getValue(), 0);
        weatherCount.put(com.plog.backend.domain.diary.entity.Weather.VERY_CLOUDY.getValue(), 0);
        weatherCount.put(com.plog.backend.domain.diary.entity.Weather.RAINY.getValue(), 0);
        weatherCount.put(com.plog.backend.domain.diary.entity.Weather.SNOWY.getValue(), 0);

        for (int i = 0; i < skyCodes.size(); i++) {
            int sky = skyCodes.get(i);
            int pty = ptyCodes.get(i);
            int weatherValue = getWeatherValue(sky, pty);
            weatherCount.put(weatherValue, weatherCount.get(weatherValue) + 1);
        }

        if (weatherCount.get(com.plog.backend.domain.diary.entity.Weather.RAINY.getValue()) >= 6) {
            return com.plog.backend.domain.diary.entity.Weather.RAINY.getValue();
        } else if (weatherCount.get(com.plog.backend.domain.diary.entity.Weather.SNOWY.getValue()) >= 6) {
            return com.plog.backend.domain.diary.entity.Weather.SNOWY.getValue();
        } else if (weatherCount.get(com.plog.backend.domain.diary.entity.Weather.VERY_CLOUDY.getValue()) >= 6) {
            return com.plog.backend.domain.diary.entity.Weather.VERY_CLOUDY.getValue();
        } else if (weatherCount.get(com.plog.backend.domain.diary.entity.Weather.CLOUDY.getValue()) >= 6) {
            return com.plog.backend.domain.diary.entity.Weather.CLOUDY.getValue();
        } else {
            return com.plog.backend.domain.diary.entity.Weather.SUNNY.getValue();
        }
    }

    private int getWeatherValue(int sky, int pty) {
        if (pty == 1 || pty == 2 || pty == 4) {
            return com.plog.backend.domain.diary.entity.Weather.RAINY.getValue();
        } else if (pty == 3) {
            return com.plog.backend.domain.diary.entity.Weather.SNOWY.getValue();
        } else if (sky == 1 && pty == 0) {
            return com.plog.backend.domain.diary.entity.Weather.SUNNY.getValue();
        } else if (sky == 3 && pty == 0) {
            return com.plog.backend.domain.diary.entity.Weather.CLOUDY.getValue();
        } else if (sky == 4 && pty == 0) {
            return com.plog.backend.domain.diary.entity.Weather.VERY_CLOUDY.getValue();
        }
        return com.plog.backend.domain.diary.entity.Weather.SUNNY.getValue();
    }

    @Override
    public void updateWeatherData() {
        updateAllWeatherForecast();
    }
}
