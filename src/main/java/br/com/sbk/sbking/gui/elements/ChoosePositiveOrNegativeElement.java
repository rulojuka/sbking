package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_Y_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_X_CENTER;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_Y_CENTER;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class ChoosePositiveOrNegativeElement {

	private Container container;
	private Direction direction;
	private JRadioButton positiveButton;
	private JRadioButton negativeButton;
	private SBKingClient sbKingClient;

	public ChoosePositiveOrNegativeElement(Container container, Direction direction, SBKingClient sbKingClient) {
		this.container = container;
		this.direction = direction;
		this.sbKingClient = sbKingClient;
	}

	public void add() {
		addLabel();
		addRadioButtons();
	}

	private void addRadioButtons() {
		int initial_y = 50;
		int initial_x = 75;
		int width = 100;
		int height = 20;

		if (Direction.NORTH == direction) {
			initial_x = NORTH_X_CENTER;
			initial_y = NORTH_Y_CENTER;
		} else if (Direction.EAST == direction) {
			initial_x = EAST_X_CENTER;
			initial_y = EAST_Y_CENTER;
		} else if (Direction.SOUTH == direction) {
			initial_x = SOUTH_X_CENTER;
			initial_y = SOUTH_Y_CENTER + 100;
		} else {
			initial_x = WEST_X_CENTER;
			initial_y = WEST_Y_CENTER;
		}

		initial_y += 25;

		ButtonGroup bg = new ButtonGroup();
		int y = initial_y;

		positiveButton = new JRadioButton("Positive");
		positiveButton.setBounds(initial_x, y, width, height);
		container.add(positiveButton);
		bg.add(positiveButton);

		y += 20;

		negativeButton = new JRadioButton("Negative");
		negativeButton.setBounds(initial_x, y, width, height);
		container.add(negativeButton);

		bg.add(negativeButton);

		JButton selectPositiveOrNegativeButton = new JButton();

		selectPositiveOrNegativeButton.addActionListener(new PositiveOrNegativeSelectActionListener());
		selectPositiveOrNegativeButton.setBounds(initial_x + width + 10, initial_y, width, height * 2);
		selectPositiveOrNegativeButton.setText("Select");
		container.add(selectPositiveOrNegativeButton);

	}

	class PositiveOrNegativeSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {

			if (positiveButton.isSelected()) {
				sbKingClient.sendPositive();
			} else {
				sbKingClient.sendNegative();
			}

		}
	}

	private void addLabel() {
		JLabel waitingLabel = new JLabel("<html>Please choose:</html>");
		waitingLabel.setHorizontalAlignment(CENTER);
		int width = 300;
		int height = 15;
		int x;
		int y;
		if (Direction.NORTH == direction) {
			x = NORTH_X_CENTER;
			y = NORTH_Y_CENTER;
		} else if (Direction.EAST == direction) {
			x = EAST_X_CENTER;
			y = EAST_Y_CENTER;
		} else if (Direction.SOUTH == direction) {
			x = SOUTH_X_CENTER;
			y = SOUTH_Y_CENTER;
		} else {
			x = WEST_X_CENTER;
			y = WEST_Y_CENTER;
		}
		waitingLabel.setSize(width, height);
		waitingLabel.setLocation(x, y);
		// rulesetLabel.setForeground(RULESET_ELEMENT_COLOR);
		container.add(waitingLabel);
	}

}
