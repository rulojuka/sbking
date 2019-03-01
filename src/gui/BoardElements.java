package gui;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import core.Board;

public class BoardElements {
	
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	private final int NORTH_X = 1024 / 2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private final int SOUTH_X = NORTH_X;
	private final int EAST_X = NORTH_X + 300;
	private final int WEST_X = NORTH_X - 300;
	private final int NORTH_Y = 120;
	private final int SOUTH_Y = 470;
	private final int EAST_Y = 300;
	private final int WEST_Y = EAST_Y;
	
	private HandElement northHand;
	private HandElement eastHand;
	private HandElement southHand;
	private HandElement westHand;
	private List<CardButton> currentTrick;
	private CurrentPlayerButton currentPlayer;
	private ScoreboardButton scoreboard;
	
	public BoardElements(Board board, Container container, ActionListener actionListener) {
		this.northHand = new HandElement(board.getNorthHand(), container, actionListener, new Point(NORTH_X,NORTH_Y));
		this.eastHand = new HandElement(board.getEastHand(), container, actionListener, new Point(EAST_X,EAST_Y));
		this.southHand = new HandElement(board.getSouthHand(), container, actionListener, new Point(SOUTH_X,SOUTH_Y));
		this.westHand = new HandElement(board.getWestHand(), container, actionListener, new Point(WEST_X,WEST_Y));
	}

}
