package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.Collection;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;

public class MessageSender {

    private Collection<ClientGameSocket> playerSockets;

    public MessageSender() {
        this.playerSockets = new ArrayList<ClientGameSocket>();
    }

    public void addClientGameSocket(ClientGameSocket clientGameSocket) {
        this.playerSockets.add(clientGameSocket);
    }

    public void sendDealAll(Deal deal) {
        LOGGER.info("Sending everyone the current deal");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendDeal(deal);
        }
        LOGGER.info("Finished sending deals.");
    }

    public void sendDealOne(Deal deal, ClientGameSocket clientGameSocket) {
        LOGGER.info("Sending one player the current deal");
        clientGameSocket.sendDeal(deal);
        LOGGER.info("Finished sending deal.");
    }

    public void sendBoardAll(Board board) {
        LOGGER.info("Sending everyone the current board");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendBoard(board);
        }
        LOGGER.info("Finished sending boards.");
    }

    public void sendMessageAll(String message) {
        LOGGER.info("Sending everyone the following message: --" + message + "--");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendMessage(message);
        }
        LOGGER.info("Finished sending messages.");
    }

    public void sendChooserPositiveNegativeAll(Direction chooser) {
        LOGGER.info("Sending everyone the chooser of Positive or Negative: --" + chooser + "--");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendChooserPositiveNegative(chooser);
        }
        LOGGER.info("Finished sending messages.");
    }

    public void sendChooserGameModeOrStrainAll(Direction chooser) {
        LOGGER.info("Sending everyone the chooser of GameMode or Strain: --" + chooser + "--");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendChooserGameModeOrStrain(chooser);
        }
        LOGGER.info("Finished sending messages.");
    }

    public void sendPositiveOrNegativeAll(PositiveOrNegative positiveOrNegative) {
        String message = positiveOrNegative.toString().toUpperCase();
        LOGGER.info("Sending everyone : --" + message + "--");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendPositiveOrNegative(message);
        }
        LOGGER.info("Finished sending messages.");
    }

    public void sendGameModeOrStrainShortDescriptionAll(String currentGameModeOrStrain) {
        String message = currentGameModeOrStrain;
        LOGGER.info("Sending everyone : --" + message + "--");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendGameModeOrStrain(message);
        }
        LOGGER.info("Finished sending messages.");
    }

    public void sendInitializeDealAll() {
        LOGGER.info("Sending everyone Initialize Deal control");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendInitializeDeal();
        }
        LOGGER.info("Finished sending controls.");
    }

    public void sendFinishDealAll() {
        LOGGER.info("Sending everyone Finish deal control");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendFinishDeal();
        }
        LOGGER.info("Finished sending controls.");
    }

    public void sendGameScoreboardAll(KingGameScoreboard gameScoreboard) {
        LOGGER.info("Sending everyone the Game Scoreboard");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendGameScoreboard(gameScoreboard);
        }
        LOGGER.info("Finished sending Game Scoreboards.");
    }

    public void sendFinishGameAll() {
        LOGGER.info("Sending everyone Finish Game control");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendFinishGame();
        }
        LOGGER.info("Finished sending controls.");
    }

    public void sendInvalidRulesetAll() {
        LOGGER.info("Sending everyone Invalid ruleset control");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendInvalidRuleset();
        }
        LOGGER.info("Finished sending controls.");
    }

    public void sendValidRulesetAll() {
        LOGGER.info("Sending everyone Valid ruleset control");
        for (ClientGameSocket playerSocket : playerSockets) {
            playerSocket.sendValidRuleset();
        }
        LOGGER.info("Finished sending controls.");
    }

}
