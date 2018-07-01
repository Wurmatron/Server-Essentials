package com.wurmcraft.serveressentials.common.reference;

public enum Keys {

  PLAYER_DATA("player-data"), WARP("warp"), KIT("kit"), TEAM("team"), RANK("rank"), AUTO_RANK(
      "auto-rank"),LOCAL_USER("local-user"),LOCAL_TEAM("local-team"), CHANNEL("channel"), LAST_MESSAGE("lastMessage"), SPY("spy"), AFK("afk"), VAULT(
      "vault"), TPA("tpa");

  private String name;

  Keys(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
