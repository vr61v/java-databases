package com.vr61v.repositories;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

}
