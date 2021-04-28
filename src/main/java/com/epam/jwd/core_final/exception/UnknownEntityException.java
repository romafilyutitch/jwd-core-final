package com.epam.jwd.core_final.exception;

public class UnknownEntityException extends RuntimeException {

    private final String entityName;
    private final Object[] args;

    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    @Override
    public String getMessage() {
        // todo
        // you should use entityName, args (if necessary)
        return "Entity " + entityName + " is unknown entity";
    }
}
