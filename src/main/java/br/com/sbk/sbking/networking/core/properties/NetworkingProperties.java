package br.com.sbk.sbking.networking.core.properties;

public class NetworkingProperties {

    private final FileProperties fileProperties;
    private final SystemProperties systemProperties;

    public NetworkingProperties(FileProperties fileProperties, SystemProperties systemProperties) {
        this.fileProperties = fileProperties;
        this.systemProperties = systemProperties;
    }

    public String getHost() {
        String hostFromSystem = this.systemProperties.getHost();
        if (hostFromSystem != null) {
            return hostFromSystem;
        } else {
            return this.fileProperties.getHost();
        }
    }

    public int getPort() {
        String portFromSystem = this.systemProperties.getPort();
        if (portFromSystem != null) {
            return Integer.parseInt(portFromSystem);
        } else {
            String port = this.fileProperties.getPort();
            return Integer.parseInt(port);
        }
    }

    public String getIP(String serverName) {
        return this.fileProperties.getIP(serverName);
    }

}
