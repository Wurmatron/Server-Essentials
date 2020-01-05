package com.wurmcraft.bot.json;

public class ServerRankMatcher {

  public Match[] servers;

  public ServerRankMatcher(Match[] servers) {
    this.servers = servers;
  }

  public class Match {
    public String serverID;
    public long rankID;

    public Match(String serverID, long rankID) {
      this.serverID = serverID;
      this.rankID = rankID;
    }
  }

}
