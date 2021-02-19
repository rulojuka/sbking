package br.com.sbk.sbking.core;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable{

  private String name;

  public Player(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
