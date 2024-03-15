package br.com.sbk.sbking.core;

/**
 *
 * LAW 2 does not specify a MAX_BOARD_NUMBER. We arbitrarily chose 256 but this number is usually 32.
 */
public class BoardNumber {

    private static final int MIN_BOARD_NUMBER = 1;
    private static final int MAX_BOARD_NUMBER = 256;

    private int number;

    public BoardNumber(int number) {
        if (number < MIN_BOARD_NUMBER || number > MAX_BOARD_NUMBER) {
            throw new IllegalArgumentException("Board number should be between " + MIN_BOARD_NUMBER + " and " + MAX_BOARD_NUMBER + " inclusive.");
        } else {
            this.number = number;
        }
    }

    public int getNumber() {
        return this.number;
    }

}
