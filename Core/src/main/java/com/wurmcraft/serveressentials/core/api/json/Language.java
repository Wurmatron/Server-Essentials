package com.wurmcraft.serveressentials.core.api.json;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;

// <MODULE>_<COMMAND>_<SUB_COMMAND>
public class Language implements StoredDataType {

  // General
  public String langKey;

  // Error
  public String ERROR_NO_PERMS;
  public String ERROR_PLAYER_NOT_FOUND;
  public String ERROR_INSUFFICENT_FUNDS;
  public String ERROR_MUTED;
  public String ERROR_RANK_NOT_FOUND;
  public String ERROR_NO_HELD_ITEM;

  // Core Module
  public String CORE_SE_VERSION;
  public String CORE_SE_STORAGE;
  public String CORE_SE_MODULES;
  public String CORE_SE_COMMANDS;

  // Language
  public String LANGUAGE_LANGUAGE_NO_FOUND;
  public String LANGUAGE_LANGUAGE_SET;
  public String LANGUAGE_MUTE_SENDER;
  public String LANGUAGE_MUTE_UNMUTE_SENDER;
  public String LANGUAGE_MUTE;
  public String LANGUAGE_MUTE_UNMUTE;
  public String LANGUAGE_MSG;
  public String LANGUAGE_REPLY;
  public String LANGUAGE_NICK;
  public String LANGUAGE_NICK_OTHER;

  // General
  public String GENERAL_TPA_SENT;
  public String GENERAL_TPA_REQUEST;
  public String GENERAL_TPACCEPT_ACCEPT;
  public String GENERAL_TPACCEPT_SEND;
  public String GENERAL_TPAACCEPT_NOT_FOUND;
  public String GENERAL_BACK_SENT;
  public String GENERAL_SETHOME_SET;
  public String GENERAL_SETHOME_MAX;
  public String GENERAL_SETHOME_INVALID;
  public String GENERAL_HOME_TELEPORT;
  public String GENERAL_HOME_NOT_FOUND;
  public String GENERAL_DELHOME_REMOVED;
  public String GENERAL_DELHOME_ALL;
  public String GENERAL_SUN;
  public String GENERAL_RAIN;
  public String GENERAL_GAMEMODE_NOT_FOUND;
  public String GENERAL_GAMEMODE_CHANGED;
  public String GENERAL_GAMEMODE_CHANGED_SENDER;
  public String GENERAL_KICKALL_MSG;
  public String GENERAL_SPEED_SET;
  public String GENERAL_TPALL;
  public String GENERAL_PING;
  public String GENERAL_FEED;
  public String GENERAL_FEED_OTHER;
  public String GENERAL_LASTSEEN;
  public String GENERAL_HEAL;
  public String GENERAL_HEAL_OTHER;
  public String GENERAL_SPAWN;
  public String GENERAL_SPAWN_SET;
  public String GENERAL_FREEZE_OTHER;
  public String GENERAL_FREEZE_UNDO_OTHER;
  public String GENERAL_FREEZE;
  public String GENERAL_FREEZE_UNDO;
  public String GENERAL_VANISH;
  public String GENERAL_VANISH_UNVANISH;
  public String GENERAL_HAT;
  public String GENERAL_SKULL;
  public String GENERAL_RENAME;
  public String GENERAL_GOD;
  public String GENERAL_GOD_UNDO;
  public String GENERAL_GOD_OTHER;
  public String GENERAL_GOD_OTHER_UNDO;
  public String GENERAL_FLY;
  public String GENERAL_FLY_UNDO;
  public String GENERAL_FLY_OTHER;
  public String GENERAL_FLY_OTHER_UNDO;
  public String GENERAL_TPAHERE_REQUEST;
  public String GENERAL_TPAHERE;
  public String GENERAL_WARP_CREATE;
  public String GENERAL_WARP;
  public String GENERAL_WARP_NOT_FOUND;
  public String GENERAL_RULES_NONE;
  public String GENERAL_SUDO_OTHER;
  public String GENERAL_TOP;
  public String GENERAL_TOP_OTHER;
  public String GENERAL_TPHERE_OTHER;
  public String GENERAL_TPHERE;
  public String GENERAL_JUMP_INVALID;
  public String GENERAL_JUMP;
  public String GENERAL_SMITE;
  public String GENERAL_BURN_OTHER;
  public String GENREAL_TP_PLAYER_CORDS_OTHER;
  public String GENERAL_TP_CORDS;
  public String GENERAL_TP;
  public String GENERAL_TP_OTHER;
  public String GENERAL_TPOFFLINE;

  // Discord
  public String DISCORD_VERIFIED;
  public String DISCORD_INVALID;

  // Economy
  public String ECO_PAY_OTHER;
  public String ECO_PAY_EARN;
  public String ECO_PERK_COST;
  public String ECO_PERK_BUY;
  public String ECO_PERK_MAX_LEVEL;

  // Chunk Loading
  public String CHUNKLOADING_SET;
  public String CHUNKLOADING_DEL;
  public String CHUNKLOADING_BALANCE;

  public Language(String langKey) {
    this.langKey = langKey;
  }

  @Override
  public String getID() {
    return langKey;
  }
}
