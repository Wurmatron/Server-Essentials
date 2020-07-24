package com.wurmcraft.serveressentials.core.api.module.config;

public class CommandCost {

  public String commandName;
  public double cost;

  public CommandCost(String commandName, double cost) {
    this.commandName = commandName;
    this.cost = cost;
  }
}
