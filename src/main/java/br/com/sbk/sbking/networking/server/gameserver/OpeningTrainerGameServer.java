package br.com.sbk.sbking.networking.server.gameserver;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.boarddealer.ShuffledBoardDealer;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DefaultBridgeOpenerFactory;
import br.com.sbk.sbking.core.game.OpeningTrainerGame;

public class OpeningTrainerGameServer extends GameServer {

  private OpeningTrainerGame openingTrainerGame;

  public OpeningTrainerGameServer() {
    this.openingTrainerGame = new OpeningTrainerGame(new ShuffledBoardDealer(),
        DefaultBridgeOpenerFactory.getBridgeOpener());
    this.game = openingTrainerGame;
  }

  @Override
  public void run() {
    while (!game.isFinished()) {
      this.game.dealNewBoard();

      this.sendInitializeDealAll();
      this.sendDealAll();

      this.dealHasChanged = true;
      while (!this.game.getCurrentDeal().isFinished()) {
        if (this.dealHasChanged) {
          LOGGER.info("Sending new 'round' of deals");
          this.sendDealAll();
          this.dealHasChanged = false;
        }
      }

      this.game.finishDeal();

      this.sbkingServer.sendFinishDealToTable(this.table);
      LOGGER.info("Deal finished!");
    }

    LOGGER.info("Game has ended.");
  }
}
