package com.epam.jwd.core_final.domain;

import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * id {@link Long} - entity id
 * name {@link String} - entity name
 */
public abstract class AbstractBaseEntity implements BaseEntity {
    private Long id;
    private String name;


    public AbstractBaseEntity(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBaseEntity entity = (AbstractBaseEntity) o;
        return Objects.equals(id, entity.id) && Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "AbstractBaseEntity { id = " + id + ", name = " + name + "}";
    }
}
