package com.wurmcraft.serveressentials.api.json.user.optional;

public class Bank {

  public Coin[] currency;

  public Bank() {
    currency = new Coin[0];
  }

  public Bank(Coin[] currency) {
    this.currency = currency;
  }

  public double getCurrency(String name) {
    for (Coin coin : currency) {
      if (coin.name.equalsIgnoreCase(name)) {
        return coin.amount;
      }
    }
    return 0;
  }

  public Coin[] getCurrency() {
    return currency;
  }

  public void setCurrency(Coin[] currency) {
    this.currency = currency;
  }
}
