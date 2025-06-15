package org.vr61v.controllers.v1.custom;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.FlightDto;
import org.vr61v.entities.Flight;
import org.vr61v.mappers.FlightMapper;
import org.vr61v.services.crud.FlightCrudService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/flights")
public class FlightController {

    private final FlightCrudService crudService;

    private final FlightMapper mapper;

    public FlightController(
            FlightCrudService flightCrudService,
            FlightMapper flightMapper
    ) {
        this.crudService = flightCrudService;
        this.mapper = flightMapper;
    }

    @PostMapping("/{no}")
    public ResponseEntity<?> create(@PathVariable String no, @Valid @RequestBody FlightDto body) {
        log.info("Handling request to create the flight entity with no:{}", no);
        Flight entity = mapper.toEntity(body);
        entity.setFlightNo(no);
        log.info("Creating a new flight entity with no:{}, body:{}", no, body);
        Flight created = crudService.create(entity);
        FlightDto dto = mapper.toDto(created);
        log.info("Success created a new flight with no:{}, dto:{}", no, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@Valid @RequestBody List<FlightDto> body) {
        log.info("Handling request to create all size:{} flight entities", body.size());
        List<Flight> entities = body.stream().map(mapper::toEntity).toList();
        List<Flight> created = crudService.createAll(entities);
        List<FlightDto> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success created all new flight entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody FlightDto body) {
        log.info("Handling request to update the flight entity with id:{}", id);
        Flight entity = mapper.toEntity(body);
        entity.setFlightId(id);
        log.info("Updating the flight entity with id:{}, body:{}", id, body);
        Flight updated = crudService.update(entity);
        FlightDto dto = mapper.toDto(updated);
        log.info("Success updated the flight entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@Valid @RequestBody List<FlightDto> body) {
        log.info("Handling request to update all size:{} flight entities", body.size());
        List<Flight> entities = body.stream().map(mapper::toEntity).toList();
        List<Flight> updated = crudService.updateAll(entities);
        List<FlightDto> dtos = updated.stream().map(mapper::toDto).toList();
        log.info("Success updating all flight entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        log.info("Handling request to find the flight entity with id:{}", id);
        Optional<Flight> found = crudService.findById(id);
        if (found.isPresent()) {
            FlightDto dto = mapper.toDto(found.get());
            log.info("Success finding the flight entity with id:{}, dto:{}", id, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No flight entity with id:{} found", id);
            return new ResponseEntity<>(
                    String.format("No flight entity with id:%s found", id),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("Handling request to find all flight entities");
        List<Flight> found = crudService.findAll();
        List<FlightDto> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            log.info("Success finding all flight entities with dto size:{}", dtos.size());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            log.warn("No flight entities found");
            return new ResponseEntity<>(
                    "No flight entities found",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("Handling request to delete the flight entity with id:{}", id);
        Optional<Flight> found = crudService.findById(id);
        if (found.isPresent()) {
            log.info("Deleting the flight entity with id:{}", id);
            crudService.deleteById(id);
            log.info("Success deleting the flight entity with id:{}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No flight entity with id:{} found", id);
            return new ResponseEntity<>(
                    String.format("No flight entity with id:%s found", id),
                    HttpStatus.NOT_FOUND
            );
        }
    }


    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody List<Integer> ids) {
        log.info("Handling request to delete all size:{} flight entities", ids.size());
        List<Flight> found = crudService.findAllById(ids);
        if (found.size() != ids.size()) {
            found.stream()
                    .map(Flight::getFlightId)
                    .forEach(ids::remove);
            log.warn("No flight entities with ids:{} found", ids);
            return new ResponseEntity<>(
                    String.format("No flight entities with ids:%s found", ids),
                    HttpStatus.NOT_FOUND
            );
        }

        log.info("Deleting all flight entities with body size:{}", ids.size());
        crudService.deleteAll(ids);
        log.info("Success deleting all flight entities with body size:{}", ids.size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
