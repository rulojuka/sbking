package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.HOST;
import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.PORT;

public class SystemProperties {

    public String getHost() {
        return System.getProperty(HOST);
    }

    public String getPort() {
        return System.getProperty(PORT);
    }

}
