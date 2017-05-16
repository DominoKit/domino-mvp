package com.progressoft.brix.domino.configurations;

import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static java.util.Objects.isNull;

public class ConfigurationLoader {

    private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(ConfigurationLoader.class);

    private final String configPath;
    private final String configFileName;

    public ConfigurationLoader(String configFileName) {
        this(new ConfigLocation().getDefault(), configFileName);
    }

    public ConfigurationLoader(String configPath, String configFileName) {
        if (isNull(configPath) || configPath.trim().isEmpty())
            throw new InvalidConfigLocationProvided();
        if (Objects.isNull(configFileName) || configFileName.trim().isEmpty())
            throw new InvalidConfigFileNameProvided();
        this.configPath = configPath;
        this.configFileName = configFileName;
    }

    public Configuration load() {
        if (!Paths.get(configPath).toFile().exists())
            createConfigurationDirectory();
        if (!Paths.get(getConfigFilePath()).toFile().exists())
            createConfigurationFile();
        return loadConfigurationFile();
    }

    private Configuration loadConfigurationFile() {
        try {
            LOGGER.info("::::::::::::::::::::::::::: Configurations ::::::::::::::::::::::::::::::");
            LOGGER.info("loading configuration from : " + Paths.get(getConfigFilePath()).toFile().getAbsolutePath());
            LOGGER.info("::::::::::::::::::::::::::: Configurations ::::::::::::::::::::::::::::::");
            return new PropertiesConfiguration(Paths.get(getConfigFilePath()).toFile());
        } catch (ConfigurationException e) {
            LOGGER.debug("could not load configuration file [" + configFileName + "]", e);
            throw new FailedToLoadConfigurationException(configFileName);
        }
    }

    private void createConfigurationFile() {
        try {
            Files.createFile(Paths.get(getConfigFilePath()));
        } catch (IOException e) {
            LOGGER.debug("could not creat configuration file [" + configFileName + "]", e);
            throw new FailedToCreateConfigurationFile(configFileName);
        }
    }

    private void createConfigurationDirectory() {
        try {
            Files.createDirectory(Paths.get(configPath));
        } catch (IOException e) {
            LOGGER.debug("could not create configuration directory [" + configPath + "]", e);
            throw new FailedToCreateConfigurationPath();
        }
    }

    public String getConfigFilePath() {
        return configPath + "/" + configFileName;
    }

    private class InvalidConfigLocationProvided extends RuntimeException {
    }

    private static class FailedToCreateConfigurationPath extends RuntimeException {
    }

    private static class FailedToLoadConfigurationException extends RuntimeException {
        public FailedToLoadConfigurationException(String message) {
            super(message);
        }
    }

    private static class FailedToCreateConfigurationFile extends RuntimeException {
        public FailedToCreateConfigurationFile(String message) {
            super(message);
        }
    }

    private class InvalidConfigFileNameProvided extends RuntimeException {
    }
}
