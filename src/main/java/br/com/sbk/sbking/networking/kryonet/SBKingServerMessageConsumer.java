package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.GameServerFromGameNameIdentifier;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessageWithIdentifier;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.CreateTableMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.PlayCardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.UndoMessage;
import br.com.sbk.sbking.networking.server.SBKingServer;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;

public class SBKingServerMessageConsumer implements Runnable {

  private SBKingServer sbkingServer;
  private BlockingQueue<SBKingMessageWithIdentifier> serverMessageQueue;

  public SBKingServerMessageConsumer(SBKingServer sbkingServer,
      BlockingQueue<SBKingMessageWithIdentifier> serverMessageQueue) {
    this.sbkingServer = sbkingServer;
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
    LOGGER.debug("Entered server --consume--");
    Object content = message.getContent();
    if (message instanceof SetNicknameMessage) {
      this.sbkingServer.setNickname(playerIdentifier, (String) content);
    } else if (message instanceof PlayCardMessage) {
      this.sbkingServer.play((Card) content, playerIdentifier);
    } else if (message instanceof MoveToSeatMessage) {
      this.sbkingServer.moveToSeat((Direction) content, playerIdentifier);
    } else if (message instanceof ChoosePositiveMessage) {
      this.sbkingServer.choosePositive(playerIdentifier);
    } else if (message instanceof ChooseNegativeMessage) {
      this.sbkingServer.chooseNegative(playerIdentifier);
    } else if (message instanceof ChooseGameModeOrStrainMessage) {
      this.sbkingServer.chooseGameModeOrStrain((String) content, playerIdentifier);
    } else if (message instanceof UndoMessage) {
      this.sbkingServer.undo(playerIdentifier);
    } else if (message instanceof CreateTableMessage) {
      Class<? extends GameServer> gameServerClass = GameServerFromGameNameIdentifier.identify((String) content);
      this.sbkingServer.createTable(gameServerClass);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
    }
  }

}
