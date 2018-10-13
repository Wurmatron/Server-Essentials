package com.wurmcraft.serveressentials.api.json.user.optional;

public class Currency {

  private String name;
  private double sell;
  private double buy;

  public Currency(String name, double sell, double buy) {
    this.name = name;
    this.sell = sell;
    this.buy = buy;
  }

  public String getName() {
    return name;
  }

  public double getSell() {
    return sell;
  }

  public double getBuy() {
    return buy;
  }
}
