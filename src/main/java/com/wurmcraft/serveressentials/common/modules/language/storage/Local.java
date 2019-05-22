package com.wurmcraft.serveressentials.common.modules.language.storage;

import com.wurmcraft.serveressentials.api.storage.FileType;

public class Local implements FileType {

  // Language Key
  public String key;

  // Chat
  public String MUTED;
  public String SPAM;
  public String PLAYER_NOT_FOUND;
  public String CHAT_SPACER;
  public String CHAT_PLAYER;
  public String CHAT_TIME;
  public String CHAT_EXPERIENCE;
  public String CHAT_BALANCE;
  public String CHAT_INVALID_NUMBER;
  public String CHAT_INVALID_AUTORANK;
  // AutoRank
  public String AUTORANK_MAX_RANK;
  public String DESCRIPTION_AUTORANK;
  public String AUTORANK_CHECK;
  public String AUTORANK_CREATED;
  public String AUTORANK_DELETED;

  public Local(String key) {
    this.key = key;
  }

  @Override
  public String getID() {
    return key;
  }
}
