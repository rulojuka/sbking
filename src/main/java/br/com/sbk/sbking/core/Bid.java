package br.com.sbk.sbking.core;

public class Bid extends Call implements Comparable<Bid> {

    private final OddTricks oddTricks;
    private final Strain strain;

    public Bid(OddTricks oddTricks, Strain strain) {
        this.oddTricks = oddTricks;
        this.strain = strain;
    }

    public OddTricks getOddTricks() {
        return oddTricks;
    }

    public Strain getStrain() {
        return strain;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Bid)) {
            return false;
        }
        Bid other = (Bid) obj;
        return this.oddTricks == other.getOddTricks()
                && this.strain == other.getStrain();
    }

    @Override
    public int hashCode() {
        String differentiator = oddTricks.getName() + strain.getName();
        return differentiator.hashCode();
    }

    /**
     * From the Laws of Bridge 2017:
     * A bid supersedes a previous bid if it designates
     * either the same number of odd tricks in a higher-ranking denomination
     * or a greater number of odd tricks in any denomination.
     */
    @Override
    public int compareTo(Bid o) {
        int oddTricksComparation = this.getOddTricks().compareTo(o.getOddTricks());
        if (oddTricksComparation != 0) {
            return oddTricksComparation;
        }
        return this.getStrain().compareTo(o.getStrain());
    }

}
