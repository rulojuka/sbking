package br.com.sbk.sbking.networking.core.properties;

public class NetworkingProperties {

    private final FileProperties fileProperties;
    private final SystemProperties systemProperties;
    private static final int DEFAULT_PORT = 60000;
    private static final String DEFAULT_IP = "164.90.254.243";

    public NetworkingProperties(FileProperties fileProperties, SystemProperties systemProperties) {
        this.fileProperties = fileProperties;
        this.systemProperties = systemProperties;
    }

    public String getHost() {
        String hostFromSystem = null;
        String hostFromFile = null;
        if (this.systemProperties != null) {
            hostFromSystem = this.systemProperties.getHost();
        }
        if (hostFromSystem != null && !hostFromSystem.isEmpty()) {
            return hostFromSystem;
        } else {
            if (this.fileProperties != null) {
                hostFromFile = this.fileProperties.getHost();
            }
            if (hostFromFile != null && !hostFromFile.isEmpty()) {
                return hostFromFile;
            } else {
                return DEFAULT_IP;
            }
        }
    }

    public int getPort() {
        String portFromSystem = null;
        Integer portFromFile = null;
        if (this.systemProperties != null) {
            portFromSystem = this.systemProperties.getPort();
        }
        if (portFromSystem != null && !portFromSystem.isEmpty()) {
            return Integer.parseInt(portFromSystem);
        } else {
            if (this.fileProperties != null) {
                portFromFile = this.fileProperties.getPort();
            }
            if (portFromFile != null && portFromFile > 0) {
                return portFromFile;
            } else {
                return DEFAULT_PORT;
            }
        }
    }

}
