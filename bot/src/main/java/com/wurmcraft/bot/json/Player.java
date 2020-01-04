package com.wurmcraft.bot.json;

public class Player {
  public String uuid;
  
  public String rank;
  
  public String discord;
  
  public Bank bank;
  
  public Player(String uuid, String rank, String discord) {
    this.uuid = uuid;
    this.rank = rank;
    this.discord = discord;
  }
  
  public class Bank {
    public Player.Coin[] coin;
    
    public Bank(Player.Coin[] coin) {
      this.coin = coin;
    }
  }
  
  public class Coin {
    public String name;
    
    public double conversionRate;
    
    public Coin(String name, double conversionRate) {
      this.name = name;
      this.conversionRate = conversionRate;
    }
  }
}
