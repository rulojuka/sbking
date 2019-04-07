package br.com.sbk.sbking.networking;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Dealer;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;

public class GameServer {
	final static Logger logger = Logger.getLogger(GameServer.class);
	public static void main(String[] args) throws Exception {
		
		NetworkGame game;
		Dealer dealer = new Dealer(Direction.NORTH);
		ExecutorService pool = Executors.newFixedThreadPool(4);
		game = new NetworkGame(pool, dealer.deal(new NegativeTricksRuleset()));

		try (ServerSocket listener = new ServerSocket(60000)) {
			logger.info("Game Server is Running...");
			for (Direction direction : Direction.values()) {
				game.connectPlayer(listener.accept(), direction);
			}

			logger.info("Sleeping for 500ms waiting for last client to setup itself");
			Thread.sleep(500);


			logger.info("All players connected, passing control to game");
			game.run();
			logger.info("Game has ended. Exiting main thread.");
		}
	}
}
