package org.vr61v.controllers.v1.custom;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.SeatDto;
import org.vr61v.embedded.SeatID;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.Seat;
import org.vr61v.mappers.SeatMapper;
import org.vr61v.services.crud.AircraftCrudService;
import org.vr61v.services.crud.SeatCrudService;
import org.vr61v.services.custom.SeatCustomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/aircrafts/{code}/seats")
public class SeatController {

    private final SeatCrudService crudService;

    private final SeatCustomService seatCustomService;

    private final AircraftCrudService aircraftCrudService;

    private final SeatMapper mapper;

    public SeatController(
            SeatCrudService seatCrudService,
            SeatCustomService seatCustomService,
            AircraftCrudService aircraftCrudService,
            SeatMapper seatMapper
    ) {
        this.crudService = seatCrudService;
        this.seatCustomService = seatCustomService;
        this.aircraftCrudService = aircraftCrudService;
        this.mapper = seatMapper;
    }

    private SeatID createId(String code, String no) {
        Optional<Aircraft> aircraft = aircraftCrudService.findById(code);
        if (aircraft.isEmpty()) {
            throw new IllegalArgumentException("aircraft not found");
        }
        return SeatID.builder()
                .aircraft(aircraft.get())
                .seatNo(no)
                .build();
    }

    private Seat getRequestEntity(String code, String no, SeatDto body) {
        Seat entity = mapper.toEntity(body);
        entity.setId(createId(code, no));
        return entity;
    }

    private List<Seat> getRequestEntityList(String code, List<SeatDto> body) {
        List<Seat> entities = new ArrayList<>();
        for (SeatDto dto : body) {
            Seat entity = mapper.toEntity(dto);
            entity.setId(createId(code, dto.getSeatNo()));
            entities.add(entity);
        }

        return entities;
    }

    @PostMapping("/{no}")
    public ResponseEntity<?> create(
            @PathVariable("code") String code,
            @PathVariable("no") String no,
            @Valid @RequestBody SeatDto body
    ) {
        log.info("Handling request to create a new seat entity with code:{}, no:{}", code, no);
        Seat entity = getRequestEntity(code, no, body);
        log.info("Creating a new seat entity with code:{}, no:{}, body:{}", code, no, body);
        Seat created = crudService.create(entity);
        SeatDto dto = mapper.toDto(created);
        log.info("Success creating a new seat entity with code:{}, no:{}, dto:{}", code, no, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(
            @PathVariable("code") String code,
            @Valid @RequestBody List<SeatDto> body
    ) {
        log.info("Handling request to create all size:{} seat entities", body.size());
        List<Seat> entity = getRequestEntityList(code, body);
        List<Seat> created = crudService.createAll(entity);
        List<SeatDto> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success creating all new seat entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{no}")
    public ResponseEntity<?> update(
            @PathVariable("code") String code,
            @PathVariable("no") String no,
            @Valid @RequestBody SeatDto body
    ) {
        log.info("Handling request to update the seat entity with code:{}, no:{}", code, no);
        Seat entity = getRequestEntity(code, no, body);
        log.info("Updating the seat entity with code:{}, no:{}, body:{}", code, no, body);
        Seat updated = crudService.update(entity);
        SeatDto dto = mapper.toDto(updated);
        log.info("Success updating the seat entity with code:{}, no:{}, dto:{}", code, no, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(
            @PathVariable("code") String code,
            @Valid @RequestBody List<SeatDto> body
    ) {
        log.info("Handling request to update all size:{} seat entities", body.size());
        List<Seat> entity = getRequestEntityList(code, body);
        List<Seat> created = crudService.updateAll(entity);
        List<SeatDto> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success updating all seat entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @GetMapping("/{no}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String code,
            @PathVariable("no") String no
    ) {
        SeatID entityId = createId(code, no);
        log.info("Handling request to find the seat entity with id:{}", entityId);
        Optional<Seat> found = crudService.findById(entityId);
        if (found.isPresent()) {
            SeatDto dto = mapper.toDto(found.get());
            log.info("Success finding the seat entity with id:{}, dto:{}", entityId, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No seat entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No seat entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll(@PathVariable String code) {
        log.info("Handling request to find all seat entities");
        List<Seat> found = crudService.findAll();
        List<SeatDto> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            log.info("Success finding all seat entities with dto size:{}", dtos.size());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            log.warn("No seat entities found");
            return new ResponseEntity<>(
                    "No seat entities found",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<?> delete(
            @PathVariable("code") String code,
            @PathVariable("no") String no
    ) {
        SeatID entityId = createId(code, no);
        log.info("Handling request to delete the seat entity with id:{}", entityId);
        Optional<Seat> found = crudService.findById(entityId);
        if (found.isPresent()) {
            log.info("Deleting the seat entity with id:{}", entityId);
            crudService.deleteById(entityId);
            log.info("Success deleting the seat entity with id:{}", entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No seat entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No seat entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@PathVariable("code") String code) {
        log.info("Handling request to delete all seats from aircraft with code:{}", code);
        Optional<Aircraft> found = aircraftCrudService.findById(code);
        if (found.isPresent()) {
            log.info("Deleting the seat entities from aircraft with code:{}", code);
            seatCustomService.deleteSeatsByAircraftCode(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No aircraft with code:{} found", code);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
