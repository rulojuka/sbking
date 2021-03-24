package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.HOST;
import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.PORT;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FileProperties {

    private final Properties configFile;
    private static final Logger LOGGER = LogManager.getLogger(FileProperties.class);

    public FileProperties(String filename) {
        this.configFile = new Properties();
        try {
            configFile.load(this.getClass().getClassLoader().getResourceAsStream(filename));
        } catch (Exception e) {
            LOGGER.error("Error trying to load configuration file: " + filename);
            LOGGER.debug(e);
        }
    }

    public String getHost() {
        return configFile.getProperty(HOST);
    }

    public String getPort() {
        return configFile.getProperty(PORT);
    }

}
