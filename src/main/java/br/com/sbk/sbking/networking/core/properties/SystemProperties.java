package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.HOST;

public class SystemProperties {

    public String getHost() {
        return System.getProperty(HOST);
    }

}
