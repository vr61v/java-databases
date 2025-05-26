# Hibernate Ticket System

## Содержание
- [Технологии](#технологии)
- [Установка](#установка)
- [Структура проекта](#структура-проекта)
- [Использование](#использование)
- [API](#api)

## Технологии

## Установка

```
begin ;

-- Fix status names into flights table
alter table flights drop constraint flights_status_check;
update flights set status = upper(replace(status, ' ', '_'));

-- Fix fare conditions names into seats and ticket_flights tables
alter table seats drop constraint seats_fare_conditions_check;
alter table ticket_flights drop constraint ticket_flights_fare_conditions_check;
update seats set fare_conditions = upper(fare_conditions);
update ticket_flights set fare_conditions = upper(fare_conditions);

-- Fix start value into flights_flight_id_seq sequence
select setval('flights_flight_id_seq',
              (select max(flights. flight_id) from flights)
       );

-- Drop unused column form airports_data
alter table airports_data drop coordinates cascade ;

end ;
```

## Структура проекта

## Использование

## API
