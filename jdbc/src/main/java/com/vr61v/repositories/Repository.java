package com.vr61v.repositories;

import java.util.List;

public interface Repository<T> {

    boolean add(T t);

    List<T> findAll();

    T findById(String id);

}
