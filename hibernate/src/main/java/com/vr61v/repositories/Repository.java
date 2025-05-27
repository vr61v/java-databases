package com.vr61v.repositories;

import java.util.Optional;

public interface Repository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    T update(T entity);

    boolean delete(T entity);

}
