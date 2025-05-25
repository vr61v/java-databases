package com.vr61v;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", schema = "bookings")
public class Bookings {
    @Id
    @Column(name = "book_ref")
    private String bookRef;

    @Column(name = "book_date")
    private OffsetDateTime bookDate;

    @Column(name = "total_amount")
    private Float totalAmount;
}
