package br.com.sbk.sbking.clientapp;

import org.springframework.stereotype.Component;

import br.com.sbk.sbking.gui.main.MainNetworkGame;

@Component
public class ClientComponent {

    public ClientComponent() {
        MainNetworkGame game = new MainNetworkGame();
        game.run();
    }

}
