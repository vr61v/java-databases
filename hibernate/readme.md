# Hibernate ORM Ticket System

Проект демонстрирует работу с Hibernate ORM и PostgreSQL для управления данными авиабилетов с использованием объектно-реляционного отображения.

## Содержание
- [Технологии](#технологии)
- [Установка](#установка)
- [Структура проекта](#структура-проекта)
- [Использование](#использование)
- [API](#api)

## Технологии
- Java 17+
- PostgreSQL
- Hibernate ORM
- Docker
- JUnit
- AssertJ
- Maven
- Lombok

## Установка

1. Клонируйте репозиторий:
```bash
git clone https://github.com/<your-repo>/hibernate-ticket-system.git
cd hibernate-ticket-system
```

2. Загрузите демонстрационную базу данных:
    * Скачайте [SQL скрипт](https://postgrespro.ru/education/demodb)
    * Поместите скаченный скрипт в папку postgres/

3. Запустите контейнеры:
```bash
   docker-compose up -d
```

4. Запустите sql скрипт для актуализации БД:
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

5. Соберите проект:
```bash
   mvn clean install
```

## Структура проекта

```
   src/
   ├── main/
   │   ├── java/com/vr61v/
   │   │   ├── entities/
   │   │   │   ├── embedded/           # Встраиваемые объекты
   │   │   │   └── types/              # Перечисления
   │   │   ├── exceptions/             # Пользовательские исключения
   │   │   ├── repositories/           # Репозитории для работы с БД
   │   │   │   ├── impl/               # Реализации репозиториев для сущностей
   │   │   │   └── Repository.java     # Базовый класс репозитория для общих CRUD операций
   │   │   └── utils/                  # Вспомогательные утилиты
   │   └── resources/                  # Конфигурационные файлы
   ├── test/
   │   └── java/
   │       └── RepositoryTests.java    # Тестирование CRUD операций

   postgres/                           # Файлы для инициализации БД
   docker-compose.yml                  # Конфигурация Docker
```

## Использование

```java
// Создаем менеджер сессий (одна инстанция на приложение)
RepositorySessionManager sessionManager = new RepositorySessionManager();

// Инициализируем репозиторий
BookingRepository bookingRepository = new BookingRepository(sessionManager);

// Создаем сущность
Booking booking = Booking.builder()
        .bookRef("123456")
        .bookDate(OffsetDateTime.now())
        .totalAmount(50_000.00F)
        .build();

// Сохраняем сущность
boolean saved = bookingRepository.save(booking);

// Получаем сущность
Booking found = bookingRepository.findById(booking.getBookRef());

// Обновляем сущность
Booking updatedTotalAmount = Booking.builder()
        .bookRef("123456")
        .bookDate(OffsetDateTime.now())
        .totalAmount(10_000.00F)
        .build();
Booking updated = bookingRepository.update(updatedTotalAmount);

// Удаляем сущность
boolean isDeleted = bookingRepository.delete(booking);
```

## API

## Repository
* boolean save(T entity) - сохранение сущности, возвращает true в случае успеха, иначе false
* Optional<T> findById(ID id) - поиск сущности по ее ID, в случае отсутствия сущности в БД возвращает Optional.empty()
* T update(T entity) - обновление всей сущности
* boolean delete(T entity) - удаление сущности, возвращает true в случае успеха, иначе false
