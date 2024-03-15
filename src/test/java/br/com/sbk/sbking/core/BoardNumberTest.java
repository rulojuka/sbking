package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BoardNumberTest {

    @Test
    void shouldWorkForAllNumbersBetweenMinAndMaxInclusive() {
        int min = 1;
        int max = 256;
        for (int i = min; i <= max; i++) {
            assertEquals(i, new BoardNumber(i).getNumber());
        }
    }

    @Test
    void shouldThrowExceptionWhenOutsideRange() {
        int[] outsideRangeExamples = { 0, -10, 1243, 500, -3 };
        for (int i : outsideRangeExamples) {
            assertThrows(IllegalArgumentException.class, () -> {
                new BoardNumber(i).getNumber();
            });
        }
    }

}
