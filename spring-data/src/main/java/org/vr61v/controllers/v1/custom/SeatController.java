package org.vr61v.controllers.v1.custom;

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

@RestController
@RequestMapping("api/v1/aircrafts/{code}/seats")
public class SeatController {

    private final SeatCrudService seatCrudService;

    private final SeatCustomService seatCustomService;

    private final AircraftCrudService aircraftCrudService;

    private final SeatMapper mapper;

    public SeatController(
            SeatCrudService seatCrudService,
            SeatCustomService seatCustomService,
            AircraftCrudService aircraftCrudService,
            SeatMapper seatMapper
    ) {
        this.seatCrudService = seatCrudService;
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
            @RequestBody SeatDto body
    ) {
        Seat entity = getRequestEntity(code, no, body);
        Seat created = seatCrudService.create(entity);
        SeatDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(
            @PathVariable("code") String code,
            @RequestBody List<SeatDto> body
    ) {
        List<Seat> entity = getRequestEntityList(code, body);
        List<Seat> created = seatCrudService.createAll(entity);
        List<SeatDto> dto = created.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{no}")
    public ResponseEntity<?> update(
            @PathVariable("code") String code,
            @PathVariable("no") String no,
            @RequestBody SeatDto body
    ) {
        Seat entity = getRequestEntity(code, no, body);
        Seat updated = seatCrudService.update(entity);
        SeatDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(
            @PathVariable("code") String code,
            @RequestBody List<SeatDto> body
    ) {
        List<Seat> entity = getRequestEntityList(code, body);
        List<Seat> created = seatCrudService.updateAll(entity);
        List<SeatDto> dto = created.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{no}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String code,
            @PathVariable("no") String no
    ) {
        SeatID entityId = createId(code, no);
        Optional<Seat> found = seatCrudService.findById(entityId);
        return found.isEmpty() ?
                new ResponseEntity<>(
                        String.format("entity with code:%s and no:%s not found", code, no),
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(mapper.toDto(found.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@PathVariable String code) {
        List<Seat> found = seatCustomService.findSeatsByAircraftCode(code);
        List<SeatDto> dtos = found.stream().map(mapper::toDto).toList();
        return found.isEmpty() ?
                new ResponseEntity<>(
                        "entities not found",
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<?> delete(
            @PathVariable("code") String code,
            @PathVariable("no") String no
    ) {
        SeatID entityId = createId(code, no);
        Optional<Seat> found = seatCrudService.findById(entityId);
        if (found.isEmpty()) {
            new ResponseEntity<>(
                    String.format("entity with code:%s and no:%s not found", code, no),
                    HttpStatus.BAD_REQUEST
            );
        }

        seatCrudService.deleteById(entityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@PathVariable("code") String code) {
        seatCustomService.deleteSeatsByAircraftCode(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
