package org.vr61v.controllers.v1.custom;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.TicketFlightDto;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.entities.TicketFlight;
import org.vr61v.mappers.TicketFlightMapper;
import org.vr61v.services.crud.FlightCrudService;
import org.vr61v.services.crud.TicketCrudService;
import org.vr61v.services.crud.TicketFlightCrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo: @Valid
// todo: LOGGING
@RestController
@RequestMapping("api/v1/ticketflights")
public class TicketFlightsController {

    private final TicketFlightCrudService ticketFlightCrudService;

    private final TicketCrudService ticketCrudService;

    private final FlightCrudService flightCrudService;

    private final TicketFlightMapper mapper;

    public TicketFlightsController(
            TicketFlightCrudService ticketFlightCrudService,
            TicketCrudService ticketCrudService,
            FlightCrudService flightCrudService,
            TicketFlightMapper ticketFlightMapper
    ) {
        this.ticketFlightCrudService = ticketFlightCrudService;
        this.ticketCrudService = ticketCrudService;
        this.flightCrudService = flightCrudService;
        this.mapper = ticketFlightMapper;
    }

    private TicketFlightID createId(String id, Integer no) {
        Optional<Ticket> ticket = ticketCrudService.findById(id);
        Optional<Flight> flight = flightCrudService.findById(no);
        if (ticket.isEmpty() || flight.isEmpty()) {
            throw new IllegalArgumentException("ticket or flight not found");
        }
        return TicketFlightID.builder()
                .ticket(ticket.get())
                .flight(flight.get())
                .build();
    }

    private TicketFlight getRequestEntity(String id, Integer no, TicketFlightDto body) {
        TicketFlight entity = mapper.toEntity(body);
        entity.setId(createId(id, no));
        return entity;
    }

    private List<TicketFlight> getRequestEntityList(List<TicketFlightDto> body) {
        List<TicketFlight> entities = new ArrayList<>();
        for (TicketFlightDto dto : body) {
            TicketFlight entity = mapper.toEntity(dto);
            entity.setId(createId(dto.getTicketNo(), dto.getFlightId()));
            entities.add(entity);
        }

        return entities;
    }

    @PostMapping("/{id}/{no}")
    public ResponseEntity<?> create(
            @PathVariable String id,
            @PathVariable("no") Integer no,
            @RequestBody TicketFlightDto body
    ) {
        TicketFlight entity = getRequestEntity(id, no, body);
        TicketFlight created = ticketFlightCrudService.create(entity);
        TicketFlightDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@RequestBody List<TicketFlightDto> body) {
        List<TicketFlight> entities = getRequestEntityList(body);
        List<TicketFlight> created = ticketFlightCrudService.createAll(entities);
        List<TicketFlightDto> dtos = created.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{no}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @PathVariable("no") Integer no,
            @RequestBody TicketFlightDto body
    ) {
        TicketFlight entity = getRequestEntity(id, no, body);
        TicketFlight updated = ticketFlightCrudService.update(entity);
        TicketFlightDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@RequestBody List<TicketFlightDto> body) {
        List<TicketFlight> entities = getRequestEntityList(body);
        List<TicketFlight> updated = ticketFlightCrudService.updateAll(entities);
        List<TicketFlightDto> dtos = updated.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/{no}")
    public ResponseEntity<?> findById(@PathVariable("id") String id, @PathVariable("no") Integer no) {
        TicketFlightID entityId = createId(id, no);
        Optional<TicketFlight> found = ticketFlightCrudService.findById(entityId);
        return found.isEmpty() ?
                new ResponseEntity<>(
                        String.format("entity with id:%s and no:%s not found", id, no),
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(mapper.toDto(found.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<TicketFlight> found = ticketFlightCrudService.findAll();
        List<TicketFlightDto> dtos = found.stream().map(mapper::toDto).toList();
        return found.isEmpty() ?
                new ResponseEntity<>(
                        "entities not found",
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/{no}")
    public ResponseEntity<?> delete(@PathVariable("id") String id, @PathVariable("no") Integer no) {
        TicketFlightID entityId = createId(id, no);
        Optional<TicketFlight> found = ticketFlightCrudService.findById(entityId);
        if (found.isEmpty()) {
            new ResponseEntity<>(
                    String.format("entity with id:%s and no:%s not found", id, no),
                    HttpStatus.BAD_REQUEST
            );
        }

        ticketFlightCrudService.deleteById(entityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody List<TicketFlightDto> TicketFlightDtos) {
        List<TicketFlightID> ids = new ArrayList<>();
        for (TicketFlightDto dto : TicketFlightDtos) {
            ids.add(createId(dto.getTicketNo(), dto.getFlightId()));
        }

        ticketFlightCrudService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
