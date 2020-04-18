package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.json.JsonParser;
import com.wurmcraft.serveressentials.core.api.track.ServerTime;
import java.util.HashMap;

public class ServerPlayer implements JsonParser {

  // Core
  public long firstJoin;
  public long lastSeen;

  // General
  public Home[] homes;
  public Vault[] vaults;
  public LocationWrapper lastLocation;
  public long teleportTimer;
  public HashMap<String, Long> kitUsage;

  // Language
  public String channel;

  // Track
  public ServerTime playtime;
}
