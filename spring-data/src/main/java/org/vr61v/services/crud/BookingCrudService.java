package org.vr61v.services.crud;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Booking;
import org.vr61v.repositories.BookingRepository;
import org.vr61v.services.CrudService;

@Service
public class BookingCrudService extends CrudService<Booking, String> {

    public BookingCrudService(BookingRepository repository) {
        super(repository);
    }

}
