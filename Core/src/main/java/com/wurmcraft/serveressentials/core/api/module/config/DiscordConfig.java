package com.wurmcraft.serveressentials.core.api.module.config;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "Discord")
public class DiscordConfig implements StoredDataType {

  public String verifiedRank;

  public DiscordConfig() {
    this.verifiedRank = "Member";
  }

  public DiscordConfig(String verifiedRank) {
    this.verifiedRank = verifiedRank;
  }

  @Override
  public String getID() {
    return "Discord";
  }
}
