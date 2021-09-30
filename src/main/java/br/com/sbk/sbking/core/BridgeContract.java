package br.com.sbk.sbking.core;

import static java.util.Map.entry;

import java.util.Map;

public class BridgeContract {
  private final int level;
  private final Strain strain;
  private final boolean doubled;
  private final boolean redoubled;
  private final boolean allPassed;
  private final Direction declarer;
  private static Map<Strain, Integer> gameLevel = Map.ofEntries(entry(Strain.CLUBS, 5), entry(Strain.DIAMONDS, 5),
      entry(Strain.HEARTS, 4), entry(Strain.SPADES, 4), entry(Strain.NOTRUMPS, 3));

  public BridgeContract(int level, Strain strain, boolean doubled, boolean redoubled, boolean allPassed,
      Direction declarer) {
    this.validateArguments(level, strain, doubled, redoubled);
    this.strain = strain;
    this.level = level;
    this.doubled = doubled;
    this.redoubled = redoubled;
    this.allPassed = allPassed;
    this.declarer = declarer;
  }

  private void validateArguments(int level, Strain strain, boolean doubled, boolean redoubled) {
    if (level < 1 || level > 7) {
      throw new IllegalArgumentException("Level must be between 1 and 7, inclusive.");
    }
    if (strain == null || gameLevel.get(strain) == null) {
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

  public boolean getAllPassed() {
    return this.allPassed;
  }

  public Direction getDeclarer() {
    return declarer;
  }

  public boolean isPartScore() {
    // This rule is wrong, it must take into account the doubles and redoubles
    return this.level < this.getGameLevelFor(this.strain);
  }

  private int getGameLevelFor(Strain strain) {
    return gameLevel.get(this.strain);
  }

  public boolean isGame() {
    return !this.isPartScore();
  }

  public boolean isPetitSlam() {
    return this.level == 6;
  }

  public boolean isGrandSlam() {
    return this.level == 7;
  }

  /* This method identifies any contract */
  public String getDescription() {
    if (this.allPassed) {
      return "PASS";
    } else {
      String response = String.valueOf(this.level) + this.strain.getShortName() + this.getDeclarer().getAbbreviation();
      if (this.doubled) {
        response += "X";
      } else if (this.redoubled) {
        response += "XX";
      }
      return response;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BridgeContract other = (BridgeContract) obj;
    return this.getDescription().equals(other.getDescription());
  }

  @Override
  public int hashCode() {
    return this.getDescription().hashCode();
  }

}
