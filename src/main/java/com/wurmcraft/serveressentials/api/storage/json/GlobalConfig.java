package com.wurmcraft.serveressentials.api.storage.json;

import com.wurmcraft.serveressentials.api.storage.FileType;

public class GlobalConfig implements FileType {

  public String discordLink;
  public String webLink;

  public GlobalConfig() {}

  public GlobalConfig(String discordLink, String webLink) {
    this.discordLink = discordLink;
    this.webLink = webLink;
  }

  @Override
  public String getID() {
    return "Global";
  }
}
