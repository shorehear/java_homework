package com.weather;

import com.weather.model.WeatherData;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WeatherClient {
    private final WeatherService weatherService;
    private final Scanner scanner;
    private boolean running;

    public WeatherClient() {
        this.weatherService = new WeatherService();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        System.out.println("=== Погодный клиент ===");
        System.out.println("Введите 'help' для справки\n");

        while (running) {
            try {
                System.out.print("> ");
                
                if (!scanner.hasNextLine()) {
                    System.out.println("\nЗавершение работы...");
                    break;
                }
                
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                processCommand(input);
                
            } catch (NoSuchElementException e) {
                System.out.println("Завершение работы (конец ввода)...");
                break;
            } catch (Exception e) {
                System.err.println("Неожиданная ошибка: " + e.getMessage());
            }
        }

        try {
            scanner.close();
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии консоли: " + e.getMessage());
        }
    }

    private void processCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toLowerCase();

        switch (command) {
            case "weather":
                if (parts.length < 2) {
                    System.out.println("Ошибка: укажите название города");
                    System.out.println("Пример: weather Москва");
                } else {
                    getWeather(parts[1]);
                }
                break;

            case "help":
                showHelp();
                break;

            case "quit":
            case "exit":
                System.out.println("Завершение работы...");
                running = false;
                break;

            default:
                System.out.println("Неизвестная команда. Введите 'help' для справки.");
        }
    }

    private void getWeather(String cityName) {
        try {
            WeatherData weather = weatherService.getWeatherByCity(cityName);
            System.out.println("\n" + weather + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Ошибка конфигурации: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка сети: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private void showHelp() {
        System.out.println("\n=== Справка ===");
        System.out.println("Доступные команды:");
        System.out.println("  weather <город>  - Получить погоду для указанного города");
        System.out.println("                     Пример: weather London");
        System.out.println("                     Пример: weather Moscow");
        System.out.println("  help             - Показать эту справку");
        System.out.println("  quit             - Завершить работу приложения\n");
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            System.setProperty("api.key", args[0]);
        }

        WeatherClient client = new WeatherClient();
        client.start();
    }
}
