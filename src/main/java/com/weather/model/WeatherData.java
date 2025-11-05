package com.weather.model;

public class WeatherData {
    private String cityName;
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;

    public WeatherData(String cityName, double temperature, String description, 
                       int humidity, double windSpeed) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String toString() {
        return String.format(
            "%s, текущая погода:\n" +
            "Температура: %+.1f°C\n" +
            "Описание: %s\n" +
            "Влажность: %d%%\n" +
            "Скорость ветра: %.1f м/с",
            cityName, temperature, description, humidity, windSpeed
        );
    }
}
