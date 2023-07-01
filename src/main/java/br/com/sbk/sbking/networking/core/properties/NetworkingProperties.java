package br.com.sbk.sbking.networking.core.properties;

public class NetworkingProperties {

    private final FileProperties fileProperties;
    private final SystemProperties systemProperties;
    private static final String DEFAULT_IP = "164.90.254.243"; // NOSONAR WONTFIX

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

}
