package com.wurmcraft.serveressentials.api.json.user.optional;

public class Share {

  private String name;
  private long amount;

  public Share(String name, long amount) {
    this.name = name;
    this.amount = amount;
  }

  public String getName() {
    return name;
  }

  public long getAmount() {
    return amount;
  }
}
