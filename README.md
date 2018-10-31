# SimpleWeather
Приложение для получения прогнозов погоды. Данные берутся из базы данных OpenWeatherMap по Rest API. Содержит примеры использования Retrofit2, Room, Dagger2, RxJava2, Unit Testing.
[![Демо](https://img.youtube.com/vi/Z3GuWwQyg1w/0.jpg)](https://youtu.be/Z3GuWwQyg1w)

# Особенности
- Поддержка локализации ru/en
- Обработка поворота экрана
- Кэширование данных
- Определение местоположения
- Геокодинг

# Ps 
Для использования сервиса OpenWeatherMap необходим API key. Для этого нужно зарегистрироваться на OpenWeatherMap, после чего на странице аккаунта будет доступен API key (APPID).
В Constants указать данный ключ в OWM_API_KEY.
