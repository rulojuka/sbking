package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;
import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.HOST;
import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.PORT;

import java.io.InputStreamReader;
import java.util.Properties;

public class FileProperties {

    private final Properties configFile;

    public FileProperties(String filename) {
        this.configFile = new Properties();
        try {
            configFile.load(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filename), "UTF-8"));
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

    public String getIP(String serverName) {
        return configFile.getProperty(serverName);
    }

}
