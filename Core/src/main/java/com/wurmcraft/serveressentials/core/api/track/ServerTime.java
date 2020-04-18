package com.wurmcraft.serveressentials.core.api.track;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class ServerTime implements JsonParser {

  public String serverID;
  public long time;

  public ServerTime(String serverID, long time) {
    this.serverID = serverID;
    this.time = time;
  }
}
