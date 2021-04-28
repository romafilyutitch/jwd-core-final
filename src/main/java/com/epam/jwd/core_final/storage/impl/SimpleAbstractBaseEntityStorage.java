package com.epam.jwd.core_final.storage.impl;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.storage.AbstractBaseEntityStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class SimpleAbstractBaseEntityStorage<T extends AbstractBaseEntity> implements AbstractBaseEntityStorage<T> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleAbstractBaseEntityStorage.class);
    private long Id = 0;
    private final List<T> entities = new ArrayList<>();

    @Override
    public T findById(Long id) {
        Optional<T> findResult = entities.stream().filter(baseEntity -> baseEntity.getId().equals(id)).findAny();
        return findResult.orElseThrow(() -> new IllegalArgumentException("There is no such BaseEntity with this Id"));
    }

    @Override
    public T findByName(String name) {
        Optional<T> findResult = entities.stream().filter(baseEntity -> baseEntity.getName().equals(name)).findAny();
        return findResult.orElseThrow(() -> new IllegalArgumentException("There is no such BaseEntity with this name"));
    }

    @Override
    public T create(T entity) {
        entity.setId(Id);
        Id++;
        entities.add(entity);
        logger.info("Entity {} was saved in storage", entity);
        return entity;
    }

    @Override
    public T remove(T entity) {
        int indexOfEntity = entities.indexOf(entity);
        T result = entities.get(indexOfEntity);
        entities.remove(entity);
        logger.info("Entity {} was removed from storage", entity);
        return result;
    }


    @Override
    public T peek(T entity) {
        int indexOfEntity = entities.indexOf(entity);
        return entities.get(indexOfEntity);
    }

    @Override
    public List<T> getAll() {
        return entities;
    }

    @Override
    public boolean contains(T entity) {
        return entities.contains(entity);
    }
}
