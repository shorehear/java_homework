Зарегистрируйтесь на https://api.openweathermap.org/data/2.5/weather, получите бесплатый API-ключ.

Создайте по пути src/resources файл config.properties, заполните две переменные: ключ и ссылка на выше.

Соберите пакет:
mvn clean package

Запустите собранный пакет:
java -jar target/weather-client-jar-with-dependencies.jar