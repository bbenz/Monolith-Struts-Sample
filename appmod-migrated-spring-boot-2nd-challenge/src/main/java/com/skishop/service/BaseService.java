package com.skishop.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class BaseService<T, ID> {
    protected final JpaRepository<T, ID> repository;

    protected BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() { return repository.findAll(); }
    public Optional<T> findById(ID id) { return repository.findById(id); }

    @Transactional
    public T save(T entity) { return repository.save(entity); }

    @Transactional
    public void deleteById(ID id) { repository.deleteById(id); }
}
