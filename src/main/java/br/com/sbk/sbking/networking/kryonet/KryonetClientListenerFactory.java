package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.concurrent.BlockingQueue;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class KryonetClientListenerFactory {

  public static Listener getClientListener(Client client, BlockingQueue<SBKingMessage> clientMessageQueue) {
    return new Listener() {
      public void connected(Connection connection) {
        LOGGER.debug("Entered --connected-- lifecycle method.");
      }

      public void received(Connection connection, Object object) {
        LOGGER.debug("Entered --received-- lifecycle method.");

        SBKingMessage message = null;

        if (object instanceof FrameworkMessage) {
          LOGGER.trace("Received Ping message.");
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
        addToQueue(message);
      }

      private void addToQueue(SBKingMessage message) {
        try {
          clientMessageQueue.add(message);
        } catch (IllegalStateException e) {
          LOGGER.error("Could not add message to the queue because of insuficcient space. Dropping message:");
          LOGGER.error(message);
          LOGGER.error(e);
        }
      }

      public void disconnected(Connection connection) {
        KryonetSBKingClient kryonetSBKingClient = null;
        LOGGER.debug("Entered --disconnected-- lifecycle method.");
        LOGGER.info("Lost connection to server. Trying to reconnect!");

        try {
          kryonetSBKingClient = (KryonetSBKingClient) client;
        } catch (Exception e) {
          LOGGER.fatal("client should be a KryonetSBKingClient");
          LOGGER.fatal(client);
          return;
        }

        // This runs in another Thread because kryonet throws IllegalStateException:
        // "Cannot connect on the connection's update thread." when run in the same
        // Thread
        ReconnectToServerRunner reconnectToServerRunner = new ReconnectToServerRunner(kryonetSBKingClient);
        new Thread(reconnectToServerRunner, "reconnect").start();
      }
    };
  }
}
