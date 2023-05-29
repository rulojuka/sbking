package br.com.sbk.sbking.gui.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.client.SBKingClientFactory;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;

public class ConnectToServer {

    public static SBKingClient connectToServer() {
        NetworkingProperties networkingProperties = new NetworkingProperties(new FileProperties(),
                new SystemProperties());
        String hostname = networkingProperties.getHost();
        int port = networkingProperties.getPort();
        if (isValidIP(hostname)) {
            return SBKingClientFactory.createWithKryonetConnection(hostname, port);
        } else {
            throw new InvalidIpException();
        }
    }

    private static boolean isValidIP(String ipAddr) {
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }

}
