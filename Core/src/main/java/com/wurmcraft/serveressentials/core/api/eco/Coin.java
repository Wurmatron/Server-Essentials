package com.wurmcraft.serveressentials.core.api.eco;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class Coin implements JsonParser {

  public String name;
  public double amount;

  public Coin(String name, double amount) {
    this.name = name;
    this.amount = amount;
  }
}
