package org.example.service;

import org.example.dao.GenericDAO;

import java.io.Serializable;
import java.util.List;

public class CrudService<T, ID extends Serializable> {

    private final GenericDAO<T, ID> dao;

    public CrudService(Class<T> entityClass) {
        this.dao = new GenericDAO<>(entityClass);
    }

    protected CrudService(GenericDAO<T, ID> dao) {
        this.dao = dao;
    }

    public T getById(ID id) {
        return dao.find(id);
    }

    public List<T> getAll() {
        return dao.findAll();
    }

    public void create(T entity) {
        requireNonNull(entity);
        dao.create(entity);
    }

    public T update(T entity) {
        requireNonNull(entity);
        return dao.update(entity);
    }

    public void delete(T entity) {
        requireNonNull(entity);
        dao.delete(entity);
    }

    private void requireNonNull(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entité ne doit pas être nulle");
        }
    }
}
