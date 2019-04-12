package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_Y;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_Y;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_Y;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_Y;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.SBKingClient;

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
			initial_x = NORTH_X;
			initial_y = NORTH_Y;
		} else if (Direction.EAST == direction) {
			initial_x = EAST_X;
			initial_y = EAST_Y;
		} else if (Direction.SOUTH == direction) {
			initial_x = SOUTH_X;
			initial_y = SOUTH_Y + 100;
		} else {
			initial_x = WEST_X;
			initial_y = WEST_Y;
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
			x = NORTH_X;
			y = NORTH_Y;
		} else if (Direction.EAST == direction) {
			x = EAST_X;
			y = EAST_Y;
		} else if (Direction.SOUTH == direction) {
			x = SOUTH_X;
			y = SOUTH_Y;
		} else {
			x = WEST_X;
			y = WEST_Y;
		}
		waitingLabel.setSize(width, height);
		waitingLabel.setLocation(x, y);
		// rulesetLabel.setForeground(RULESET_ELEMENT_COLOR);
		container.add(waitingLabel);
	}

}
