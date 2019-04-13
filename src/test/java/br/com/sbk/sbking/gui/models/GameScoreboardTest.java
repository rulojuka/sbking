package br.com.sbk.sbking.gui.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameScoreboardTest {

	@Test
	public void getLineShouldReturnFormattedString() {
		GameScoreboard gameScoreboard = new GameScoreboard();
		String firstLine = gameScoreboard.getLine(1);
		assertEquals("Negative tricks      ---- --- --",firstLine);
	}

}
