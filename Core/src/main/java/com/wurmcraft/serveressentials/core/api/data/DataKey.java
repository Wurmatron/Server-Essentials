package com.wurmcraft.serveressentials.core.api.data;

public enum DataKey {
  PLAYER("Player-Data"),
  MODULE_CONFIG("Modules"),
  LANGUAGE("Language"),
  RANK("Rank"),
  TPA("TPA");

  private String name;

  DataKey(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
