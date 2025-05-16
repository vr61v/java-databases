package com.vr61v;

import com.vr61v.entities.Ticket;
import com.vr61v.repositories.Repository;
import com.vr61v.repositories.TicketsRepository;

public class JdbcRunner {
    public static void main(String[] args) {
        Repository<Ticket> repository = new TicketsRepository();
//        repository.findAll().forEach(System.out::println);
//        Ticket ticket = new Ticket(
//                "1111111111111",
//                "D0BE1F",
//                "5272 756072",
//                "ELIZAVETA SMIRNOVA",
//                new ContactData(
//                        "+70412695997",
//                        "smirnova_26051970@postgrespro.ru")
//        );
//        System.out.println(repository.add(ticket));
        System.out.println(repository.findById("1111111111111"));
    }
}
