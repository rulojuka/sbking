package br.com.sbk.sbking.networking;

import java.util.Properties;

public class NetworkingProperties {

	Properties configFile;

	public NetworkingProperties() {
		this.configFile = new Properties();
		try {
			this.configFile.load(this.getClass().getClassLoader().getResourceAsStream("networkConfiguration.cfg"));
		} catch (Exception eta) {
			eta.printStackTrace();
		}
	}

	private String getProperty(String key) {
		String value = this.configFile.getProperty(key);
		return value;
	}

	public String getHost() {
		return this.getProperty("HOST");
	}

	public int getPort() {
		String port = this.getProperty("PORT");
		return new Integer(port);
	}

}
