package br.com.sbk.sbking.core;

import java.util.EnumMap;
import java.util.Map;

public class BridgeContract {
  private final int level;
  private final Strain strain;
  private final boolean doubled;
  private final boolean redoubled;
  private static Map<Strain, Integer> gameLevel;

  static {
    gameLevel = new EnumMap<Strain, Integer>(Strain.class);
    gameLevel.put(Strain.CLUBS, 5);
    gameLevel.put(Strain.DIAMONDS, 5);
    gameLevel.put(Strain.HEARTS, 4);
    gameLevel.put(Strain.SPADES, 4);
    gameLevel.put(Strain.NOTRUMPS, 3);
  }

  public BridgeContract(int level, Strain strain, boolean doubled, boolean redoubled) {
    this.validateArguments(level, strain, doubled, redoubled);
    this.strain = strain;
    this.level = level;
    this.doubled = doubled;
    this.redoubled = redoubled;
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

}
