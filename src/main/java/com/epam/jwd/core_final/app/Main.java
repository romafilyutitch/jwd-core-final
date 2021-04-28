package com.epam.jwd.core_final.app;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.exception.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.trace("Application started");
        try {
            Application.start();
        } catch (InvalidStateException e) {
            logger.error(e.getMessage(), e);
            System.out.println("Cannot initialize data");
        }
        logger.trace("Application is completed");
    }
}