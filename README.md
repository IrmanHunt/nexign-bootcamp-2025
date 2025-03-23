# Отборочное задание по направлению «Инженерные практики»: CDR & UDR Microservice
---
### Описание задания

Микросервис, который реализует упрощённую модель работы систем обработки звонков. Проект генерирует CDR-записи, сохраняет их в базе данных H2, агрегирует данные в UDR-отчёты и предоставляет REST API для их получения

**Задачи:**
- Задача 1: написать часть, эмулирующую работу коммутатора, т.е. генерирующую CDR записи.
- Задача 2: написать часть, предоставляющую Rest-API для работы с UDR.
- Задача 3 (дополнительная): написать часть, формирующую и сохраняющую CDR-отчет.

---
### Решение поставленных задач

Был создан полностью рабочий микросервис, который решает каждую поставленную задачу.

**Задача 1:**
- Класс `DataInitializer` реализует интерфейс `CommandLineRunner` и каждый раз при старте приложения заносит в локальную базу данных H2 Database список из 15-ти msisdn абонентов.
- С помощью метода _generateCdrRecords_ интерфейса `CdrGenerator` происходит либо простая, либо сложная генерация CDR записей. Реализация зависит от выбранных в application.properties настроек.

**Задача 2:**
- Контроллер `UdrController` содержит 2 метода: _getUdrForSubscriber_ - возвращает udr абонента за все время или за выбранный месяц и _getAllUdrForMonth_ - возвращает список udr всех абонентов за выбранный месяц.
- Вся рабочая логика происходит в классе `udrService`, который обрабатывает данные полученные из интерфейсов: `CdrRecordRepository` и `SubscriberRepository`. 

**Задача 3:**
- Контроллер `ReportController` содежит метод: _generateCdrReport_ - генерирует отчет по абоненту за выбранный период и сохраняет его по адресу /reports в формате csv.
- За логику работы отвечает класс сервисного слоя `ReportService`.

---
### Технологии

#### Java 21, Spring Boot, H2-Database, Maven, JUnit

**Зависимости:**
- spring-boot-starter-data-jpa – работа с JPA и базами данных.
- spring-boot-starter-web – создание REST API.
- h2 – встроенная H2 база данных.
- spring-boot-starter-test – для тестирования Spring-приложения.
- jackson-datatype-jsr310 – для работы некоторых тестов.
- lombok – для использования аннотации @Slf4j.

---
### Конфигурация (application.properties)

```
# Год генерации данных для UDR
app.generation.year=2024

# Интервал между звонками (дни)
app.call.interval.days=30

# Путь для хранения отчетов
app.report.directory=./reports

# Метод генерации CDR (easy или complex)
app.cdr-generation-method=easy
```

---
### API Эндпоинты

- `GET /api/udr/{msisdn}` – получение UDR-отчёта для абонента за все время.
- `GET /api/udr/{msisdn}?month={month}` – получение UDR-отчёта для абонента за выбранных месяц (например april).
- `GET /api/udr?month={month}` – получение UDR-отчёта для всех абонентов за выбранный месяц (например september).
- `POST /api/reports/cdr` – генерация отчёта по CDR с возвратом UUID.

**Пример тела post-запроса:**
```json
{
  "msisdn": "79876543221",
  "start": "2024-01-01",
  "end": "2024-01-31"
}
```

---
### Пример использования

**Первый пример.**

**Получение UDR отчета для абонента:**
```
http://localhost:8080/api/udr/79876543221?month=april
```

**Ответ:**
```
{
    "msisdn": "79876543221",
    "incomingCall": { "totalTime": "02:12:13" },
    "outcomingCall": { "totalTime": "00:02:50" }
}
```

**Второй пример.**

**Генерация CDR отчета:**
```
POST http://localhost:8080/api/reports/cdr
```

Тело запроса (JSON):
```
{
    "msisdn": "79876543221",
    "start": "2024-01-01",
    "end": "2024-01-31"
}
```

Ответ:
```
Отчет успешно сохранен. UUID запроса: 8c9a5f96-2da2-4462-b70f-caecf77814b7
```

---
### Запуск приложения

**Первый способ:**

1. Склонируйте репозиторий.
```
git clone https://github.com/IrmanHunt/nexign-bootcamp-2025.git
cd nexign-bootcamp-2025
```

2. Запустите jar-файл.
```
cd /target
java -jar nexign-bootcamp-2025-0.0.1-SNAPSHOT.jar
```

**Второй способ (Docker):**

1. Склонируйте репозиторий.
```
git clone https://github.com/IrmanHunt/nexign-bootcamp-2025.git
cd nexign-bootcamp-2025
```

2. Создайте image:
```
docker build -t nexign-bootcamp-2025 .
```

2. Запустите контейнер:
```
docker run -p 8080:8080 --name my-nexign-bootcamp-2025 nexign-bootcamp-2025
```