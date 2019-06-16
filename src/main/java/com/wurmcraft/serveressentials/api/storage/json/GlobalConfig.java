package com.wurmcraft.serveressentials.api.storage.json;

import com.wurmcraft.serveressentials.api.storage.FileType;

public class GlobalConfig implements FileType {

  public String discordLink;
  public String webLink;
  public String[] motd;
  public String[] rules;

  public GlobalConfig() {}

  public GlobalConfig(String discordLink, String webLink) {
    this.discordLink = discordLink;
    this.webLink = webLink;
    motd = new String[0];
  }

  @Override
  public String getID() {
    return "Global";
  }
}
