package br.com.sbk.sbking.networking.core.properties;

public class NetworkingProperties {

    private final FileProperties fileProperties;
    private final SystemProperties systemProperties;
    private static final int DEFAULT_PORT = 60000;

    public NetworkingProperties(FileProperties fileProperties, SystemProperties systemProperties) {
        this.fileProperties = fileProperties;
        this.systemProperties = systemProperties;
    }

    public String getHost() {
        String hostFromSystem = this.systemProperties.getHost();
        if (hostFromSystem != null) {
            return hostFromSystem;
        } else {
            if (this.fileProperties == null) {
                return null;
            }
            return this.fileProperties.getHost();
        }
    }

    public int getPort() {
        String portFromSystem = this.systemProperties.getPort();
        if (portFromSystem != null) {
            return Integer.parseInt(portFromSystem);
        } else {
            if (this.fileProperties == null) {
                return DEFAULT_PORT;
            }
            Integer portFromFile = this.fileProperties.getPort();
            if (portFromFile == null) {
                return DEFAULT_PORT;
            }
            return portFromFile;
        }
    }

}
