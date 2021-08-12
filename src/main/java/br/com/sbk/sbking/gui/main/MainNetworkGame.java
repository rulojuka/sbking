package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.screens.GameScreen;
import br.com.sbk.sbking.gui.screens.LobbyScreen;
import br.com.sbk.sbking.gui.screens.SBKingScreen;
import br.com.sbk.sbking.gui.screens.WelcomeScreen;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.kryonet.messages.GameScreenFromGameNameIdentifier;

public class MainNetworkGame {

    public static void main(String[] args) {
        ClientApplicationState.startAppState();
        SBKingClientJFrame sbkingClientJFrame = new SBKingClientJFrame();

        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.runAt(sbkingClientJFrame);

        SBKingClient sbkingClient = welcomeScreen.getSBKingClient();
        SBKingScreen currentScreen;

        SpectatorNamesUpdater tableUpdater = new SpectatorNamesUpdater(sbkingClient);
        Thread tableUpdaterThread = new Thread(tableUpdater, "spectator-names-updater");
        tableUpdaterThread.start();

        while (true) {

            String gameName = sbkingClient.getGameName();
            if (gameName == null) {
                sbkingClient.setTables(null);
            }

            currentScreen = new LobbyScreen(sbkingClient);
            currentScreen.runAt(sbkingClientJFrame);

            LOGGER.info("Finished Lobby Screen, creating table screen");

            Class<? extends GameScreen> gameScreenClass = GameScreenFromGameNameIdentifier
                    .identify(sbkingClient.getGameName());
            GameScreen gameScreen;
            try {
                Class[] constructorArguments = new Class[1];
                constructorArguments[0] = SBKingClient.class;
                gameScreen = gameScreenClass.getDeclaredConstructor(constructorArguments).newInstance(sbkingClient);
                LOGGER.info("Created GameScreen:" + gameScreen.getClass());
                currentScreen = gameScreen;
                currentScreen.runAt(sbkingClientJFrame);
            } catch (Exception e) {
                LOGGER.fatal("Could not initialize GameScreen with received gameScreenClass.");
                LOGGER.fatal(e);
                System.exit(ErrorCodes.COULD_NOT_INITIALIZE_GAMESCREEN);
            }
        }
    }

}
