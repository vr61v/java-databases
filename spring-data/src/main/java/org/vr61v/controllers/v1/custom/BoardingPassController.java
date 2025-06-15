package org.vr61v.controllers.v1.custom;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

@Slf4j
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
            @Valid @RequestBody BoardingPassDto body
    ) {
        log.info("Handling request to create a new boarding pass entity with id:{}, no:{}", id, no);
        BoardingPass entity = getRequestEntity(id, no, body);
        log.info("Creating a new boarding pass entity with id:{}, body:{}", id, body);
        BoardingPass created = boardingPassCrudService.create(entity);
        BoardingPassDto dto = mapper.toDto(created);
        log.info("Success creating a new boarding pass entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@Valid @RequestBody List<BoardingPassDto> body) {
        log.info("Handling request to create all size:{} boarding pass entities", body.size());
        List<BoardingPass> entities = getRequestEntityList(body);
        List<BoardingPass> created = boardingPassCrudService.createAll(entities);
        List<BoardingPassDto> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success creating all new boarding pass entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{no}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @PathVariable("no") Integer no,
            @Valid @RequestBody BoardingPassDto body
    ) {
        log.info("Handling request to update the boarding pass entity with id:{}", id);
        BoardingPass entity = getRequestEntity(id, no, body);
        log.info("Updating the boarding pass entity with id:{}, body:{}", id, body);
        BoardingPass updated = boardingPassCrudService.update(entity);
        BoardingPassDto dto = mapper.toDto(updated);
        log.info("Success updating the boarding pass entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@Valid @RequestBody List<BoardingPassDto> body) {
        log.info("Handling request to update all size:{} boarding pass entities", body.size());
        List<BoardingPass> entities = getRequestEntityList(body);
        List<BoardingPass> updated = boardingPassCrudService.updateAll(entities);
        List<BoardingPassDto> dtos = updated.stream().map(mapper::toDto).toList();
        log.info("Success updating all boarding pass entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/{no}")
    public ResponseEntity<?> findById(
            @PathVariable("id") String id,
            @PathVariable("no") Integer no
    ) {
        log.info("Handling request to find the boarding pass entity with id:{}", id);
        TicketFlightID entityId = createId(id, no);
        Optional<BoardingPass> found = boardingPassCrudService.findById(entityId);
        if (found.isPresent()) {
            BoardingPassDto dto = mapper.toDto(found.get());
            log.info("Success finding the boarding pass entity with id:{}, dto:{}", id, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No boarding pass entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No boarding pass entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("Handling request to find all boarding pass entities");
        List<BoardingPass> found = boardingPassCrudService.findAll();
        List<BoardingPassDto> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            log.info("Success finding all boarding pass entities with dto size:{}", dtos.size());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            log.warn("No boarding pass entity found");
            return new ResponseEntity<>(
                    "No boarding pass entity found",
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
        log.info("Handling request to delete the boarding pass entity with id:{}", entityId);
        Optional<BoardingPass> found = boardingPassCrudService.findById(entityId);
        if (found.isPresent()) {
            log.info("Deleting the boarding pass entity with id:{}", entityId);
            boardingPassCrudService.deleteById(entityId);
            log.info("Success deleting the boarding pass entity with id:{}", entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No boarding pass entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No boarding pass entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody List<BoardingPassDto> boardingPassDtos) {
        log.info("Handling request to delete all size:{} boarding pass entities", boardingPassDtos.size());
        Set<TicketFlightID> ids = new HashSet<>();
        for (BoardingPassDto dto : boardingPassDtos) {
            ids.add(createId(dto.getTicketNo(), dto.getFlightId()));
        }

        List<BoardingPass> found = boardingPassCrudService.findAllById(ids);
        if (found.size() != ids.size()) {
            found.stream()
                    .map(BoardingPass::getId)
                    .forEach(ids::remove);
            log.warn("No boarding pass entities with ids:{} found", ids);
            return new ResponseEntity<>(
                    String.format("No boarding pass entities with ids:%s found", ids),
                    HttpStatus.NOT_FOUND
            );
        }

        log.info("Deleting all boarding pass entities with body size:{}", ids.size());
        boardingPassCrudService.deleteAll(ids);
        log.info("Success deleting all boarding pass entities with body size:{}", ids.size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
