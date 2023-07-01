package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.File;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import br.com.sbk.sbking.core.constants.ErrorCodes;

public class FileProperties {

    private static final String BASE_PATH = "";
    private static final String ALTERNATE_BASE_PATH = "src/main/resources/";
    private static final String DEV_FILE_NAME = "config.dev.xml";
    private static final String PRODUCTION_FILE_NAME = "config.xml";

    private String host = null;

    public FileProperties() {
        Configurations configurations = new Configurations();
        XMLConfiguration configuration = null;

        String completeDevFileName = BASE_PATH + DEV_FILE_NAME;
        String completeAlternateDevFileName = ALTERNATE_BASE_PATH + DEV_FILE_NAME;

        String completeProductionFileName = BASE_PATH + PRODUCTION_FILE_NAME;
        String completeAlternateProductionFileName = ALTERNATE_BASE_PATH + PRODUCTION_FILE_NAME;

        File devFile = new File(completeDevFileName);
        File devAlternateFile = new File(completeAlternateDevFileName);
        boolean devFileOk = devFile.exists() && !devFile.isDirectory();
        boolean devAlternateFileOk = devAlternateFile.exists() && !devAlternateFile.isDirectory();

        File productionFile = new File(completeProductionFileName);
        File productionAlternateFile = new File(completeAlternateProductionFileName);
        boolean productionFileOk = devFile.exists() && !productionFile.isDirectory();
        boolean productionAlternateFileOk = productionAlternateFile.exists() && !productionAlternateFile.isDirectory();

        try {
            if (devFileOk) {
                configuration = configurations.xml(completeDevFileName);
                LOGGER.info("Using properties from development file.");
            } else if (devAlternateFileOk) {
                configuration = configurations.xml(completeAlternateDevFileName);
                LOGGER.info("Using properties from alternate development file.");
            } else if (productionFileOk) {
                configuration = configurations.xml(completeProductionFileName);
                LOGGER.info("Using properties from production file.");
            } else if (productionAlternateFileOk) {
                configuration = configurations.xml(completeAlternateProductionFileName);
                LOGGER.info("Using properties from alternate production file.");
            }
        } catch (ConfigurationException e) {
            LOGGER.fatal(e);
            System.exit(ErrorCodes.COULD_NOT_READ_PROPERTIES_FILE);
        }

        if (configuration == null) {
            this.host = null;
        } else {
            this.host = configuration.getString(PropertiesConstants.HOST);
        }
    }

    public String getHost() {
        return this.host;
    }

}
