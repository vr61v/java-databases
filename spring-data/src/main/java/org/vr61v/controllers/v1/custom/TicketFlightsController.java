package org.vr61v.controllers.v1.custom;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/v1/ticketflights")
public class TicketFlightsController {

    private final TicketFlightCrudService crudService;

    private final TicketCrudService ticketCrudService;

    private final FlightCrudService flightCrudService;

    private final TicketFlightMapper mapper;

    public TicketFlightsController(
            TicketFlightCrudService ticketFlightCrudService,
            TicketCrudService ticketCrudService,
            FlightCrudService flightCrudService,
            TicketFlightMapper ticketFlightMapper
    ) {
        this.crudService = ticketFlightCrudService;
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
            @Valid @RequestBody TicketFlightDto body
    ) {
        log.info("Handling request to create the ticket flight entity with id:{}, no:{}", id, no);
        TicketFlight entity = getRequestEntity(id, no, body);
        log.info("Creating a new ticket flight entity with no:{}, body:{}", no, body);
        TicketFlight created = crudService.create(entity);
        TicketFlightDto dto = mapper.toDto(created);
        log.info("Success created a new ticket flight with no:{}, dto:{}", no, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@Valid @RequestBody List<TicketFlightDto> body) {
        log.info("Handling request to create all size:{} ticket flight entities", body.size());
        List<TicketFlight> entities = getRequestEntityList(body);
        List<TicketFlight> created = crudService.createAll(entities);
        List<TicketFlightDto> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success created all new ticket flight entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{no}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @PathVariable("no") Integer no,
            @Valid @RequestBody TicketFlightDto body
    ) {
        log.info("Handling request to update the ticket flight entity with id:{}", id);
        TicketFlight entity = getRequestEntity(id, no, body);
        log.info("Updating the ticket flight entity with id:{}, body:{}", id, body);
        TicketFlight updated = crudService.update(entity);
        TicketFlightDto dto = mapper.toDto(updated);
        log.info("Success updated the ticket flight entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@Valid @RequestBody List<TicketFlightDto> body) {
        log.info("Handling request to update all size:{} ticket flight entities", body.size());
        List<TicketFlight> entities = getRequestEntityList(body);
        List<TicketFlight> updated = crudService.updateAll(entities);
        List<TicketFlightDto> dtos = updated.stream().map(mapper::toDto).toList();
        log.info("Success updating all ticket flight entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/{no}")
    public ResponseEntity<?> findById(
            @PathVariable("id") String id,
            @PathVariable("no") Integer no
    ) {
        TicketFlightID entityId = createId(id, no);
        log.info("Handling request to find the ticket flight entity with id:{}", entityId);
        Optional<TicketFlight> found = crudService.findById(entityId);
        if (found.isPresent()) {
            TicketFlightDto dto = mapper.toDto(found.get());
            log.info("Success finding the ticket flight entity with id:{}, dto:{}", entityId, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No ticket flight entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No ticket flight entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("Handling request to find all ticket flight entities");
        List<TicketFlight> found = crudService.findAll();
        List<TicketFlightDto> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            log.info("Success finding all ticket flight entities with dto size:{}", dtos.size());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            log.warn("No ticket flight entities found");
            return new ResponseEntity<>(
                    "No ticket flight entities found",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{id}/{no}")
    public ResponseEntity<?> delete(
            @PathVariable("id") String id,
            @PathVariable("no") Integer no
    ) {
        TicketFlightID entityId = createId(id, no);
        log.info("Handling request to delete the ticket flight entity with id:{}", entityId);
        Optional<TicketFlight> found = crudService.findById(entityId);
        if (found.isPresent()) {
            log.info("Deleting the ticket flight entity with id:{}", entityId);
            crudService.deleteById(entityId);
            log.info("Success deleting the ticket flight entity with id:{}", entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No ticket flight entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No ticket flight entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody List<TicketFlightDto> TicketFlightDtos) {
        Set<TicketFlightID> ids = new HashSet<>();
        for (TicketFlightDto dto : TicketFlightDtos) {
            ids.add(createId(dto.getTicketNo(), dto.getFlightId()));
        }

        log.info("Handling request to delete all size:{} ticket flight entities", ids.size());
        List<TicketFlight> found = crudService.findAllById(ids);
        if (found.size() != ids.size()) {
            found.stream()
                    .map(TicketFlight::getId)
                    .forEach(ids::remove);
            log.warn("No ticket flight entities with ids:{} found", ids);
            return new ResponseEntity<>(
                    String.format("No ticket flight entities with ids:%s found", ids),
                    HttpStatus.NOT_FOUND
            );
        }

        log.info("Deleting all ticket flight entities with body size:{}", ids.size());
        crudService.deleteAll(ids);
        log.info("Success deleting all ticket flight entities with body size:{}", ids.size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
