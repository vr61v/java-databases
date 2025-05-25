package com.vr61v.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tickets")
@EqualsAndHashCode(exclude = "tickets")
@Entity
@Table(name = "bookings", schema = "bookings")
public class Booking {
    @Id
    @Column(name = "book_ref", length = 6, nullable = false)
    private String bookRef;

    @Column(name = "book_date", nullable = false)
    private OffsetDateTime bookDate;

    @Column(name = "total_amount", nullable = false)
    private Float totalAmount;

    @OneToMany(mappedBy = "booking")
    private List<Ticket> tickets;
}
