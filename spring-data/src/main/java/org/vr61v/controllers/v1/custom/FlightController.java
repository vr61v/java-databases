package org.vr61v.controllers.v1.custom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.FlightDto;
import org.vr61v.entities.Flight;
import org.vr61v.mappers.FlightMapper;
import org.vr61v.services.crud.FlightCrudService;

import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<?> create(@PathVariable String no, @RequestBody FlightDto body) {
        Flight entity = mapper.toEntity(body);
        entity.setFlightNo(no);
        Flight created = crudService.create(entity);
        FlightDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@RequestBody List<FlightDto> body) {
        List<Flight> entities = body.stream().map(mapper::toEntity).toList();
        List<Flight> created = crudService.createAll(entities);
        List<FlightDto> dtos = created.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody FlightDto body) {
        Flight entity = mapper.toEntity(body);
        entity.setFlightId(id);
        Flight updated = crudService.update(entity);
        FlightDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@RequestBody List<FlightDto> body) {
        List<Flight> entities = body.stream().map(mapper::toEntity).toList();
        List<Flight> updated = crudService.updateAll(entities);
        List<FlightDto> dtos = updated.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        Optional<Flight> found = crudService.findById(id);
        return found.isEmpty() ?
                new ResponseEntity<>(
                        String.format("entity with id:%s was not found", id),
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(mapper.toDto(found.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Flight> found = crudService.findAll();
        List<FlightDto> dtos = found.stream().map(mapper::toDto).toList();
        return found.isEmpty() ?
                new ResponseEntity<>(
                        "entities was not found",
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<Flight> found = crudService.findById(id);
        if (found.isEmpty()) {
            return new ResponseEntity<>(
                    String.format("entity with id:%s was not found", id),
                    HttpStatus.NOT_FOUND
            );
        }

        crudService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody List<Integer> ids) {
        List<Flight> found = crudService.findAllById(ids);
        if (found.size() != ids.size()) {
            return new ResponseEntity<>(
                    String.format("some entities with ids:%s was not found", ids),
                    HttpStatus.NOT_FOUND
            );
        }

        crudService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
