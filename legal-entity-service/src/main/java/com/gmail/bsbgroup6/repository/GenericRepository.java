package com.gmail.bsbgroup6.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    void add(T entity);

    T update(T entity);

    void delete(T entity);

    T findById(I id);

    List<T> findAll();
}
