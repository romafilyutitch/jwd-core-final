package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {
    protected Long idEquals;
    protected String nameEquals;

    public boolean matches(T entity) {
        return checkIfIdEquals(entity) && checkIfNameEquals(entity);
    }

    public List<String> getListOfCriteriaNames() {
        List<String> criteriaNames = new ArrayList<>();
        criteriaNames.add("idEquals");
        criteriaNames.add("nameEquals");
        return criteriaNames;
    }

    private boolean checkIfIdEquals(T entity) {
        return idEquals == null || idEquals.equals(entity.getId());
    }

    private boolean checkIfNameEquals(T entity) {
        return nameEquals == null || nameEquals.equals(entity.getName());
    }

}
