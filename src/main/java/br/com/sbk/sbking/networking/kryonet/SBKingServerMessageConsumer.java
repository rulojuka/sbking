package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessageWithIdentifier;

public class SBKingServerMessageConsumer implements Runnable {

  private BlockingQueue<SBKingMessageWithIdentifier> serverMessageQueue;

  public SBKingServerMessageConsumer(BlockingQueue<SBKingMessageWithIdentifier> serverMessageQueue) {
    this.serverMessageQueue = serverMessageQueue;
  }

  @Override
  public void run() {
    try {
      while (true) {
        // BlockingQueue.take() blocks until a message is available.
        SBKingMessageWithIdentifier messageWithIdentifier = serverMessageQueue.take();
        this.consume(messageWithIdentifier.getMessage(), messageWithIdentifier.getIdentifier());
      }
    } catch (InterruptedException e) {
      LOGGER.fatal("Interrupted exception");
      LOGGER.fatal(e);
    }

  }

  private void consume(SBKingMessage message, UUID playerIdentifier) {
    LOGGER.trace("Entered server --consume--");
    Object content = message.getContent();
    LOGGER.error("Could not understand message.");
    LOGGER.error(message);
    LOGGER.error(content);
  }

}
