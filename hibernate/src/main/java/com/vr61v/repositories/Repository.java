package com.vr61v.repositories;

public interface Repository<T, ID> {

    T save(T entity);

    T findById(ID id);

    T update(T entity);

    void delete(T entity);

}
