package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.esotericsoftware.kryonet.Listener;

import br.com.sbk.sbking.networking.kryonet.KryonetSBKingServer;
import br.com.sbk.sbking.networking.kryonet.KryonetServerFactory;
import br.com.sbk.sbking.networking.kryonet.KryonetServerListenerFactory;
import br.com.sbk.sbking.networking.kryonet.SBKingServerMessageConsumer;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessageWithIdentifier;

public class SBKingServerFactory {

  public static SBKingServer createWithKryonetConnection(int port) {
    SBKingServer sbKingServer = new SBKingServer();
    BlockingQueue<SBKingMessageWithIdentifier> serverMessageQueue = new LinkedBlockingQueue<SBKingMessageWithIdentifier>();
    KryonetSBKingServer server = KryonetServerFactory.getRegisteredServer(sbKingServer);

    Listener kryonetServerListener = KryonetServerListenerFactory.getServerListener(server, serverMessageQueue); // Producer
    SBKingServerMessageConsumer sbKingServerMessageConsumer = new SBKingServerMessageConsumer(sbKingServer,
        serverMessageQueue); // Consumer

    new Thread(sbKingServerMessageConsumer, "msg-consumer").start();
    server.addListener(kryonetServerListener);

    try {
      server.bind(port);
    } catch (IOException e1) {
      LOGGER.fatal("Could not bind port.");
    }
    server.start();

    // bindings
    sbKingServer.setKryonetSBKingServer(server);

    return sbKingServer;
  }

}
