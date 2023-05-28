package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessageWithIdentifier;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.TextMessage;

public class KryonetServerListenerFactory {

  private static final String CLIENT_FATAL_ERROR_MESSAGE = "Client should be a KryonetSBKingServer";
  private static final String CONNECTION_FATAL_ERROR_MESSAGE = "connection should be a ConnectionWithPlayer";

  public static Listener getServerListener(Server server,
      BlockingQueue<SBKingMessageWithIdentifier> serverMessageQueue) {
    return new Listener() {
      public void connected(Connection connection) {
        LOGGER.debug("Entered --connected-- lifecycle method.");

        KryonetSBKingServer kryonetSBKingServer = null;
        ConnectionWithIdentifier connectionWithPlayer = null;
        try {
          kryonetSBKingServer = (KryonetSBKingServer) server;
        } catch (Exception e) {
          LOGGER.fatal(CLIENT_FATAL_ERROR_MESSAGE);
          LOGGER.fatal(server);
          return;
        }
        try {
          connectionWithPlayer = (ConnectionWithIdentifier) connection;
        } catch (Exception e) {
          LOGGER.fatal(CONNECTION_FATAL_ERROR_MESSAGE);
          LOGGER.fatal(connection);
          return;
        }

        kryonetSBKingServer.addConnection(connectionWithPlayer);

        TextMessage response = new TextMessage("You just connected! Welcome to SBKing!");
        connection.sendTCP(response);

        TextMessage yourIdentifierIs = new TextMessage("Your identifier is: " + connectionWithPlayer.getIdentifier());
        connection.sendTCP(yourIdentifierIs);

        kryonetSBKingServer.sendYourTableIsTo(null, connectionWithPlayer.getIdentifier());
      }

      public void received(Connection connection, Object object) {
        KryonetSBKingServer kryonetSBKingServer = null;
        SBKingMessage message = null;
        ConnectionWithIdentifier connectionWithPlayer = null;

        if (object instanceof FrameworkMessage) {
          LOGGER.trace("Received Ping message.");
          return;
        }

        try {
          kryonetSBKingServer = (KryonetSBKingServer) server;
        } catch (Exception e) {
          LOGGER.fatal(CLIENT_FATAL_ERROR_MESSAGE);
          LOGGER.fatal(server);
          return;
        }

        try {
          message = (SBKingMessage) object;
        } catch (Exception e) {
          LOGGER.fatal("object should be a SBKingMessage");
          LOGGER.fatal(object);
          LOGGER.fatal(object instanceof FrameworkMessage);
          return;
        }

        try {
          connectionWithPlayer = (ConnectionWithIdentifier) connection;
        } catch (Exception e) {
          LOGGER.fatal(CONNECTION_FATAL_ERROR_MESSAGE);
          LOGGER.fatal(connection);
          return;
        }

        UUID identifier = connectionWithPlayer.getIdentifier();
        LOGGER.debug("Received " + message.getClass() + " message from UUID/Nickname: " + identifier + "/"
            + kryonetSBKingServer.getNicknameFromIdentifier(identifier));
        LOGGER.trace("Content is " + message.getContent() + ".");
        // This possibly needs to be in a Thread
        addToQueue(message, connectionWithPlayer);
      }

      private void addToQueue(SBKingMessage message, ConnectionWithIdentifier connectionWithIdentifier) {
        try {
          SBKingMessageWithIdentifier messageWithIdentifier = new SBKingMessageWithIdentifier(message,
              connectionWithIdentifier.getIdentifier());
          serverMessageQueue.add(messageWithIdentifier);
        } catch (IllegalStateException e) {
          LOGGER.error("Could not add message to the queue because of insuficcient space. Dropping message:");
          LOGGER.error(message);
          LOGGER.error(e);
        }
      }

      public void disconnected(Connection connection) {
        LOGGER.debug("Entered --disconnected-- lifecycle method.");

        KryonetSBKingServer kryonetSBKingServer = null;
        ConnectionWithIdentifier connectionWithPlayer = null;
        try {
          kryonetSBKingServer = (KryonetSBKingServer) server;
        } catch (Exception e) {
          LOGGER.fatal(CLIENT_FATAL_ERROR_MESSAGE);
          LOGGER.fatal(server);
          return;
        }
        try {
          connectionWithPlayer = (ConnectionWithIdentifier) connection;
        } catch (Exception e) {
          LOGGER.fatal(CONNECTION_FATAL_ERROR_MESSAGE);
          LOGGER.fatal(connection);
          return;
        }

        LOGGER.info("Lost connection to UUID: " + connectionWithPlayer.getIdentifier());
        kryonetSBKingServer.removeConnection(connectionWithPlayer);
      }
    };
  }

}
