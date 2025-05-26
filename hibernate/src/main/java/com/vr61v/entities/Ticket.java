package com.vr61v.entities;

import com.vr61v.entities.embedded.ContactData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tickets", schema = "bookings")
public class Ticket {

    @Id
    @Column(name = "ticket_no", length = 13, nullable = false)
    private String ticketNo;

    @ManyToOne
    @JoinColumn(name = "book_ref", nullable = false)
    private Booking booking;

    @Column(name = "passenger_id", length = 20, nullable = false)
    private String passengerId;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contact_data")
    private ContactData contactData;

}
