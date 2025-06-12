package org.vr61v.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tickets")
@EqualsAndHashCode(exclude = "tickets")
@Builder
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

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

}
