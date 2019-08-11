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
  public String RANK_NOT_FOUND;
  public String CHAT_UUID;
  public String CHAT_RANK;
  public String CHAT_MUTED;
  public String CHAT_RANK_PREFIX;
  public String CHAT_RANK_SUFFIX;
  public String CHAT_RANK_INHERITANCE;
  public String CHAT_LASTSEEN;
  public String CHAT_RANKUP;
  public String MUST_BE_POSITIVE;
  public String CHAT_INVENTORY_FULL;
  public String CHAT_ITEM_NOT_FOUND;
  public String CHAT_NO_PERMS;
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
  public String DESCRIPTION_PERM;
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
  public String DESCRIPTION_SPEED;
  public String GENERAL_SPEED;
  public String GENERAL_SPEED_OTHER;
  public String DESCRIPTION_DELETEPLAYERFILE;
  public String GENERAL_DELETE_PLAYER_FILE;
  public String GENERAL_DPF_DELETED;
  public String DESCRIPTION_FLY;
  public String GENERAL_FLY_ENABLED;
  public String GENERAL_FLY_DISABLED;
  public String GENERAL_FLY_OTHER_ENABLED;
  public String GENERAL_FLY_OTHER_DISABLED;
  public String DESCRIPTION_GAMEMODE;
  public String GENERAL_GM_INVALID;
  public String GENERAL_GM_CHANGED;
  public String GENERAL_GM_CHANGED_OTHER;
  public String DESCRIPTION_RULES;
  public String GENERAL_RULES_SET;
  public String DESCRIPTION_FREEZE;
  public String GENERAL_FROZEN_UNDO;
  public String GENERAL_FROZEN_UNDO_OTHER;
  public String GENERAL_FROZEN;
  public String GENERAL_FROZEN_OTHER;
  public String DESCRIPTION_INVSEE;
  public String DESCRIPTION_GOD;
  public String GENERAL_GOD_ENABLED;
  public String GENERAL_GOD_DISABLED;
  public String GENERAL_GOD_ENABLED_OTHER;
  public String GENERAL_GOD_DISABLED_OTHER;
  public String DESCRIPTION_SETSPAWN;
  public String GENERAL_SPAWN_SET;
  public String DESCRIPTION_RELOAD_GLOBAL;
  public String GENERAL_RELOAD_GLOBAL;
  public String DESCRIPTION_LANGUAGE;
  public String LANG_RELOAD;
  public String LANG_LANG_SET;
  public String LANG_LANG_INVAID;
  public String DESCRIPTION_ONLINETIME;
  public String REST_OT_GLOBAL;
  public String REST_OT_LOCAL;
  public String REST_RANK_CHANGED_OTHER;
  public String REST_RANK_CHANGED;
  public String PERM_UPDATED_OTHER;
  public String PERM_UPDATED;
  public String PERK_UPDATED_OTHER;
  public String PERK_UPDATED;
  public String RANK_CREATED;
  public String DESCRIPTION_SEEN;
  public String DESCRIPTION_SAY;
  public String DESCRIPTION_LIST;
  public String DESCRIPTION_FEED;
  public String GENERAL_FEED;
  public String GENERAL_FEED_OTHER;
  public String DESCRIPTION_HELP;
  public String DESCRIPTION_GLOBALMOTD;
  public String GENERAL_GLOBALMOTD_SET;
  public String DESCRIPTION_VANISH;
  public String GENERAL_UNVANISH;
  public String GENERAL_VANISH;
  public String GENERAL_UNVANISH_OTHER;
  public String GENERAL_VANISH_OTHER;
  public String DESCRIPTION_CHUNKLOADING;
  public String DESCRIPTION_KIT;
  public String GENERAL_KIT_INVALID;
  public String GENERAL_KIT_GIVEN;
  public String GENERAL_KIT_TIMER;
  public String GENERAL_KIT_INVALID_NAME;
  public String GENERAL_KIT_CREATED;
  public String DESCRIPTION_CONVERT;
  // Teleportation
  public String DESCRIPTION_JUMP;
  public String TELEPORT_JUMP;
  public String DESCRIPTION_TOP;
  public String TELEPORT_TOP;
  public String DESCRIPTION_SPAWN;
  public String TELEPORT_SPAWN;
  public String DESCRIPTION_SETWARP;
  public String TELEPORT_SET_WARP;
  public String DESCRIPTION_WARP;
  public String TELEPORT_WARP;
  public String TELEPORT_WARP_NOT_FOUND;
  public String DESCRIPTION_TPA;
  public String TELEPORT_TPA_SENT;
  public String TELEPORT_REQUEST;
  public String DESCRIPTION_TPACCEPT;
  public String TELEPORT_TPA_ACCEPT;
  public String TELEPORT_TPA_ACCEPT_REQUEST;
  public String TELEPORT_TPA_NONE;
  public String DESCRIPTION_TPADENY;
  public String TELEPORT_TPA_DENY;
  public String DESCRIPTION_TPHERE;
  public String TELEPORT_TPHERE;
  public String TELEPORT_FORCED;
  public String DESCRIPTION_TPLOCK;
  public String TELEPORT_TOGGLE_ENABLE;
  public String TELEPORT_TOGGLE_DISABLE;
  public String DESCRIPTION_SETHOME;
  public String TELEPORT_HOME_SET;
  public String TELEPORT_HOME_MAX;
  public String DESCRIPTION_HOME;
  public String TELEPORT_HOME_NOT_FOUND;
  public String TELEPORT_HOME;
  public String DESCRIPTION_DELHOME;
  public String TELEPORT_DELHOME;
  public String DESCRIPTION_BACK;
  public String TELEPORT_BACK;
  public String DESCRIPTION_TP;
  public String TELEPORT_OTHER;
  public String TELEPORT_WARP_LIST;
  public String TELEPORT_FAILED;
  // Economy
  public String DESCRIPTION_BALANCE;
  public String DESCRIPTION_PAY;
  public String ECO_PAY_SENT;
  public String ECO_PAY_EARN;
  public String DESCRIPTION_PERK;
  public String ECO_PERK_INVALID;
  public String ECO_NEED;
  public String DESCRIPTION_BANK;
  public String ECO_INVALID_CURRENCY;
  public String ECO_EXCHANGED;
  public String ECO_SIGN_INVALID;
  public String ECO_SIGN_CREATED;
  public String ECO_REWARD_CREATED;
  public String ECO_REWARD_INVALID;
  public String ECO_REWARD_REMOVED;
  public String ECO_REWARD_POINTS_EMPTY;
  public String ECO_REWARD_GIVE;
  public String DESCRIPTION_REWARD;

  // Security
  public String DESCRIPTION_MODS;
  public String SECURITY_TRUSTED;
  public String SECURITY_ALT;
  public String SECURITY_LOCKDOWN;
  public String DESCRIPTION_LOCKDOWN;
  public String SECURITY_LOCKDOWN_DISABLED;
  public String SECURITY_LOCKDOWN_ENABLED;

  // Transfer
  public String DESCRIPTION_TRANSFER;
  public String TRANSFER_EMPTY;
  public String TRANSFER_TRANSFERRED;
  public String TRANSFER_SENT;

  // Discord
  public String DESCRIPTION_VERIFY;
  public String DISCORD_INVALID_TOKEN;
  public String DISCORD_SYNCED;

  public Local(String key) {
    this.key = key;
  }

  @Override
  public String getID() {
    return key;
  }
}
