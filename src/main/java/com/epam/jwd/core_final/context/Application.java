package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.exception.InvalidStateException;

import java.util.function.Supplier;

public interface Application {

    static void start() throws InvalidStateException {
        final Supplier<ApplicationContext> applicationContextSupplier = () -> NassaContext.INSTANCE; // todo
        applicationContextSupplier.get().init();
        ApplicationMenu menu = MainMenu.INSTANCE;
        while (menu != null) {
            String input = menu.printAvailableOptions();
            menu = menu.handleUserInput(input);
        }
    }
}
