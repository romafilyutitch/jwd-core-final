package com.epam.jwd.core_final.storage.impl;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.storage.AbstractBaseEntityStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleAbstractBaseEntityStorage<T extends AbstractBaseEntity> implements AbstractBaseEntityStorage<T> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleAbstractBaseEntityStorage.class);
    private long Id = 0;
    private final List<T> entities = new ArrayList<>();

    @Override
    public T create(T entity) {
        entity.setId(Id);
        Id++;
        entities.add(entity);
        logger.info("Entity {} was saved in storage", entity);
        return entity;
    }

    @Override
    public List<T> getAll() {
        return entities;
    }

}
