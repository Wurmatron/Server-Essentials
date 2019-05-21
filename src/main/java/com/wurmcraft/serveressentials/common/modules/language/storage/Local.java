package com.wurmcraft.serveressentials.common.modules.language.storage;

import com.wurmcraft.serveressentials.api.storage.FileType;

public class Local implements FileType {

  // Language Key
  public String key;

  // Chat
  public String MUTED;
  public String SPAM;

  public Local(String key) {
    this.key = key;
  }

  @Override
  public String getID() {
    return key;
  }
}
