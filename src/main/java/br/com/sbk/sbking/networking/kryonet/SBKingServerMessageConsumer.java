package br.com.sbk.sbking.networking.kryonet;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessageWithIdentifier;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.AcceptClaimMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.ClaimMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTableSpectatorsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTablesMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.RejectClaimMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.UndoMessage;
import br.com.sbk.sbking.networking.server.SBKingServer;

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
    LOGGER.trace("Entered server --consume--");
    Object content = message.getContent();
    if (message instanceof SetNicknameMessage) {
      this.sbkingServer.setNickname(playerIdentifier, (String) content);
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
    } else if (message instanceof GetTablesMessage) {
      this.sbkingServer.sendTablesTo(playerIdentifier);
    } else if (message instanceof ClaimMessage) {
      this.sbkingServer.claim(playerIdentifier);
    } else if (message instanceof AcceptClaimMessage) {
      this.sbkingServer.acceptClaim(playerIdentifier);
    } else if (message instanceof RejectClaimMessage) {
      this.sbkingServer.rejectClaim(playerIdentifier);
    } else if (message instanceof GetTableSpectatorsMessage) {
      this.sbkingServer.sendSpectatorsTo(playerIdentifier);
    } else {
      LOGGER.error("Could not understand message.");
      LOGGER.error(message);
      LOGGER.error(content);
    }

  }

}
