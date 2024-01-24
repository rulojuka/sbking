package br.com.sbk.sbking.core;

import java.util.UUID;

public class Player {

  private UUID identifier;
  private String nickname;

  public Player(UUID identifier, String nickname) {
    this.identifier = identifier;
    this.nickname = nickname;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String name) {
    if (name == null || name.isEmpty()) {
      this.nickname = RandomNameGenerator.getRandomName();
    } else {
      this.nickname = name;
    }
  }

  @Override
  public int hashCode() {
    return this.identifier.hashCode();
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
    Player other = (Player) obj;
    return this.identifier.equals(other.identifier);
  }

}
