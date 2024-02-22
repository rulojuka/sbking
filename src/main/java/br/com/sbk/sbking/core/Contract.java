package br.com.sbk.sbking.core;

public class Contract {
    private final int level;
    private final Strain strain;
    private final boolean doubled;
    private final boolean redoubled;
    private final boolean vulnerable;

    public Contract(int level, Strain strain, boolean doubled, boolean redoubled, boolean vulnerable) {
        this.validateArguments(level, strain, doubled, redoubled);
        this.strain = strain;
        this.level = level;
        this.doubled = doubled;
        this.redoubled = redoubled;
        this.vulnerable = vulnerable;
    }

    private void validateArguments(int level, Strain strain, boolean doubled, boolean redoubled) {
        if (level < 1 || level > 7) {
            throw new IllegalArgumentException("Level must be between 1 and 7, inclusive.");
        }
        if (strain == null) {
            throw new IllegalArgumentException("Strain must be a bridge strain.");
        }
        if ((doubled && redoubled)) {
            throw new IllegalArgumentException("A contract cannot be doubled *and* redoubled.");
        }
    }

    public int getLevel() {
        return level;
    }

    public Strain getStrain() {
        return strain;
    }

    public boolean getDoubled() {
        return this.doubled;
    }

    public boolean getRedoubled() {
        return this.redoubled;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    @Override
    public String toString() {
        String response = this.getLevel() + this.getStrain().getSymbol();
        if (this.getDoubled()) {
            response += "X";
        } else if (this.getRedoubled()) {
            response += "XX";
        }
        return response;
    }

}
