package br.com.sbk.sbking.gui.jelements;

@SuppressWarnings("serial")
public class ConnectToServerButton extends SBKingButton {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;

    public ConnectToServerButton() {
        super();
        this.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.setText("Play!");
    }

}
