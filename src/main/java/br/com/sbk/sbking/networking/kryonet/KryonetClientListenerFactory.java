package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class KryonetClientListenerFactory {

  public static Listener getClientListener(Client client) {
    return new Listener() {
      public void connected(Connection connection) {
        LOGGER.debug("Entered --connected-- lifecycle method.");
      }

      public void received(Connection connection, Object object) {
        LOGGER.debug("Entered --received-- lifecycle method.");

        KryonetSBKingClient kryonetSBKingClient = null;
        SBKingMessage message = null;

        if (object instanceof FrameworkMessage) {
          LOGGER.trace("Received Ping message.");
          return;
        }

        try {
          kryonetSBKingClient = (KryonetSBKingClient) client;
        } catch (Exception e) {
          LOGGER.fatal("Client should be a KryonetSBKingClient");
          LOGGER.fatal(client);
          return;
        }

        try {
          message = (SBKingMessage) object;
        } catch (Exception e) {
          LOGGER.fatal("object should be a SBKingMessage");
          LOGGER.fatal(object);
          return;
        }

        LOGGER.debug("Received " + message.getClass() + " message.");
        LOGGER.debug("Content is " + message.getContent() + ".");
        // This possibly needs to be in a Thread
        kryonetSBKingClient.onMessage(message);
      }

      public void disconnected(Connection connection) {
        LOGGER.debug("Entered --disconnected-- lifecycle method.");
      }
    };
  }
}
