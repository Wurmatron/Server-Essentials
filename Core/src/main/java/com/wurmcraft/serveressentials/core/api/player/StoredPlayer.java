package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;

public class StoredPlayer implements StoredDataType {

  public String uuid;
  public ServerPlayer server;
  public GlobalPlayer global;

  public StoredPlayer(String uuid, ServerPlayer server, GlobalPlayer global) {
    this.uuid = uuid;
    this.server = server;
    this.global = global;
  }

  public StoredPlayer(String uuid) {
    this.uuid = uuid;
    this.server = new ServerPlayer();
    this.global = new GlobalPlayer();
  }

  @Override
  public String getID() {
    return uuid;
  }
}
