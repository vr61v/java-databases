package org.vr61v.controllers.v1.custom;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.BoardingPassDto;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.BoardingPass;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.mappers.BoardingPassMapper;
import org.vr61v.services.crud.BoardingPassCrudService;
import org.vr61v.services.crud.FlightCrudService;
import org.vr61v.services.crud.TicketCrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/boardingpass")
public class BoardingPassController {

    private final BoardingPassCrudService boardingPassCrudService;

    private final TicketCrudService ticketCrudService;

    private final FlightCrudService flightCrudService;

    private final BoardingPassMapper mapper;

    public BoardingPassController(
            BoardingPassCrudService boardingPassCrudService,
            TicketCrudService ticketCrudService,
            FlightCrudService flightCrudService,
            BoardingPassMapper boardingPassMapper
    ) {
        this.boardingPassCrudService = boardingPassCrudService;
        this.ticketCrudService = ticketCrudService;
        this.flightCrudService = flightCrudService;
        this.mapper = boardingPassMapper;
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

    private BoardingPass getRequestEntity(String id, Integer no, BoardingPassDto body) {
        BoardingPass entity = mapper.toEntity(body);
        entity.setId(createId(id, no));
        return entity;
    }

    private List<BoardingPass> getRequestEntityList(List<BoardingPassDto> body) {
        List<BoardingPass> entities = new ArrayList<>();
        for (BoardingPassDto dto : body) {
            BoardingPass entity = mapper.toEntity(dto);
            entity.setId(createId(dto.getTicketNo(), dto.getFlightId()));
            entities.add(entity);
        }

        return entities;
    }

    @PostMapping("/{id}/{no}")
    public ResponseEntity<?> create(
            @PathVariable String id,
            @PathVariable("no") Integer no,
            @RequestBody BoardingPassDto body
    ) {
        BoardingPass entity = getRequestEntity(id, no, body);
        BoardingPass created = boardingPassCrudService.create(entity);
        BoardingPassDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@RequestBody List<BoardingPassDto> body) {
        List<BoardingPass> entities = getRequestEntityList(body);
        List<BoardingPass> created = boardingPassCrudService.createAll(entities);
        List<BoardingPassDto> dtos = created.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{no}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @PathVariable("no") Integer no,
            @RequestBody BoardingPassDto body
    ) {
        BoardingPass entity = getRequestEntity(id, no, body);
        BoardingPass updated = boardingPassCrudService.update(entity);
        BoardingPassDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@RequestBody List<BoardingPassDto> body) {
        List<BoardingPass> entities = getRequestEntityList(body);
        List<BoardingPass> updated = boardingPassCrudService.updateAll(entities);
        List<BoardingPassDto> dtos = updated.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/{no}")
    public ResponseEntity<?> findById(@PathVariable("id") String id, @PathVariable("no") Integer no) {
        TicketFlightID entityId = createId(id, no);
        Optional<BoardingPass> found = boardingPassCrudService.findById(entityId);
        return found.isEmpty() ?
                new ResponseEntity<>(
                        String.format("entity with id:%s and no:%s not found", id, no),
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(mapper.toDto(found.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<BoardingPass> found = boardingPassCrudService.findAll();
        List<BoardingPassDto> dtos = found.stream().map(mapper::toDto).toList();
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
        Optional<BoardingPass> found = boardingPassCrudService.findById(entityId);
        if (found.isEmpty()) {
            new ResponseEntity<>(
                    String.format("entity with id:%s and no:%s not found", id, no),
                    HttpStatus.BAD_REQUEST
            );
        }

        boardingPassCrudService.deleteById(entityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody List<BoardingPassDto> boardingPassDtos) {
        List<TicketFlightID> ids = new ArrayList<>();
        for (BoardingPassDto dto : boardingPassDtos) {
            ids.add(createId(dto.getTicketNo(), dto.getFlightId()));
        }

        boardingPassCrudService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
