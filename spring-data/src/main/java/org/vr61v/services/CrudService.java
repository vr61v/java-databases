package org.vr61v.services;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class CrudService<E, ID> {

    protected final ListCrudRepository<E, ID> repository;

    public CrudService(ListCrudRepository<E, ID> repository) {
        this.repository = repository;
    }

    public E create(E entity) {
        return repository.save(entity);
    }

    public List<E> createAll(Iterable<E> entities) {
        return repository.saveAll(entities);
    }

    public E update(E entity) {
        return repository.save(entity);
    }

    public List<E> updateAll(Iterable<E> entities) {
        return repository.saveAll(entities);
    }

    public Optional<E> findById(ID id) {
        return repository.findById(id);
    }

    public List<E> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void deleteAll(Iterable<ID> ids) {
        repository.deleteAllById(ids);
    }

}
