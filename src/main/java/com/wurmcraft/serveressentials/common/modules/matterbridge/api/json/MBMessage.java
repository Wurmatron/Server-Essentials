package com.wurmcraft.serveressentials.common.modules.matterbridge.api.json;

import com.wurmcraft.serveressentials.common.modules.matterbridge.MatterBridgeModule;
import java.util.Date;
import java.util.Map;

public class MBMessage {

  public String avatar;
  public String event;
  public String gateway;
  public String text;
  public String username;
  public String account;
  public String channel;
  public String id;
  public String parent_id;
  public String protocol;
  public String timestamp;
  public String userid;
  public Map<String, String> Extra;

  public MBMessage(
      String avatar, String text, String username, String id, String parent_id, String userid) {
    this.avatar = avatar;
    this.event = "";
    this.gateway = MatterBridgeModule.config.gateway;
    this.text = text;
    this.username = username;
    this.account = MatterBridgeModule.config.account;
    this.id = id;
    this.parent_id = parent_id;
    this.protocol = MatterBridgeModule.config.protocol;
    Date date = new Date();
    this.timestamp = MatterBridgeModule.DATE_FORMAT.format(date);
    this.userid = userid;
    this.Extra = null;
  }
}
