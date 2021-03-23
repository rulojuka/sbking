package br.com.sbk.sbking.gui.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameScoreboardTest {

    @Test
    public void getLineShouldReturnFormattedString() {
        KingGameScoreboard gameScoreboard = new KingGameScoreboard();
        String firstLine = gameScoreboard.getLine(1);
        assertEquals("Negative tricks      ---- --- --", firstLine);
    }

}
