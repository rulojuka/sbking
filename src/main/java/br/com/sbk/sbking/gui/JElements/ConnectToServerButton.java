package br.com.sbk.sbking.gui.JElements;

@SuppressWarnings("serial")
public class ConnectToServerButton extends SBKingButton {

	private static final int BUTTON_WIDTH = 500;
	private static final int BUTTON_HEIGHT = 100;

	public ConnectToServerButton() {
		super();
		this.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		this.setText("Connect to server");
	}

}
