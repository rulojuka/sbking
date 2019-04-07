package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;

import static br.com.sbk.sbking.gui.constants.FrameConstants.*;

import javax.swing.JLabel;

public class WaitingForPlayersLabelElement {
	
	public static void add(Container container) {
		JLabel rulesetLabel = new JLabel("Waiting for players...");
		rulesetLabel.setHorizontalAlignment(CENTER);
		int width = 300;
		int height = 15;
		rulesetLabel.setSize(width, height);
		rulesetLabel.setLocation(TABLE_WIDTH/2 - width/2, TABLE_HEIGHT/2 - height/2);
		//rulesetLabel.setForeground(RULESET_ELEMENT_COLOR);
		container.add(rulesetLabel);
	}

}
