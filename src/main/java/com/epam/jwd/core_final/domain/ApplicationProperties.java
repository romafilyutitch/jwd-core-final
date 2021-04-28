package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.io.File;

/**
 * This class should be IMMUTABLE!
 * <p>
 * Expected fields:
 * <p>
 * inputRootDir {@link String} - base dir for all input files
 * outputRootDir {@link String} - base dir for all output files
 * crewFileName {@link String}
 * missionsFileName {@link String}
 * spaceshipsFileName {@link String}
 * <p>
 * fileRefreshRate {@link Integer}
 * dateTimeFormat {@link String} - date/time format for {@link java.time.format.DateTimeFormatter} pattern
 */
public enum ApplicationProperties {
    INSTANCE;
    //todo
    private final String pathToResources = "src/main/resources";
    private final String inputRootDir;
    private final String outputRootDir;
    private final String crewFileName;
    private final String missionsFileName;
    private final String spaceshipsFileName;
    private final String spaceMapFileName;
    private final Integer fileRefreshRate;
    private final String dateTimeFormat;

    ApplicationProperties() {
        PropertyReaderUtil.loadProperties();
        this.inputRootDir = PropertyReaderUtil.readProperty("inputRootDir");
        this.outputRootDir = PropertyReaderUtil.readProperty("outputRootDir");
        this.crewFileName = PropertyReaderUtil.readProperty("crewFileName");
        this.missionsFileName = PropertyReaderUtil.readProperty("missionsFileName");
        this.spaceshipsFileName = PropertyReaderUtil.readProperty("spaceshipsFileName");
        this.spaceMapFileName = PropertyReaderUtil.readProperty("spaceMapFileName");
        this.fileRefreshRate = Integer.valueOf(PropertyReaderUtil.readProperty("fileRefreshRate"));
        this.dateTimeFormat = PropertyReaderUtil.readProperty("dateTimeFormat");
    }

    public String getInputRootDir() {
        return pathToResources + File.separator + inputRootDir;
    }

    public String getOutputRootDir() {
        return pathToResources + File.separator + outputRootDir;
    }

    public String getCrewFileName() {
        return crewFileName;
    }

    public String getMissionsFileName() {
        return missionsFileName;
    }

    public String getSpaceshipsFileName() {
        return spaceshipsFileName;
    }

    public String getSpaceMapFileName() {
        return spaceMapFileName;
    }

    public Integer getFileRefreshRate() {
        return fileRefreshRate;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }
}
