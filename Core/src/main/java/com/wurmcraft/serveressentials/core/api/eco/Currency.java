package com.wurmcraft.serveressentials.core.api.eco;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class Currency implements JsonParser {

  public String name;
  public double amount;

  public Currency(String name, double amount) {
    this.name = name;
    this.amount = amount;
  }
}
