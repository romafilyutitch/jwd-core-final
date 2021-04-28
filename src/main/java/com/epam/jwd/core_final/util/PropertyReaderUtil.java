package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.domain.ApplicationProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertyReaderUtil {

    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE_PATH = "src/main/resources/application.properties";

    private PropertyReaderUtil() {
    }

    /**
     * try-with-resource using FileInputStream
     *
     * @see {https://www.netjstech.com/2017/09/how-to-read-properties-file-in-java.html for an example}
     * <p>
     * as a result - you should populate {@link ApplicationProperties} with corresponding
     * values from property file
     */
    public static void loadProperties() {
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_PATH)) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
