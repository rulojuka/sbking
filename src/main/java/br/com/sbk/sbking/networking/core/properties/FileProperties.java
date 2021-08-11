package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.File;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import br.com.sbk.sbking.core.constants.ErrorCodes;

public class FileProperties {

    private static final String BASE_PATH = "/";
    private static final String DEV_FILE_NAME = "config.dev.xml";
    private static final String PRODUCTION_FILE_NAME = "config.xml";

    private XMLConfiguration configuration;

    public FileProperties() {
        Configurations configurations = new Configurations();
        String completeDevFileName = BASE_PATH + DEV_FILE_NAME;
        String completeProductionFileName = BASE_PATH + PRODUCTION_FILE_NAME;
        File devFile = new File(completeDevFileName);
        File productionFile = new File(completeProductionFileName);
        if (devFile.exists() && !devFile.isDirectory()) {
            try {
                this.configuration = configurations.xml(completeDevFileName);
                LOGGER.info("Using properties from development file.");
            } catch (ConfigurationException e) {
                LOGGER.fatal(e);
                System.exit(ErrorCodes.COULD_NOT_READ_PROPERTIES_FILE);
            }
        } else if (productionFile.exists() && !productionFile.isDirectory()) {
            try {
                this.configuration = configurations.xml(completeProductionFileName);
                LOGGER.info("Using properties from production file.");
            } catch (ConfigurationException e) {
                LOGGER.fatal(e);
                System.exit(ErrorCodes.COULD_NOT_READ_PROPERTIES_FILE);
            }
        }
    }

    public String getHost() {
        if (this.configuration == null) {
            return null;
        }
        return this.configuration.getString(PropertiesConstants.HOST);
    }

    public Integer getPort() {
        if (this.configuration == null) {
            return null;
        }
        return this.configuration.getInt(PropertiesConstants.PORT);
    }
}
