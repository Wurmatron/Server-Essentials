package com.wurmcraft.bot.json;

public class ServerMatcher {

  public Match[] servers;

  public ServerMatcher(Match[] servers) {
    this.servers = servers;
  }

  public class Match {
    public String serverID;
    public long rankID;
    public String panelID;

    public Match(String serverID, long rankID, String panelID) {
      this.serverID = serverID;
      this.rankID = rankID;
      this.panelID = panelID;
    }
  }

}
