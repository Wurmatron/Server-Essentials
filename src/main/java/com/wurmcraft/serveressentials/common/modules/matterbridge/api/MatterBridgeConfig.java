package com.wurmcraft.serveressentials.common.modules.matterbridge.api;

import com.wurmcraft.serveressentials.api.storage.FileType;

public class MatterBridgeConfig implements FileType {

  public String url;
  public String gateway;
  public String account;
  public String channel;
  public String protocol;

  public MatterBridgeConfig(
      String url, String gateway, String account, String channel, String protocol) {
    this.url = url;
    this.gateway = gateway;
    this.account = account;
    this.channel = channel;
    this.protocol = protocol;
  }

  public MatterBridgeConfig() {
    this.url = "";
    this.gateway = "";
    this.account = "";
    this.channel = "";
    this.protocol = "";
  }

  @Override
  public String getID() {
    return "MatterBridge";
  }
}
