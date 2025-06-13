package org.vr61v.controllers.v1.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vr61v.controllers.v1.CrudController;
import org.vr61v.dtos.TicketDto;
import org.vr61v.entities.Ticket;
import org.vr61v.mappers.TicketMapper;
import org.vr61v.services.crud.TicketCrudService;

@RestController
@RequestMapping("api/v1/tickets")
public class TicketCrudController extends CrudController<Ticket, TicketDto, String> {

    public TicketCrudController(
            TicketCrudService ticketCrudService,
            TicketMapper ticketMapper
    ) {
        super(ticketCrudService, ticketMapper, "ticket");
    }

    @Override
    protected void setId(Ticket entity, String no) {
        entity.setTicketNo(no);
    }

    @Override
    protected String getId(Ticket entity) {
        return entity.getTicketNo();
    }

}
