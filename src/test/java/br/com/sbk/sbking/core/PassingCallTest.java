package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PassingCallTest {

    static final PassingCall subject = new PassingCall();

    @Test
    void passingCallIsACall() {
        assertTrue(subject instanceof Call);
    }

}
