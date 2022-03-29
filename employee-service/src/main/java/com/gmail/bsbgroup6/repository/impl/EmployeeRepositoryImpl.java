package com.gmail.bsbgroup6.repository.impl;

import com.gmail.bsbgroup6.repository.EmployeeRepository;
import com.gmail.bsbgroup6.repository.model.Employee;
import com.gmail.bsbgroup6.repository.model.Pagination;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl extends GenericRepositoryImpl<Long, Employee>
        implements EmployeeRepository {

    @Override
    public List<Employee> findByPagination(Pagination pagination) {
        String queryString = "select e from Employee as e order by e.id asc";
        int page = pagination.getPage();
        int maxResult = pagination.getMaxResult();
        Query query = em.createQuery(queryString);
        query.setFirstResult((maxResult * page) - maxResult);
        query.setMaxResults(maxResult);
        try {
            return (List<Employee>) query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Employee> findByFullName(String name) {
        String queryString = "select e from Employee as e where e.fullName like :name";
        Query query = em.createQuery(queryString);
        query.setParameter("name", name + "%");
        try {
            return (List<Employee>) query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Employee> findByName(String fullName) {
        String queryString = "select e from Employee as e where e.fullName=:fullName";
        Query query = em.createQuery(queryString);
        query.setParameter("fullName", fullName);
        Employee employee;
        try {
            employee = (Employee) query.getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Employee> findByPersonIbanByn(String personIbanByn) {
        String queryString = "select e from Employee as e where e.personIbanByn=:personIbanByn";
        Query query = em.createQuery(queryString);
        query.setParameter("personIbanByn", personIbanByn);
        Employee employee;
        try {
            employee = (Employee) query.getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Employee> findByPersonIbanCurrency(String personIbanCurrency) {
        String queryString = "select e from Employee as e where e.personIbanCurrency=:personIbanCurrency";
        Query query = em.createQuery(queryString);
        query.setParameter("personIbanCurrency", personIbanCurrency);
        Employee employee;
        try {
            employee = (Employee) query.getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
