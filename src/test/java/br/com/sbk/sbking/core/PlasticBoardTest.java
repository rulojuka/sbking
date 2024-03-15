package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlasticBoardTest {

    @Test
    void getDealerFromBoardNumber_oneShouldReturnNorth() {
        BoardNumber boardNumberOne = new BoardNumber(1);

        assertEquals(Direction.NORTH, PlasticBoard.getDealerFromBoardNumber(boardNumberOne));
    }

    @Test
    void getDealerFromBoardNumber_twoShouldReturnEast() {
        BoardNumber boardNumberTwo = new BoardNumber(2);

        assertEquals(Direction.EAST, PlasticBoard.getDealerFromBoardNumber(boardNumberTwo));
    }

    @Test
    void getDealerFromBoardNumber_threeShouldReturnSouth() {
        BoardNumber boardNumberThree = new BoardNumber(3);

        assertEquals(Direction.SOUTH, PlasticBoard.getDealerFromBoardNumber(boardNumberThree));
    }

    @Test
    void getDealerFromBoardNumber_fourShouldReturnWest() {
        BoardNumber boardNumberFour = new BoardNumber(4);

        assertEquals(Direction.WEST, PlasticBoard.getDealerFromBoardNumber(boardNumberFour));
    }

    @Test
    void getDealerFromBoardNumber_shouldReturnClockwiseForIncreasingNumbers() {
        int currentNumber = 0;
        Direction currentDirection = Direction.WEST;

        for (int i = 1; i <= 64; i++) {
            currentNumber++;
            currentDirection = currentDirection.next();
            BoardNumber currentBoardNumber = new BoardNumber(currentNumber);
            assertEquals(currentDirection, PlasticBoard.getDealerFromBoardNumber(currentBoardNumber));
        }

    }

}
