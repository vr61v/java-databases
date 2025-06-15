# JDBC Ticket System

## Содержание
- [Технологии](#технологии)
- [Установка](#установка)
- [Структура проекта](#структура-проекта)
- [Использование](#использование)
- [API](#api)

## Технологии
- Java 17+
- PostgreSQL
- Spring boot (data-jpa, web, validation)
- Docker
- Maven
- Lombok
- Mapstruct

## Установка

1. Клонируйте репозиторий:
```bash
    git clone https://github.com/vr61v/java-databases.git
    cd spring-data
```

2. Загрузите демонстрационную базу данных:
    * Скачайте [SQL скрипт](https://postgrespro.ru/education/demodb)
    * Поместите скаченный скрипт в папку postgres/

3. Соберите проект
```bash
   mvn clean install
```

4. Соберите образ из dockerfile
```bash
    docker build -t vr61v/spring-data .
```

4. Запустите контейнеры:
```bash
   docker-compose up -d
```

5. Запустите sql скрипт для актуализации БД:
```sql
    begin ;
    
    -- Исправление строк стутусов перелетов (Значения должны разделяться через 
    -- нижнее подчеркивание, а все буквы должны быть заглавными)
    
    alter table flights drop constraint flights_status_check;
    update flights set status = upper(replace(status, ' ', '_'));
    
    -- Аналогичное исправление для тарифов, значения переводятся в тот же формат
    -- что и статусы перелетов
    
    alter table seats drop constraint seats_fare_conditions_check;
    alter table ticket_flights drop constraint ticket_flights_fare_conditions_check;
    update seats set fare_conditions = upper(fare_conditions);
    update ticket_flights set fare_conditions = upper(fare_conditions);
    
    -- При запуске контейнера в бэкапе базы данных не сохранилось текущее значение
    -- для последовательности id перелета, поэтому необходимо задать его в ручную
    
    select setval('flights_flight_id_seq',
                  (select max(flights. flight_id) from flights)
           );
    
    -- Удаление неиспользуемого столбца координат для аэропорта, в итоге будут 
    -- удалены так же и представления, созданные с этим столбцом 
    
    alter table airports_data drop coordinates cascade ;
    
    end ;
```

## Структура проекта
```
   src/
   ├── main/
   │   ├── java/com/vr61v/
   │   │   ├── controllers/v1/
   │   │   │   ├── crud/                # Реализация crud контроллеров
   │   │   │   ├── custom/              # Реализация контроллеров с уникальной логикой
   │   │   │   └── CrudController       # Абстрактный класс crud контроллера
   │   │   ├── dtos/                    # Dto для общения с контроллером
   │   │   ├── embedded/                # Встраиваемые сущности
   │   │   ├── entities/                # Сущности jpa репозиториев
   │   │   ├── handlers/                # Перехватчики ошибок из контроллеров
   │   │   ├── mappers/                 # Интерфейсы для реализации мапперов сущностей
   │   │   ├── repositories/            # Интерфейсы для реализации jpa репозиториев
   │   │   ├── services/
   │   │   │   ├── crud/                # Реализация crud сервисов
   │   │   │   ├── custom/              # Реализация сервисов с уникальной логикой
   │   │   │   └── CrudService          # Абстрактный класс crud сервиса
   │   │   └── types/                   # Перечисления для работы с БД
   │   └── resources/                   # Конфигурационные файлы

   postgres/                            # Файлы для инициализации БД
   dockerfile                           # Файл сборки образа docker
   docker-compose.yml                   # Конфигурация запуска сервисов Docker
```

## Использование
> Для использования приложения необходимо либо собрать проект и запустить локально,
    либо собрать образ из Dockerfile и запустить в compose. Приложение требует для работы
    свободного порта 5432(postgres) и 8080(application). Для быстрого тестирования ендпоинтов
    можно импортировать spring-data.postman_collection.json, который в себе хранит все возможные
    базовые crud запросы (для большего понимания к коллекциям написано описание в самом postman)

## API

> [!NOTE]
> В случае отсутствия какого либо id в удаляемой коллекции будет возвращено сообщение,
> с указанием отсутствующих id.

> [!WARNING]
> Использование GET запроса для получения всех сущностей может привести к огромным задержкам
> по причине большой базы данных, перед использованием следует изучить размеры каждой из таблиц.

### Основные конечные точки

#### Самолеты (`/api/v1/aircrafts`)
- **GET /api/v1/aircrafts** - Получить список всех самолетов
- **GET /api/v1/aircrafts/{code}** - Получить самолет по коду
- **POST /api/v1/aircrafts/{code}** - Создать новый самолет
- **POST /api/v1/aircrafts** - Создать несколько самолетов (массив)
- **PUT /api/v1/aircrafts/{code}** - Обновить данные самолета
- **PUT /api/v1/aircrafts** - Обновить несколько самолетов (массив)
- **DELETE /api/v1/aircrafts/{code}** - Удалить самолет
- **DELETE /api/v1/aircrafts** - Удалить несколько самолетов (массив кодов)

#### Места в самолетах (`/api/v1/aircrafts/{code}/seats`)
- **GET /api/v1/aircrafts/{code}/seats** - Получить все места в самолете
- **GET /api/v1/aircrafts/{code}/seats/{seatNo}** - Получить конкретное место
- **POST /api/v1/aircrafts/{code}/seats/{seatNo}** - Создать новое место
- **POST /api/v1/aircrafts/{code}/seats** - Создать несколько мест (массив)
- **PUT /api/v1/aircrafts/{code}/seats/{seatNo}** - Обновить данные места
- **PUT /api/v1/aircrafts/{code}/seats** - Обновить несколько мест (массив)
- **DELETE /api/v1/aircrafts/{code}/seats/{seatNo}** - Удалить место
- **DELETE /api/v1/aircrafts/{code}/seats** - Удалить несколько мест (массив)

#### Аэропорты (`/api/v1/airports`)
- **GET /api/v1/airports** - Получить список всех аэропортов
- **GET /api/v1/airports/{code}** - Получить аэропорт по коду
- **POST /api/v1/airports/{code}** - Создать новый аэропорт
- **POST /api/v1/airports** - Создать несколько аэропортов (массив)
- **PUT /api/v1/airports/{code}** - Обновить данные аэропорта
- **PUT /api/v1/airports** - Обновить несколько аэропортов (массив)
- **DELETE /api/v1/airports/{code}** - Удалить аэропорт
- **DELETE /api/v1/airports** - Удалить несколько аэропортов (массив кодов)

#### Бронирования (`/api/v1/bookings`)
- **GET /api/v1/bookings** - Получить список всех бронирований
- **GET /api/v1/bookings/{ref}** - Получить бронирование по номеру
- **POST /api/v1/bookings/{ref}** - Создать новое бронирование
- **POST /api/v1/bookings** - Создать несколько бронирований (массив)
- **PUT /api/v1/bookings/{ref}** - Обновить данные бронирования
- **PUT /api/v1/bookings** - Обновить несколько бронирований (массив)
- **DELETE /api/v1/bookings/{ref}** - Удалить бронирование
- **DELETE /api/v1/bookings** - Удалить несколько бронирований (массив номеров)

#### Билеты (`/api/v1/tickets`)
- **GET /api/v1/tickets** - Получить список всех билетов
- **GET /api/v1/tickets/{number}** - Получить билет по номеру
- **POST /api/v1/tickets/{number}** - Создать новый билет
- **POST /api/v1/tickets** - Создать несколько билетов (массив)
- **PUT /api/v1/tickets/{number}** - Обновить данные билета
- **PUT /api/v1/tickets** - Обновить несколько билетов (массив)
- **DELETE /api/v1/tickets/{number}** - Удалить билет
- **DELETE /api/v1/tickets** - Удалить несколько билетов (массив номеров)

#### Рейсы (`/api/v1/flights`)
- **GET /api/v1/flights** - Получить список всех рейсов
- **GET /api/v1/flights/{id}** - Получить рейс по ID
- **POST /api/v1/flights/{id}** - Создать новый рейс
- **POST /api/v1/flights** - Создать несколько рейсов (массив)
- **PUT /api/v1/flights/{id}** - Обновить данные рейса
- **PUT /api/v1/flights** - Обновить несколько рейсов (массив)
- **DELETE /api/v1/flights/{id}** - Удалить рейс
- **DELETE /api/v1/flights** - Удалить несколько рейсов (массив ID)

#### Билеты на рейсы (`/api/v1/ticketflights`)
- **GET /api/v1/ticketflights** - Получить список всех билетов на рейсы
- **GET /api/v1/ticketflights/{ticketNo}/{flightId}** - Получить билет на рейс
- **POST /api/v1/ticketflights/{ticketNo}/{flightId}** - Создать билет на рейс
- **POST /api/v1/ticketflights** - Создать несколько билетов на рейсы (массив)
- **PUT /api/v1/ticketflights/{ticketNo}/{flightId}** - Обновить билет на рейс
- **PUT /api/v1/ticketflights** - Обновить несколько билетов на рейсы (массив)
- **DELETE /api/v1/ticketflights/{ticketNo}/{flightId}** - Удалить билет на рейс
- **DELETE /api/v1/ticketflights** - Удалить несколько билетов на рейсы (массив)

#### Посадочные талоны (`/api/v1/boardingpass`)
- **GET /api/v1/boardingpass** - Получить список всех посадочных талонов
- **GET /api/v1/boardingpass/{ticketNo}/{flightId}** - Получить посадочный талон
- **POST /api/v1/boardingpass/{ticketNo}/{flightId}** - Создать посадочный талон
- **POST /api/v1/boardingpass** - Создать несколько посадочных талонов (массив)
- **PUT /api/v1/boardingpass/{ticketNo}/{flightId}** - Обновить посадочный талон
- **PUT /api/v1/boardingpass** - Обновить несколько посадочных талонов (массив)
- **DELETE /api/v1/boardingpass/{ticketNo}/{flightId}** - Удалить посадочный талон
- **DELETE /api/v1/boardingpass** - Удалить несколько посадочных талонов (массив)

### Обработка ошибок
API возвращает следующие коды ошибок:
- `400 Bad Request` - Некорректный запрос (валидация, нечитаемое сообщение)
- `404 Not Found` - Запрашиваемый ресурс не найден
- `500 Internal Server Error` - Ошибка сервера при обработке запроса

Примеры ошибок:
- `MethodArgumentNotValidException` - Ошибка валидации входных данных
- `HandlerMethodValidationException` - Ошибка валидации параметров метода
- `DataAccessException` - Ошибка доступа к данным
- `HttpMessageNotReadableException` - Невозможно прочитать тело запроса
- `NotFoundException` - Запрашиваемый ресурс не найден