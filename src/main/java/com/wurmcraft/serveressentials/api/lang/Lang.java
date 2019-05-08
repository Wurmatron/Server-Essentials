package com.wurmcraft.serveressentials.api.lang;

import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.storage.Local;

public class Lang implements FileType {

  private String key;
  public Local local;

  public Lang(String key) {
    this.key = key;
  }

  public Lang(String key, Local local) {
    this.key = key;
    this.local = local;
  }

  public Lang(String key, String json) {
    this.key = key;
    this.local = ServerEssentialsServer.instance.GSON.fromJson(json, Local.class);
  }

  @Override
  public String getID() {
    return key;
  }
}
