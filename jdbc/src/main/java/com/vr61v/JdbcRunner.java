package com.vr61v;

import com.vr61v.entities.Ticket;
import com.vr61v.repositories.Repository;
import com.vr61v.repositories.TicketsRepository;

public class JdbcRunner {
    public static void main(String[] args) {
        Repository<Ticket> repository = new TicketsRepository();
        repository.findAll().forEach(System.out::println);
    }
}
