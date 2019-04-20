package br.com.sbk.sbking.networking.core.properties;

import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.HOST;
import static br.com.sbk.sbking.networking.core.properties.PropertiesConstants.PORT;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FileProperties {

	private final Properties configFile;
	final static Logger logger = Logger.getLogger(FileProperties.class);

	public FileProperties(String filename) {
		this.configFile = new Properties();
		try {
			configFile.load(this.getClass().getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			logger.error("Error trying to load configuration file: " + filename);
			logger.error(e);
		}
	}

	public String getHost() {
		return configFile.getProperty(HOST);
	}

	public String getPort() {
		return configFile.getProperty(PORT);
	}

}
