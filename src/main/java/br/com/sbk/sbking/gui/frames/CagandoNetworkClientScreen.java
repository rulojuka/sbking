package br.com.sbk.sbking.gui.frames;

import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.gui.painters.DealPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;

@SuppressWarnings("serial")
public class CagandoNetworkClientScreen extends NetworkClientScreen {

	final static Logger logger = LogManager.getLogger(CagandoNetworkClientScreen.class);

	public CagandoNetworkClientScreen() {
		super();
	}

	@Override
	public void run() {
		logger.info("Starting to paint ConnectToServerScreen");
		paintConnectToServerScreen();
		logger.info("Finished painting ConnectToServerScreen");

		logger.info("Waiting for connectedToServer to be true");
		while (!connectedToServer) {
			sleepFor(100);
		}

		logger.info("Waiting for sbKingClient.isDirectionSet() to be true");
		while (!sbKingClient.isDirectionOrSpectatorSet()) {
			sleepFor(100);
		}

		while (true) {
			if (sbKingClient.isSpectator()) {
				if (sbKingClient.getBoardHasChanged() || sbKingClient.getDealHasChanged() || sbKingClient.getGUIHasChanged()) {
					if (!sbKingClient.getGUIHasChanged()) {
						logger.info("Deal has changed. Painting deal.");
						logger.info("It is a spectator.");
					}
					Deal currentDeal = sbKingClient.getDeal();
					Board currentBoard = sbKingClient.getCurrentBoard();
					if (currentDeal == null) {
						paintSpectatorScreen(currentBoard, sbKingClient.getPlayCardActionListener());
					} else {
						paintSpectatorScreen(currentDeal, sbKingClient.getPlayCardActionListener());
					}
				}
			} else {
				if (sbKingClient.getDealHasChanged() || sbKingClient.getGUIHasChanged()) {
					if (!sbKingClient.getGUIHasChanged()) {
						logger.info("Deal has changed. Painting deal.");
						logger.info("It is a player.");
					}
					Deal currentDeal = sbKingClient.getDeal();

					logger.info("Starting to paint Deal");
					paintDeal(currentDeal, sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
					logger.info("Finished painting Deal");
				}

			}
			sleepFor(300);
		}
	}

	private void sleepFor(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			logger.debug(e);
		}
	}

	private void paintPainter(Painter painter) {
		this.getContentPane().removeAll();
		painter.paint(this.getContentPane());
		if (sbKingClient != null) {
			sbKingClient.setGUIHasChanged(false);
		}
	}

	private void paintConnectToServerScreen() {
		Painter connectToServerPainter = new ConnectToServerPainter(this);
		paintPainter(connectToServerPainter);
	}

	private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
		Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
		paintPainter(dealPainter);
	}

	private void paintSpectatorScreen(Deal deal, ActionListener playCardActionListener) {
		if (deal == null) {
			logger.error("Deal should not be null here.");
		} else {
			Painter spectatorPainter = new SpectatorPainter(playCardActionListener, deal);
			paintPainter(spectatorPainter);
		}
	}

	private void paintSpectatorScreen(Board board, ActionListener playCardActionListener) {
		if (board == null) {
			logger.error("Board should not be null here.");
		} else {
			Painter spectatorPainter = new SpectatorPainter(playCardActionListener, board);
			paintPainter(spectatorPainter);
		}
	}

}
