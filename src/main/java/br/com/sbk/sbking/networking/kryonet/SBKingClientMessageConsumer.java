package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.DealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.FinishDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GetTableSpectatorsResponseMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GetTablesResponseMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.IsNotSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.IsSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.TextMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourDirectionIsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourTableIsMessage;

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
    if (message instanceof TextMessage) {
      LOGGER.info("Received message from server: " + content);
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
    } else if (message instanceof YourTableIsMessage) {
      this.sbkingClient.setGameName((String) content);
    } else if (message instanceof GetTablesResponseMessage) {
      this.sbkingClient.setTables((List<LobbyScreenTableDTO>) content);
    } else if (message instanceof GetTableSpectatorsResponseMessage) {
      this.sbkingClient.setSpectatorNames((List<String>) content);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
      LOGGER.error(content);
    }

  }

}
