package org.vr61v.controllers.v1.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vr61v.controllers.v1.CrudController;
import org.vr61v.dtos.BookingDto;
import org.vr61v.entities.Booking;
import org.vr61v.mappers.BookingMapper;
import org.vr61v.services.crud.BookingCrudService;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingCrudController extends CrudController<Booking, BookingDto, String> {

    public BookingCrudController(
            BookingCrudService bookingCrudService,
            BookingMapper bookingMapper
    ) {
        super(bookingCrudService, bookingMapper, "booking");
    }

    @Override
    protected void setId(Booking entity, String id) {
        entity.setBookRef(id);
    }

    @Override
    protected String getId(Booking entity) {
        return entity.getBookRef();
    }

}
