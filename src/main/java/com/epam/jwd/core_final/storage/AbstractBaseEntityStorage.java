package com.epam.jwd.core_final.storage;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;

import java.util.List;

public interface AbstractBaseEntityStorage<T extends AbstractBaseEntity> {

    T create(T entity);

    List<T> getAll();

}
