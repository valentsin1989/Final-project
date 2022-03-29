package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.GenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    protected Class<T> entityClass;

    public GenericRepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
    }

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void add(T entity) {
        em.persist(entity);
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    public T findById(I id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        String queryString = "from " + entityClass.getSimpleName();
        Query query = em.createQuery(queryString);
        return query.getResultList();
    }
}
