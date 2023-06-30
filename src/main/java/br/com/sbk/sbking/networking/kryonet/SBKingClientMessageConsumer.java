package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.concurrent.BlockingQueue;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourIdIsMessage;

public class SBKingClientMessageConsumer implements Runnable {

  private SBKingClient sbkingClient;
  private BlockingQueue<SBKingMessage> clientMessageQueue;

  public SBKingClientMessageConsumer(SBKingClient sbkingClient, BlockingQueue<SBKingMessage> clientMessageQueue) {
    this.sbkingClient = sbkingClient;
    this.clientMessageQueue = clientMessageQueue;
  }

  @Override
  public void run() {
    try {
      while (true) {
        // BlockingQueue.take() blocks until a message is available.
        SBKingMessage message = clientMessageQueue.take();
        this.consume(message);
      }
    } catch (InterruptedException e) {
      LOGGER.fatal("Interrupted exception");
      LOGGER.fatal(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void consume(SBKingMessage message) {
    LOGGER.trace("Entered --onMessage--");
    Object content = message.getContent();
    if (message instanceof GameModeOrStrainChooserMessage) {
      this.sbkingClient.setGameModeOrStrainChooser((Direction) content);
    } else if (message instanceof YourIdIsMessage) {
      this.sbkingClient.initializeId((String) content);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
      LOGGER.error(content);
    }

  }

}
