package com.epam.jwd.core_final.storage;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;

import java.util.List;

public interface AbstractBaseEntityStorage<T extends AbstractBaseEntity>{

    T findById(Long id);

    T findByName(String name);

    T create(T entity);

    T remove(T entity);

    T peek(T entity);

    List<T> getAll();

    boolean contains(T entity);

}
