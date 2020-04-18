package com.wurmcraft.serveressentials.core.api.track;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class NetworkTime implements JsonParser {

  public ServerTime[] serverPlaytimes;

  public NetworkTime(ServerTime[] serverPlaytimes) {
    this.serverPlaytimes = serverPlaytimes;
  }
}
