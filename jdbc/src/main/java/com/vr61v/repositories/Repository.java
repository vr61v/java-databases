package com.vr61v.repositories;

import java.util.List;

public interface Repository<T> {

    boolean add(T t);

    boolean addAll(List<T> t);

    T findById(String id);

    List<T> findAll();

    List<T> findAllById(List<String> ids);

    List<T> findPage(int page, int size);

    boolean update(T t);

    boolean updateAll(List<T> t);

    boolean delete(String id);

    boolean deleteAll(List<String> ids);

}
