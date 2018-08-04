package com.wurmcraft.serveressentials.common.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;

public class Local {

  // Teleport
  public String TPA_SENT;
  public String TPA_Recive;
  public String TPA_ACCEPT;
  public String TPA_DENY;
  public String TP_HOME;
  public String HOME_DELETED;
  public String TP_BACK;
  public String TP_BACK_FAIL;
  public String TP;
  public String TP_HERE;
  public String TELEPORT_TOP;
  public String HOME_NOTSET;
  public String HOME_HOVER;
  public String HOME_CREATED;
  public String HOME_FAILED;
  public String TP_HOME_OTHER;
  public String TPA_NOTSAFE;

  // Permissions
  public String PERM_ADDED;
  public String PERM_DEL;
  public String PERK_ADDED;
  public String PERK_DEL;

  // Chat
  public String CHAT_SPACER;
  public String CHAT_RANK;
  public String CHAT_TEAM;
  public String CHAT_LANG;
  public String CHAT_LASTSEEN;
  public String PLAYER_ONLY;
  public String CHAT_NAME;
  public String CHAT_PREFIX;
  public String CHAT_SUFFIX;
  public String CHAT_INHERITANCE;
  public String CHAT_ACCEPT;
  public String CHAT_DENY;
  public String GLOBAL;
  public String LOCAL;
  public String CHAT_LEADER;
  public String CHANNEL_CHANGED;
  public String CHAT_SPAM;
  public String MUTED_OTHER;
  public String MUTED;
  public String UNMUTED_OTHER;
  public String UNMUTED;
  public String NICK;
  public String CHANNEL_LIST;
  public String HOME_LIST;
  public String AFK;
  public String NOTAFK;
  public String AFK_OTHER;
  public String NOTAFK_OTHER;
  public String SPAWN;
  public String UUID;
  public String USER_SYNC;
  public String CHAT_TIME;

  // Commands
  public String RANK_NULL;
  public String PLAYER_NOT_FOUND;
  public String PLAYER_RANK_CHANGED;
  public String INVALID_LANG;
  public String LANGUAGE_CHANGED;
  public String GOD_DISABLE;
  public String GOD_ENABBLE;
  public String TPLOCK_DISABLED;
  public String TPLOCK_ENABLED;
  public String INVALID_NUMBER;
  public String MODE_INVALID;
  public String MODE_CHANGED;
  public String MODE_CHANGED_OTHER;
  public String FLY_ENABLED;
  public String FLY_DISABLED;
  public String FLY_ENABLED_OTHER;
  public String FLY_DISABLED_OTHER;
  public String PLAYER_INVENTORY;
  public String UNFROZEN;
  public String FROZEN;
  public String UNFROZEN_OTHER;
  public String FROZEN_OTHER;
  public String RTP;
  public String COMMAND_FORCED;
  public String COMMAND_SENDER_FORCED;
  public String COMMAND_NOT_FOUND;
  public String SUN;
  public String FEED;
  public String FEED_OTHER;
  public String HEAL;
  public String HEAL_OTHER;
  public String SPEED_CHANGED;
  public String PING_RESPONSE;
  public String PLAYER_FILE_DELETE;
  public String PLAYER_FILE_DELETE_OTHER;

  // Claiming
  public String CLAIM_BREAK;
  public String CLAIM_PLACE;
  public String CLAIM_INTERACT;
  public String CLAIM_EXPLOSION;
  public String CHUNK_CLAIMED;
  public String CHUNK_ALREADY_CLAIMED;
  public String CLAIM_REMOVED;
  public String MISSING_CLAIM;

  // Eco
  public String PAYED;
  public String INVALID_HAND;
  public String SIGN_CREATED;
  public String PURCHASED;
  public String NO_MONEY;
  public String SOLD;
  public String NO_ITEM;
  public String CURRENCY_CONVERT;
  public String BUY_PERK;

  // Item
  public String SKULL;
  public String NAME_CHANGED;

  // Command Descriptions
  public String COMMAND_TOP;
  public String COMMAND_BROADCAST;
  public String COMMAND_CHANNEL;
  public String COMMAND_HOME;
  public String COMMAND_DELHOME;
  public String COMMAND_SETHOME;
  public String COMMAND_TPA;
  public String COMMAND_TPAACCEPT;
  public String COMMAND_TP;
  public String COMMAND_SEEN;
  public String COMMAND_HELP;
  public String COMMAND_TPLOCK;
  public String COMMAND_BACK;
  public String COMMAND_TPHERE;
  public String COMMAND_RTP;
  public String COMMAND_AFK;
  public String COMMAND_DISCORD;
  public String COMMAND_MODS;
  public String COMMAND_PERM;
  public String COMMAND_ONLINETIME;
  public String COMMAND_SPAWN;
  public String COMMAND_LIST;
  public String COMMAND_MOTD;

  // Gui
  public String GUI_PERK;

  public static Local load(String langKey) {
    boolean valid = false;
    for (String lang : ConfigHandler.supportedLanguages) {
      if (lang.equalsIgnoreCase(langKey)) {
        valid = true;
      }
    }
    if (!valid) {
      return null;
    }
    Gson gson =
        new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setPrettyPrinting()
            .create();
    File lang =
        new File(
            ConfigHandler.saveLocation
                + File.separator
                + "Language"
                + File.separator
                + langKey
                + ".lang");
    if (lang.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(lang));
        return gson.fromJson(reader, Local.class);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
