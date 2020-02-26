package com.wurmcraft.bot.json;

public class ServerMatcher {

  public Match[] servers;

  public ServerMatcher(Match[] servers) {
    this.servers = servers;
  }

  public class Match {
    public String serverID;
    public String panelID;

    public Match(String serverID, String panelID) {
      this.serverID = serverID;
      this.panelID = panelID;
    }
  }
}
