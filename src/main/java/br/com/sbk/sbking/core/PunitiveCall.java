package br.com.sbk.sbking.core;

public final class PunitiveCall extends Call {

    private final boolean ddouble;

    public PunitiveCall(String label) {
        this.ddouble = "X".equals(label);
    }

    public boolean isDouble() {
        return this.ddouble;
    }

    public boolean isRedouble() {
        return !this.ddouble;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PunitiveCall)) {
            return false;
        }
        PunitiveCall other = (PunitiveCall) obj;
        return this.ddouble == other.isDouble();
    }

    @Override
    public int hashCode() {
        int randomInt1 = 0b10011000000000101101010110100000;
        int randomInt2 = 0b10110010010000011111000011010101;
        if (this.ddouble) {
            return randomInt1;
        } else {
            return randomInt2;
        }
    }

}
