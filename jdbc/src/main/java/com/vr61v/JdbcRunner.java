package com.vr61v;

import com.vr61v.entities.ContactData;
import com.vr61v.entities.Ticket;
import com.vr61v.repositories.Repository;
import com.vr61v.repositories.TicketsRepository;

public class JdbcRunner {
    public static void main(String[] args) {
        Repository<Ticket> repository = new TicketsRepository();
        Ticket add = new Ticket(
                "1111111111111",
                "5D4169",
                "5112 696349",
                "VIKTORIYA SMIRNOVA",
                new ContactData(
                        "+70013357481",
                        "a.moiseev_1977@postgrespro.ru"
                )
        );

        Ticket update = new Ticket(
                "1111111111111",
                "5D4169",
                "1234 123456",
                "SOMETHING NAME",
                new ContactData(
                        "+70013357481",
                        "a.moiseev_1977@postgrespro.ru"
                )
        );
        System.out.println(repository.add(add));
        System.out.println(repository.findAll().subList(0, 10));
        System.out.println(repository.findById("1111111111111"));
        System.out.println(repository.update(update));
        System.out.println(repository.findById("1111111111111"));
        System.out.println(repository.delete("1111111111111"));
    }
}
