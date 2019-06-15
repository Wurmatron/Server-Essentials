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
  public String CHAT_INVALID_BOOLEAN;
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
  public String LANG_CHANNEL_DELFILTER;
  public String LANG_CHANNEL_RELOADED;
  public String LANG_CHANNEL_INVALID_NAME;
  public String LANG_CHANNEL_INVALID_TYPE;
  public String LANG_CHANNEL_CREATED;
  public String LANG_CHANNEL_DELETE;
  public String LANG_CHANNEL_NAME;
  public String LANG_CHANNEL_PREFIX;
  public String LANG_CHANNEL_TYPE;
  public String LANG_CHANNEL_FILTER;
  public String LANG_CHANNEL_LIST;
  public String DESCRIPTION_MESSAGE;
  public String DESCRIPTION_REPLY;
  public String DESCRIPTION_NICK;
  public String LANG_NICK_CHANGED;
  public String LANG_NICK_CHANGED_SENDER;
  // General
  public String GENERAL_DISCORD_SET;
  public String GENERAL_DISCORD_LINK;
  public String DESCRIPTION_DISCORD;
  public String GENERAL_WEBSITE_SET;
  public String GENERAL_WEBSITE_LINK;
  public String DESCRIPTION_WEBSITE;
  public String DESCRIPTION_MOTD;
  public String GENERAL_MOTD_SET;
  public String DESCRIPTION_PING;
  public String GENERAL_PING;
  public String DESCRIPTION_UUID;
  public String GENERAL_UUID;
  public String DESCRIPTION_SERVER;
  public String GENERAL_SERVER;
  public String DESCRIPTION_RAIN;
  public String GENERAL_RAIN;
  public String DESCRIPTION_SUN;
  public String GENERAL_SUN;
  public String DESCRIPTION_RENAME;
  public String GENERAL_RENAME;
  public String DESCRIPTION_SKULL;
  public String GENERAL_SKULL;
  public String DESCRIPTION_HAT;
  public String GENERAL_HAT;
  public String DESCRIPTION_HEAL;
  public String GENERAL_HEAL;
  public String GENERAL_HEAL_OTHER;
  public String DESCRIPTION_SMITE;
  public String GENERAL_SMITE;
  public String GENERAL_SMITE_OTHER;
  public String DESCRIPTION_SUDO;
  public String GENERAL_COMMAND_SUDO;

  public Local(String key) {
    this.key = key;
  }

  @Override
  public String getID() {
    return key;
  }
}
