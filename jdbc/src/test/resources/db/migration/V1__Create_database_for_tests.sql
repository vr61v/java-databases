create table if not exists bookings.bookings
(
    book_ref char(6) not null primary key,
    book_date timestamp with time zone not null,
    total_amount numeric(10, 2) not null
);

alter table bookings.bookings owner to postgres;

create table if not exists bookings.tickets
(
    ticket_no char(13) not null primary key,
    book_ref char(6) not null references bookings.bookings,
    passenger_id varchar(20) not null,
    passenger_name text not null,
    contact_data jsonb
);

alter table bookings.tickets owner to postgres;

insert into bookings.bookings values ('000000', now(),50000.00);
