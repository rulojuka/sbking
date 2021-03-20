package br.com.sbk.sbking.core;

public enum ThreePlayersDirection {

    FOREHAND("Forehand", 'F'), MIDDLEHAND("Middlehand", 'M'), HINDHAND("Hindhand", 'H');

    private final String completeName;
    private final char abbreviation;

    ThreePlayersDirection(String completeName, char abbreviation) {
        this.completeName = completeName;
        this.abbreviation = abbreviation;
    }

    // Static copy to avoid many copies
    private static ThreePlayersDirection[] vals = values();

    public ThreePlayersDirection next(int n) {
        return vals[(this.ordinal() + n) % vals.length];
    }

    public ThreePlayersDirection next() {
        return this.next(1);
    }

    public boolean isForehand() {
        return this == ThreePlayersDirection.FOREHAND;
    }

    public boolean isMiddlehand() {
        return this == ThreePlayersDirection.MIDDLEHAND;
    }

    public boolean isHindhand() {
        return this == ThreePlayersDirection.HINDHAND;
    }

    public String getCompleteName() {
        return completeName;
    }

    public char getAbbreviation() {
        return abbreviation;
    }

}
