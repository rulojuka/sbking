package br.com.sbk.sbking.core;

public class PassingCall extends Call {

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof PassingCall);
    }

    @Override
    public int hashCode() {
        int randomInt = 0b10010101000010110011001001010101;
        return randomInt;
    }

}
