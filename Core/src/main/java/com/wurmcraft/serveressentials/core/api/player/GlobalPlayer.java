package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;
import com.wurmcraft.serveressentials.core.api.track.NetworkTime;

public class GlobalPlayer implements JsonParser {

  // Core
  public long firstJoin;
  public long lastSeen;

  // Rank
  public String rank;
  public String[] extraPerms;
  public String[] perks;

  // Language
  public String language;
  private boolean muted;

  // Economy
  public Wallet wallet;

  // Track
  public NetworkTime playtime;

  // Discord
  public String discordID;

  // Vote
  public int rewardPoints;
}
