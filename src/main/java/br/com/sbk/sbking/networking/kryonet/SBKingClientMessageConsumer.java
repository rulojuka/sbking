package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.concurrent.BlockingQueue;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.BoardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.DealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.FinishDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.IsNotSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.IsSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.TextMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.YourDirectionIsMessage;

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

  private void consume(SBKingMessage message) {
    LOGGER.debug("Entered --onMessage--");
    Object content = message.getContent();
    if (message instanceof TextMessage) {
      LOGGER.info("Received message from server: " + content);
    } else if (message instanceof BoardMessage) {
      this.sbkingClient.setCurrentBoard((Board) content);
    } else if (message instanceof DealMessage) {
      this.sbkingClient.setCurrentDeal((Deal) content);
    } else if (message instanceof YourDirectionIsMessage) {
      this.sbkingClient.initializeDirection((Direction) content);
    } else if (message instanceof PositiveOrNegativeChooserMessage) {
      this.sbkingClient.setPositiveOrNegativeChooser((Direction) content);
    } else if (message instanceof PositiveOrNegativeMessage) {
      this.sbkingClient.setPositiveOrNegative((String) content);
    } else if (message instanceof GameModeOrStrainChooserMessage) {
      this.sbkingClient.setGameModeOrStrainChooser((Direction) content);
    } else if (message instanceof InitializeDealMessage) {
      this.sbkingClient.initializeDeal();
    } else if (message instanceof FinishDealMessage) {
      this.sbkingClient.finishDeal();
    } else if (message instanceof InvalidRulesetMessage) {
      this.sbkingClient.setRulesetValid(false);
    } else if (message instanceof ValidRulesetMessage) {
      this.sbkingClient.setRulesetValid(true);
    } else if (message instanceof IsSpectatorMessage) {
      this.sbkingClient.setSpectator(true);
    } else if (message instanceof IsNotSpectatorMessage) {
      this.sbkingClient.setSpectator(false);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
    }

  }

}
