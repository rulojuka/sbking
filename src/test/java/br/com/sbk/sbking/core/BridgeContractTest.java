package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BridgeContractTest {

    private static Strain anyStrain = Strain.DIAMONDS;
    private static int anyLevel = 3;
    private static boolean doubled = true;
    private static boolean redoubled = false;
    private static boolean vulnerable = false;

    private Contract setup() {
        return new Contract(anyLevel, anyStrain, doubled, redoubled, vulnerable);
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidLowerLevel() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(0, anyStrain, false, false, vulnerable);
        });

    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidUpperLevel() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(8, anyStrain, false, false, vulnerable);
        });
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithInvalidStrain() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(7, null, false, false, vulnerable);
        });
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionWhenConstructedWithDoubledAndRedoubled() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(7, null, true, true, vulnerable);
        });
    }

    @Test
    public void shouldGetItsLevel() {
        Contract subject = this.setup();
        assertEquals(anyLevel, subject.getLevel());
    }

    @Test
    public void shouldGetItsStrain() {
        Contract subject = this.setup();
        assertEquals(anyStrain, subject.getStrain());
    }

    @Test
    public void shouldGetItsDoubled() {
        Contract subject = this.setup();
        assertEquals(doubled, subject.getDoubled());
    }

    @Test
    public void shouldGetItsRedoubled() {
        Contract subject = this.setup();
        assertEquals(redoubled, subject.getRedoubled());
    }

}
