package com.wurmcraft.serveressentials.api.json.user.optional;

public class Coin {

  private String name;
  private double amount;

  public Coin(String name, double amount) {
    this.name = name.replaceAll(" ", "_");
    this.amount = amount;
  }

  public Coin() {
    name = "";
    amount = 0;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
