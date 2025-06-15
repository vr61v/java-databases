package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.mappers.BaseMapper;
import org.vr61v.services.CrudService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Abstract base controller providing CRUD (Create, Read, Update, Delete) operations
 * for entities with both single and batch processing support.
 * <p>
 * This controller handles conversion between entities and DTOs automatically using
 * provided mapper and delegates business logic to the underlying service.
 *
 * @param <E>   the entity type this controller manages
 * @param <DTO> the Data Transfer Object type used for input/output
 * @param <ID>  the type of entity's identifier (only simple types are supported,
 *             composite IDs require custom implementation)
 */
@Slf4j
@RequiredArgsConstructor
public abstract class CrudController<E, DTO, ID> {

    private final CrudService<E, ID> crudService;

    private final BaseMapper<E, DTO> mapper;

    private final String entityName;

    /**
     * Sets the unique identifier for the entity before save or update it.
     * <p>
     * Implementation must assign the provided ID to the entity's primary key field.
     * The specific field to set depends on the entity type.
     *
     * @param entity the entity instance to modify
     * @param id the identifier to assign to the entity
     */
    protected abstract void setId(E entity, ID id);
    protected abstract ID getId(E entity);

    @PostMapping("/{id}")
    public ResponseEntity<?> create(@PathVariable ID id, @Valid @RequestBody DTO body) {
        log.info("Handling request to create a new {} entity with id:{}", entityName, id);
        E entity = mapper.toEntity(body);
        setId(entity, id);
        log.info("Creating a new {} entity with id:{}, body:{}", entityName, id, body);
        E created = crudService.create(entity);
        DTO dto = mapper.toDto(created);
        log.info("Success creating a new {} entity with id:{}, dto:{}", entityName, id, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createAll(@Valid @RequestBody List<DTO> body) {
        log.info("Handling request to create all size:{} {} entities", body.size(), entityName);
        List<E> entities = body.stream().map(mapper::toEntity).toList();
        List<E> created = crudService.createAll(entities);
        List<DTO> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success creating all new {} entities with dto size:{}", entityName, dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable ID id, @Valid @RequestBody DTO body) {
        log.info("Handling request to update the {} entity with id:{}", entityName, id);
        E entity = mapper.toEntity(body);
        setId(entity, id);
        log.info("Updating the {} entity with id:{}, body:{}", entityName, id, body);
        E updated = crudService.update(entity);
        DTO dto = mapper.toDto(updated);
        log.info("Success updating the {} entity with id:{}, dto:{}", entityName, id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(@Valid @RequestBody List<DTO> body) {
        log.info("Handling request to update all size:{} {} entities", body.size(), entityName);
        List<E> entities = body.stream().map(mapper::toEntity).toList();
        List<E> updated = crudService.updateAll(entities);
        List<DTO> dtos = updated.stream().map(mapper::toDto).toList();
        log.info("Success updating all {} entities with dto size:{}", entityName, dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") ID id) {
        log.info("Handling request to find the {} entity with id:{}", entityName, id);
        Optional<E> found = crudService.findById(id);
        if (found.isPresent()) {
            DTO dto = mapper.toDto(found.get());
            log.info("Success finding the {} entity with id:{}, dto:{}", entityName, id, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No {} entity with id:{} found", entityName, id);
            return new ResponseEntity<>(
                    String.format("No %s entity with id:%s found", entityName, id),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("Handling request to find all {} entities", entityName);
        List<E> found = crudService.findAll();
        List<DTO> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            log.info("Success finding all {} entities with dto size:{}", entityName, dtos.size());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            log.warn("No {} entities found", entityName);
            return new ResponseEntity<>(
                    String.format("No %s entities found", entityName),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        log.info("Handling request to delete the {} entity with id:{}", entityName, id);
        Optional<E> found = crudService.findById(id);
        if (found.isPresent()) {
            log.info("Deleting the {} entity with id:{}", entityName, id);
            crudService.deleteById(id);
            log.info("Success deleting the {} entity with id:{}", entityName, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No {} entity with id:{} found", entityName, id);
            return new ResponseEntity<>(
                    String.format("No %s entity with id:%s found", entityName, id),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody Set<ID> ids) {
        log.info("Handling request to delete all size:{} {} entities", ids.size(), entityName);
        List<E> found = crudService.findAllById(ids);
        if (found.size() != ids.size()) {
            found.stream()
                    .map(this::getId)
                    .forEach(ids::remove);
            log.warn("No {} entities with ids:{} found", entityName, ids);
            return new ResponseEntity<>(
                    String.format("No %s entities with ids:%s found", entityName, ids),
                    HttpStatus.NOT_FOUND
            );
        }

        log.info("Deleting all {} entities with body size:{}", entityName, ids.size());
        crudService.deleteAll(ids);
        log.info("Success deleting all {} entities with body size:{}", entityName, ids.size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
