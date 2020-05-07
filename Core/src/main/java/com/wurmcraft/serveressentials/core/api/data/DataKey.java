package com.wurmcraft.serveressentials.core.api.data;

public enum DataKey {
  PLAYER("Player-Data"),
  MODULE_CONFIG("Modules"),
  LANGUAGE("Language"),
  RANK("Rank"),
  TPA("TPA"),
  CURRENCY("Economy"),
  WARP("Warp"),
  CHUNK_LOADING("ChunkLoading");

  private String name;

  DataKey(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
