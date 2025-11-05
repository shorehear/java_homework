package com.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weather.model.WeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weather.model.WeatherData;

public class WeatherService {
    private final HttpClient httpClient;
    private final ConfigManager config;

    public WeatherService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.config = ConfigManager.getInstance();
    }

    public WeatherData getWeatherByCity(String cityName) throws Exception {
        String apiKey = config.getApiKey();
        
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException(
                "Не указан API-ключ. Поместите его в config.properties");
        }

        String encodedCity = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
        String url = String.format("%s?q=%s&appid=%s&units=metric&lang=ru",
                config.getApiUrl(), encodedCity, apiKey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new IllegalArgumentException("Город не найден");
            } else if (response.statusCode() == 401) {
                throw new IllegalStateException(
                    "Проверьте корректность API-ключа");
            } else if (response.statusCode() != 200) {
                throw new IOException("Ошибка подключения к сервису погоды");
            }

            return parseWeatherData(response.body());
            
        } catch (IOException e) {
            throw new IOException(
                "Проверьте наличие интернета и корректность API-ключа", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Запрос был прерван", e);
        }
    }

    private WeatherData parseWeatherData(String jsonResponse) {
        JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();
        
        String cityName = root.get("name").getAsString();
        
        JsonObject main = root.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        int humidity = main.get("humidity").getAsInt();
        
        JsonArray weatherArray = root.getAsJsonArray("weather");
        String description = weatherArray.get(0).getAsJsonObject()
                .get("description").getAsString();
        
        JsonObject wind = root.getAsJsonObject("wind");
        double windSpeed = wind.get("speed").getAsDouble();
        
        return new WeatherData(cityName, temperature, description, 
                humidity, windSpeed);
    }
}
