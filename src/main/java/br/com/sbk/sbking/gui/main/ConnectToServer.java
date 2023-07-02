package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.gui.listeners.ClientActionListener;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.rest.RestHTTPClient;

public final class ConnectToServer {

    private ConnectToServer() {
        throw new IllegalStateException("Utility class");
    }

    public static String getIPAddress() {
        NetworkingProperties networkingProperties = new NetworkingProperties(new FileProperties(),
                new SystemProperties());
        String hostname = networkingProperties.getHost();
        if (isValidIP(hostname)) {
            return hostname;
        } else {
            throw new InvalidIpException();
        }
    }

    public static SBKingClient connectToServer() {
        RestHTTPClient restHTTPClient = new RestHTTPClient(getIPAddress());
        return createWithRestHttpClient(restHTTPClient);
    }

    private static boolean isValidIP(String ipAddr) {
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }

    private static SBKingClient createWithRestHttpClient(RestHTTPClient restHTTPClient) {
        SBKingClient sbKingClient = new SBKingClient();
        sbKingClient.setRestHTTPClient(restHTTPClient);
        sbKingClient
                .setActionListener(
                        new ClientActionListener(restHTTPClient));
        LOGGER.info("Trying to connect.");
        try {
            UUID playerUUID = restHTTPClient.connect();
            String playerIdentifier = playerUUID.toString();
            sbKingClient.initializeId(playerIdentifier);
            LOGGER.info("Connected with identifier: {}.", playerIdentifier);
        } catch (Exception e) {
            LOGGER.fatal("Could not connect to server");
            LOGGER.fatal(e);
            System.exit(ErrorCodes.COULD_NOT_CONNECT_TO_SERVER);
        }
        return sbKingClient;
    }

}
