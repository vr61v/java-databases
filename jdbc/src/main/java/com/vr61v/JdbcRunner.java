package com.vr61v;

import com.vr61v.entities.ContactData;
import com.vr61v.entities.Ticket;
import com.vr61v.repositories.Repository;
import com.vr61v.repositories.TicketsRepository;
import com.vr61v.utils.RepositoryConnectionManager;

public class JdbcRunner {
    public static void main(String[] args) {
//        RepositoryTestsConnectionManager manager = new RepositoryTestsConnectionManager();
        RepositoryConnectionManager manager = new RepositoryConnectionManager();
        Repository<Ticket> repository = new TicketsRepository(manager);

        Ticket add = new Ticket(
                "1111111111111",
                "000004",
                "5112 696349",
                "VIKTORIYA SMIRNOVA",
                new ContactData(
                        "+70013357481",
                        "a.moiseev_1977@postgrespro.ru"
                )
        );

        Ticket update = new Ticket(
                "1111111111111",
                "000004",
                "1234 123456",
                "SOMETHING NAME",
                new ContactData(
                        "+70013357481",
                        "a.moiseev_1977@postgrespro.ru"
                )
        );

        System.out.println("=".repeat(200));
        System.out.println("CREATING TICKET");
        System.out.println(repository.add(add));
//
//        System.out.println("=".repeat(200));
//        System.out.println("FIND ALL TICKETS");
//        System.out.println(repository.findAll().size());
//
//        System.out.println("=".repeat(200));
//        System.out.println("FIND FIRST PAGE OF TICKETS");
//        repository.findPage(0, 5).forEach(System.out::println);
//
//        System.out.println("=".repeat(200));
//        System.out.println("FIND TICKET BY ID (BEFORE UPDATE)");
//        System.out.println(repository.findById("1111111111111"));
//
//        System.out.println("=".repeat(200));
//        System.out.println("UPDATING TICKET");
//        System.out.println(repository.update(update));
//
//        System.out.println("=".repeat(200));
//        System.out.println("FIND TICKET BY ID (AFTER UPDATE)");
//        System.out.println(repository.findById("1111111111111"));
//
//        System.out.println("=".repeat(200));
//        System.out.println("DELETE TICKET BY ID");
//        System.out.println(repository.delete("1111111111111"));
//
//
//        System.out.println("=".repeat(200));
//        System.out.println("CREATING TICKETS (MANY)");
//        Ticket add1 = new Ticket(
//                "1111111111111",
//                "000000",
//                "5112 696349",
//                "VIKTORIYA SMIRNOVA",
//                new ContactData(
//                        "+70013357481",
//                        "a.moiseev_1977@postgrespro.ru"
//                )
//        );
//        Ticket add2 = new Ticket(
//                "1111111111112",
//                "000000",
//                "5112 696349",
//                "VIKTORIYA SMIRNOVA",
//                new ContactData(
//                        "+70013357481",
//                        "a.moiseev_1977@postgrespro.ru"
//                )
//        );
//        Ticket add3 = new Ticket(
//                "1111111111113",
//                "000000",
//                "5112 696349",
//                "VIKTORIYA SMIRNOVA",
//                new ContactData(
//                        "+70013357481",
//                        "a.moiseev_1977@postgrespro.ru"
//                )
//        );
//        System.out.println(repository.addAll(List.of(add1, add2, add3)));
//
//        System.out.println("=".repeat(200));
//        System.out.println("FIND TICKETS BY ID (BEFORE UPDATE)");
//        repository.findAllById(List.of("1111111111111", "1111111111112", "1111111111113"))
//                .forEach(System.out::println);
//
//        System.out.println("=".repeat(200));
//        Ticket update1 = new Ticket(
//                "1111111111111",
//                "000000",
//                "1234 123456",
//                "SOMETHING NAME1",
//                new ContactData(
//                        "+79999999991",
//                        "needupdate1@postgrespro.ru"
//                )
//        );
//        Ticket update2 = new Ticket(
//                "1111111111112",
//                "000000",
//                "4321 654321",
//                "SOMETHING NAME2",
//                new ContactData(
//                        "+79999999992",
//                        "needupdate2@postgrespro.ru"
//                )
//        );
//        Ticket update3 = new Ticket(
//                "1111111111113",
//                "000000",
//                "1234 123456",
//                "SOMETHING NAME3",
//                new ContactData(
//                        "+79999999993",
//                        "needupdate3@postgrespro.ru"
//                )
//        );
//        System.out.println("UPDATING TICKETS");
//        System.out.println(repository.updateAll(List.of(update1, update2, update3)));
//
//        System.out.println("=".repeat(200));
//        System.out.println("FIND TICKETS BY ID (AFTER UPDATE)");
//        repository.findAllById(List.of("1111111111111", "1111111111112", "1111111111113"))
//                .forEach(System.out::println);
//
//        System.out.println("=".repeat(200));
//        System.out.println("DELETING TICKETS (MANY)");
//        System.out.println(repository.deleteAll(
//                List.of("1111111111111", "1111111111112", "1111111111113")
//        ));
//        System.out.println("=".repeat(200));
    }
}
