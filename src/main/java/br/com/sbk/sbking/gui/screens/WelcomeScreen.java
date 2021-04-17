package br.com.sbk.sbking.gui.screens;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.client.SBKingClientFactory;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;

public class WelcomeScreen implements SBKingScreen {

  private NetworkingProperties networkingProperties;
  private boolean connectedToServer = false;
  private SBKingClient sbkingClient;

  public WelcomeScreen() {
    super();
    this.networkingProperties = new NetworkingProperties(new FileProperties(), new SystemProperties());
  }

  public void connectToServer(String nickname) {
    String hostname = this.networkingProperties.getHost();
    int port = this.networkingProperties.getPort();
    if (isValidIP(hostname)) {
      this.sbkingClient = SBKingClientFactory.createWithKryonetConnection(nickname, hostname, port);
      this.connectedToServer = true;
    } else {
      LOGGER.error("Invalid IP");
    }
  }

  public boolean isConnectedToServer() {
    return connectedToServer;
  }

  public SBKingClient getSBKingClient() {
    return sbkingClient;
  }

  private boolean isValidIP(String ipAddr) {
    Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
    Matcher mtch = ptn.matcher(ipAddr);
    return mtch.find();
  }

  @Override
  public void runAt(SBKingClientJFrame sbkingClientJFrame) {
    LOGGER.info("Starting to paint ConnectToServerScreen");
    sbkingClientJFrame.paintPainter(new ConnectToServerPainter(this));
    LOGGER.info("Finished painting ConnectToServerScreen");

    LOGGER.info("Waiting for connectedToServer to be true");
    while (!connectedToServer) {
      sleepFor(100);
    }
    LOGGER.info("connectedToServer is true. Leaving ConnectToNetworkScreen");
  }

  private void sleepFor(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      LOGGER.debug(e);
    }
  }

}
