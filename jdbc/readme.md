# JDBC Ticket System

Проект демонстрирует работу с JDBC и PostgreSQL для управления данными авиабилетов.

## Содержание
- [Технологии](#технологии)
- [Установка](#установка)
- [Структура проекта](#структура-проекта)
- [Использование](#использование)
- [API](#api)

## Технологии
- Java 17+
- PostgreSQL
- JDBC
- Docker
- Jackson (для работы с JSON)
- Maven

## Установка

1. Клонируйте репозиторий:
```bash
   git clone https://github.com/vr61v/java-database.git
   cd java-database/jdbc
```

2. Загрузите демонстрационную базу данных:
    * Скачайте [SQL скрипт](https://postgrespro.ru/education/demodb)
    * Поместите скаченный скрипт в папку postgres/

3. Запустите контейнеры:
```bash
   docker-compose up -d
```

4. Соберите проект:
```bash
    mvn clean install
```

## Структура проекта
```
    src/
    ├── main/
    │   ├── java/com/vr61v/
    │   │   ├── entities/       # Сущности предметной области
    │   │   ├── exceptions/     # Пользовательские исключения
    │   │   ├── mappers/        # Мапперы для преобразования данных
    │   │   ├── repositories/   # Репозитории для работы с БД
    │   │   └── utils/          # Вспомогательные утилиты
    │   └── resources/          # Конфигурационные файлы
    postgres/                   # Файлы для инициализации БД
    docker-compose.yml          # Конфигурация Docker
```

## Использование
```
    // Создание репозитория
    TicketsRepository repository = new TicketsRepository();
    
    // Получение билета по ID
    Ticket ticket = repository.findById("0005432000991");
    
    // Получение страницы билетов
    List<Ticket> tickets = repository.findPage(0, 10);
    
    // Добавление нового билета
    Ticket newTicket = new Ticket(...);
    boolean added = repository.add(newTicket);
```

## API

### TicketsRepository implemented Repository
* add(T entity) - добавление одной сущности
* addAll(List<T> entities) - массовое добавление сущностей
* findById(String id) - поиск сущности по ID
* findAll() - получение всех сущностей
* findAllById(List<String> ids) - поиск нескольких сущностей по ID
* findPage(int page, int size) - постраничное получение сущностей
* update(T entity) - обновление сущности
* updateAll(List<T> entities) - массовое обновление сущностей
* delete(String id) - удаление сущности по ID
* deleteAll(List<String> ids) - массовое удаление сущностей по ID

### TicketMapper implemented Mapper
* mapToEntity(ResultSet rs) - преобразует ответ из JDBC в сущность
* mapToColumns(T entity) - преобразует все поля сущности в список строк
