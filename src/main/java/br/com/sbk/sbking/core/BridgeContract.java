package br.com.sbk.sbking.core;

import java.util.Map;
import static java.util.Map.entry;

public class BridgeContract {
  private final int level;
  private final Strain strain;
  private final boolean doubled;
  private final boolean redoubled;
  private final boolean pass;
  private static Map<Strain, Integer> gameLevel = Map.ofEntries(entry(Strain.CLUBS, 5), entry(Strain.DIAMONDS, 5),
      entry(Strain.HEARTS, 4), entry(Strain.SPADES, 4), entry(Strain.NOTRUMPS, 3));

  public BridgeContract(int level, Strain strain, boolean doubled, boolean redoubled) {
    if(level==0){
      this.pass = true;
      this.strain = null;
      this.level = 0;
      this.doubled = false;
      this.redoubled = false;
    } else {
      this.validateArguments(level, strain, doubled, redoubled);
      this.pass=false;
      this.strain = strain;
      this.level = level;
      this.doubled = doubled;
      this.redoubled = redoubled;
    }
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

  public boolean isPass() {
    return this.pass == true;
  }

  @Override
  public String toString() {
    if(this.isPass()){
      return "PASS";
    }
    StringBuilder response = new StringBuilder();
    response.append(this.getLevel());
    response.append(this.getStrain().getSymbol());
    if(this.getDoubled()){
      response.append("X");
    }
    if(this.getRedoubled()){
      response.append("XX");
    }
    return response.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (this.getClass() != obj.getClass()) {
        return false;
    }
    BridgeContract other = (BridgeContract) obj;
    if (this.isPass()==true && other.isPass()==true){
      return true;
    }

    if (this.getLevel() == other.getLevel() &&
        this.getStrain().equals(other.getStrain()) &&
        this.getDoubled() == other.getDoubled() &&
        this.getRedoubled() == other.getRedoubled() &&
        this.isPass() == other.isPass()
    ) {
        return true;
    }
    return false;
}

}
