package com.wurmcraft.serveressentials.forge.modules.matterlink;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;
import java.util.*;

@ConfigModule(moduleName = "MatterLink")
public class MatterLinkConfig  implements StoredDataType {

  public String url;
  public String gateway;
  public String protocol;
  public String account;
  public String token;
  public boolean displayLoginLogoutMessages;
  public String dataCollectionType;

  public MatterLinkConfig() {
    this.url = "https://matterlink.xxxx.com:4200/api/";
    this.gateway = "chat";
    this.protocol = "mc";
    this.account = "minecraft";
    this.token = "";
    displayLoginLogoutMessages= false;
    this.dataCollectionType = "stream";
  }

  public MatterLinkConfig(String url, String gateway, String protocol,
      String account, String token, boolean displayLoginLogoutMessages,
      String dataCollectionType) {
    this.url = url;
    this.gateway = gateway;
    this.protocol = protocol;
    this.account = account;
    this.token = token;
    this.displayLoginLogoutMessages = displayLoginLogoutMessages;
    this.dataCollectionType = dataCollectionType;
  }

  @Override
  public String getID() {
    return "MatterLink";
  }
}
