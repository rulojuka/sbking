package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PunitiveCallTest {

    static final PunitiveCall _double = new PunitiveCall("X");
    static final PunitiveCall redouble = new PunitiveCall("XX");

    @Test
    void punitiveCallIsACall() {
        assertTrue(_double instanceof Call);
    }

    @Test
    void doubleShouldBeAPunitiveCall() {
        assertNotNull(_double);
        assertTrue(_double.isDouble());
    }

    @Test
    void redoubleShouldBeAPunitiveCall() {
        assertNotNull(redouble);
        assertTrue(redouble.isRedouble());
    }

    @Test
    void punitiveCallShouldBeEitherDoubleOrRedoubleButNotBoth() {
        assertTrue(redouble.isDouble() ^ redouble.isRedouble());
        assertTrue(_double.isDouble() ^ _double.isRedouble());
    }

}
