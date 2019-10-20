package com.wurmcraft.serveressentials.api.storage.json;

public class GlobalBan {

  public String name;
  public String uuid;
  public String ip;
  public String reason;

  public GlobalBan(String name, String uuid, String ip, String reason) {
    this.name = name;
    this.uuid = uuid;
    this.ip = ip;
    this.reason = reason;
  }
}
