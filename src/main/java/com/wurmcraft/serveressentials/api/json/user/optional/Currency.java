package com.wurmcraft.serveressentials.api.json.user.optional;

public class Currency {

  public String name;
  public double sell;
  public double buy;

  public Currency(String name, double sell, double buy) {
    this.name = name;
    this.sell = sell;
    this.buy = buy;
  }
}
