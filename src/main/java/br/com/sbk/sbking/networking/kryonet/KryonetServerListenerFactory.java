package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.TextMessage;

public class KryonetServerListenerFactory {

  public static Listener getServerListener(Server server) {
    return new Listener() {
      public void connected(Connection connection) {
        LOGGER.debug("Entered --connected-- lifecycle method.");

        KryonetSBKingServer kryonetSBKingServer = null;
        ConnectionWithIdentifier connectionWithPlayer = null;
        try {
          kryonetSBKingServer = (KryonetSBKingServer) server;
        } catch (Exception e) {
          LOGGER.fatal("Client should be a KryonetSBKingServer");
          LOGGER.fatal(server);
          return;
        }
        try {
          connectionWithPlayer = (ConnectionWithIdentifier) connection;
        } catch (Exception e) {
          LOGGER.fatal("connection should be a ConnectionWithPlayer");
          LOGGER.fatal(connection);
          return;
        }

        kryonetSBKingServer.addConnection(connectionWithPlayer);

        TextMessage response = new TextMessage("You just connected! Welcome to SBKing!");
        connection.sendTCP(response);
      }

      public void received(Connection connection, Object object) {
        LOGGER.info("Entered --received-- lifecycle method.");

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
          LOGGER.fatal("Client should be a KryonetSBKingServer");
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
          LOGGER.fatal("connection should be a ConnectionWithPlayer");
          LOGGER.fatal(connection);
          return;
        }

        UUID identifier = connectionWithPlayer.getIdentifier();
        LOGGER.debug("Received message from UUID: " + identifier);
        LOGGER.debug("Its nickname is: " + kryonetSBKingServer.getNicknameFromIdentifier(identifier));
        LOGGER.debug("Received " + message.getClass() + " message.");
        LOGGER.debug("Content is " + message.getContent() + ".");
        // This possibly needs to be in a Thread
        kryonetSBKingServer.onMessage(message, connectionWithPlayer);
      }

      public void disconnected(Connection connection) {
        LOGGER.debug("Entered --disconnected-- lifecycle method.");

        KryonetSBKingServer kryonetSBKingServer = null;
        ConnectionWithIdentifier connectionWithPlayer = null;
        try {
          kryonetSBKingServer = (KryonetSBKingServer) server;
        } catch (Exception e) {
          LOGGER.fatal("Client should be a KryonetSBKingServer");
          LOGGER.fatal(server);
          return;
        }
        try {
          connectionWithPlayer = (ConnectionWithIdentifier) connection;
        } catch (Exception e) {
          LOGGER.fatal("connection should be a ConnectionWithPlayer");
          LOGGER.fatal(connection);
          return;
        }

        kryonetSBKingServer.removeConnection(connectionWithPlayer);
      }
    };
  }

}
