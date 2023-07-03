package br.com.sbk.sbking.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.PavlicekNumber;
import br.com.sbk.sbking.core.RandomUtils;

public class BoardDAOTest {

    private Board board;
    private BoardEntity boardEntity;
    private BigInteger bigIntegerPavlicekNumber;
    private PavlicekNumber generator;

    private BoardDAO boardDAO;

    @BeforeEach
    public void setup() {
        RandomUtils randomUtils = new RandomUtils();
        int randomNumber = randomUtils.nextInt(10000);
        this.bigIntegerPavlicekNumber = BigInteger.ZERO.add(BigInteger.valueOf(randomNumber));
        this.generator = new PavlicekNumber();
        this.board = generator.getBoardFromNumber(bigIntegerPavlicekNumber);
        this.boardEntity = new BoardEntity(UUID.randomUUID());
        this.boardEntity.setBoard(this.board);
    }

    @Test
    public void testEverything() {
        this.boardDAO = new BoardDAO();
        this.boardDAO.saveBoard(boardEntity);
        BoardEntity boardFromDB = this.boardDAO.findByPavlicekNumber(this.bigIntegerPavlicekNumber.toString());
        // System.out.println("Got boardEntity from db: " +
        // boardFromDB.getPavlicekNumber() + " " + boardFromDB.getId());
        String expectedNumber = boardEntity.getPavlicekNumber();
        String actualNumber = boardFromDB.getPavlicekNumber();
        assertEquals(expectedNumber, actualNumber);
    }
}
