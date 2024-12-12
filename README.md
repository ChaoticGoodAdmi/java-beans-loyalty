# `beans-loyalty`

## Описание
Микросервис **`beans-loyalty`** отвечает за управление бонусным счетом пользователей в системе онлайн-заказов. Он предоставляет REST API для работы с бонусами, реализует начисление и списание баллов, а также публикует/обрабатывает асинхронные события через Kafka. Архитектура сервиса построена на паттернах **Event Sourcing**, **CQRS** и **Event-Driven Architecture**.

---

## Ключевые функции
- Начисление бонусных баллов при создании заказа.
- Списание баллов для уменьшения стоимости заказа.
- Предоставление информации о текущем балансе бонусов.
- Асинхронное взаимодействие с другими сервисами через Kafka.

---

## Требования к инфраструктуре
1. **Базы данных:**
    - **MongoDB** – для хранения событий (Event Store).
    - **Redis** – для кэширования агрегированных снимков состояния.
2. **Сообщения:**
    - **Kafka** – для обмена асинхронными событиями.
3. **Системные зависимости:**
    - Java 17+
    - Docker (для работы с контейнерами Redis, MongoDB и Kafka).

---

## Инструкция по сборке и установке

### 1. Клонирование репозитория
```bash
git clone https://github.com/your-repo/beans-loyalty.git
cd beans-loyalty
```
2. Настройка переменных окружения

Создайте .env файл или настройте переменные окружения:

    REDIS_HOST=localhost
    REDIS_PORT=6379
    MONGO_URI=mongodb://localhost:27017/beans-loyalty
    KAFKA_BROKER=localhost:9092
    BONUS_PERCENTAGE=0.1

3. Сборка проекта

Убедитесь, что у вас установлен Gradle:

    ./gradlew clean build

4. Запуск приложения
Локальный запуск:

    java -jar build/libs/beans-loyalty-0.0.1-SNAPSHOT.jar

Запуск с помощью Docker Compose:

Создайте файл docker-compose.yml с описанием инфраструктуры (Redis, MongoDB, Kafka). Затем выполните:

    docker-compose up

API
1. Получение текущего баланса

    GET /loyalty/balance

2. История бонусных баллов

    POST /loyalty/history

Асинхронные взаимодействия

    Получает события:
        OrderCreated (Kafka топик OrderCreated).
    Публикует события:
        LoyaltyUpdated (Kafka топик LoyaltyUpdated).