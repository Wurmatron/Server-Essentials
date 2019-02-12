package com.wurmcraft.serveressentials.api.json.rest;

public class ServerStatus {

  public String name;
  public String status;
  public double time;
  public String[] players;
  public double lastUpdate;

  public ServerStatus(
      String name, String status, double time, String[] players, double lastUpdate) {
    this.name = name;
    this.status = status;
    this.time = time;
    this.players = players;
    this.lastUpdate = lastUpdate;
  }
}
