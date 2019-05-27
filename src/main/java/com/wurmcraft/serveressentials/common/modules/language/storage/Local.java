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
  // Language
  public String DESCRIPTION_MUTE;
  public String LANG_MUTE;
  public String LANG_UNMUTE;
  public String LANG_MUTE_SENDER;
  public String LANG_MUTE_NOTIFY;
  public String LANG_UNMUTE_SENDER;
  public String DESCRIPTION_BROADCAST;
  public String DESCRIPTION_CHANNEL;
  public String LANG_CHANNEL_CHANGED;
  public String LANG_CHANNEL_INVALID;
  public String LANG_CHANNEL_ADDFILTER;

  public Local(String key) {
    this.key = key;
  }

  @Override
  public String getID() {
    return key;
  }
}
