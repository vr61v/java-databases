package org.vr61v.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.mappers.BaseMapper;
import org.vr61v.services.CrudService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class CrudController<E, DTO, ID> {

    private final CrudService<E, ID> crudService;

    private final BaseMapper<E, DTO> mapper;

    protected abstract void setId(E entity, ID id);

    @PostMapping("/{id}")
    public ResponseEntity<?> create(@PathVariable ID id, @RequestBody DTO body) {
        E entity = mapper.toEntity(body);
        setId(entity, id);
        E created = crudService.create(entity);
        DTO dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@RequestBody List<DTO> body) {
        List<E> entities = body.stream().map(mapper::toEntity).toList();
        List<E> created = crudService.createAll(entities);
        List<DTO> dtos = created.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody DTO body) {
        E entity = mapper.toEntity(body);
        setId(entity, id);
        E updated = crudService.update(entity);
        DTO dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@RequestBody List<DTO> body) {
        List<E> entities = body.stream().map(mapper::toEntity).toList();
        List<E> updated = crudService.updateAll(entities);
        List<DTO> dtos = updated.stream().map(mapper::toDto).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") ID id) {
        Optional<E> found = crudService.findById(id);
        return found.isEmpty() ?
                new ResponseEntity<>(
                        String.format("entity with id:%s was not found", id),
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(mapper.toDto(found.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<E> found = crudService.findAll();
        List<DTO> dtos = found.stream().map(mapper::toDto).toList();
        return found.isEmpty() ?
                new ResponseEntity<>(
                        "entities was not found",
                        HttpStatus.BAD_REQUEST
                ) :
                new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        Optional<E> found = crudService.findById(id);
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
    public ResponseEntity<?> deleteAll(@RequestBody List<ID> ids) {
        List<E> found = crudService.findAllById(ids);
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
