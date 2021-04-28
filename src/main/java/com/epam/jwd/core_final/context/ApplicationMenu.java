package com.epam.jwd.core_final.context;

// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default String printAvailableOptions() {
        return null;
    }

    default ApplicationMenu handleUserInput(String userInput) {
        return null;
    }
}
