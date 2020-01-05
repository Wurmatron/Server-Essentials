package com.wurmcraft.bot.json;

public class ServerTime {

  private String serverID;
  private double onlineTime;
  private long lastSeen;
  private long firstJoin;
  private int loadedChunks;

  public ServerTime(
      String serverID, double onlineTime, long lastSeen, long firstJoin, int loadedChunks) {
    this.serverID = serverID;
    this.onlineTime = onlineTime;
    this.lastSeen = lastSeen;
    this.firstJoin = firstJoin;
    this.loadedChunks = loadedChunks;
  }

  public String getServerID() {
    return serverID;
  }

  public double getOnlineTime() {
    return onlineTime;
  }

  public long getLastSeen() {
    return lastSeen;
  }

  public double getFirstJoin() {
    return firstJoin;
  }

  public int getLoadedChunks() {
    return loadedChunks;
  }

  public void setOnlineTime(double onlineTime) {
    this.onlineTime = onlineTime;
  }

  public void setLastSeen(long lastSeen) {
    this.lastSeen = lastSeen;
  }

  public void setFirstJoin(long firstJoin) {
    this.firstJoin = firstJoin;
  }

  public void setLoadedChunks(int loadedChunks) {
    this.loadedChunks = loadedChunks;
  }
}
