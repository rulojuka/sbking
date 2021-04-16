package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.File;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import br.com.sbk.sbking.core.constants.ErrorCodes;

public class FileProperties {

    private XMLConfiguration configuration;

    public FileProperties() {
        Configurations configurations = new Configurations();
        File devFile = new File("src/main/resources/config.dev.xml");
        File productionFile = new File("src/main/resources/config.xml");
        if (devFile.exists() && !devFile.isDirectory()) {
            try {
                this.configuration = configurations.xml("src/main/resources/config.dev.xml");
            } catch (ConfigurationException e) {

            }
        } else if (productionFile.exists() && !productionFile.isDirectory()) {
            try {
                this.configuration = configurations.xml("src/main/resources/config.xml");
            } catch (ConfigurationException e) {
                LOGGER.fatal(e);
                System.exit(ErrorCodes.COULD_NOT_READ_PROPERTIES_FILE);
            }
        }
    }

    public String getHost() {
        return this.configuration.getString(PropertiesConstants.HOST);
    }

    public Integer getPort() {
        return this.configuration.getInt(PropertiesConstants.PORT);
    }
}
