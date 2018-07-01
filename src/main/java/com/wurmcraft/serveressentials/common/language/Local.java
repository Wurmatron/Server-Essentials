package com.wurmcraft.serveressentials.common.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;

public class Local {

  // Home
  public String HOME_TELEPORTED;
  public String HOME_NONE;
  public String HOME_NONEXISTENT;
  public String HOME_INVALID;
  public String HOME_REPLACED;
  public String HOME_SET;
  public String HOME_MAX;
  public String HOME_FAILED;
  public String HOME_DELETED;
  public String HOME_ERROR_DELETION;
  // Warp
  public String WARP_NAME;
  public String WARP_CREATED;
  public String WARP_NONE;
  public String WARP_TELEPORT;
  public String WARP_DELETE;
  public String WARPS_NONE;
  // Global
  public String SPAWN_SET;
  public String SPAWN_TELEPORTED;
  public String COMMAND_FORCED;
  public String COMMAND_SENDER_FORCED;
  public String LAST_SEEN;
  public String COMMAND_NOT_FOUND;
  public String NO_RULES;
  public String PAGE_NONE;
  public String RULE_CREATED;
  public String RULE_REMOVED;
  public String RULE_INVALID_INDEX;
  public String MOTD_CREATED;
  public String MOTD_REMOVED;
  public String MOTD_INVALID_INDEX;
  public String NO_MOTD;
  public String TOP;
  public String SKULL;
  public String PLAYER_ONLY;
  public String AFK_NOW;
  public String AFK_OFF;
  public String PING_REPLY;
  public String PREGEN_NOTIFY;
  public String PREGEN_FINISHED;
  public String PREGEN_WARN;
  public String PREGEN_STARTED;
  public String PREGEN;
  public String PREGEN_STOP;
  public String LOCKDOWN;
  public String LOCKDOWN_ENABLED;
  public String LOCKDOWN_DISABLED;
  public String BANNED_MOD;
  public String RANK_RELOAD;
  public String RANK_NOT_FOUND;
  // Player
  public String PLAYER_NOT_FOUND;
  public String PLAYER_INVENTORY;
  public String PLAYER_INVENTORY_ENDER;
  public String MODE_INVALID;
  public String MODE_CHANGED;
  public String MODE_CHANGED_OTHER;
  public String DATA_RELOADED;
  public String DATA_RELOADED_OTHER;
  public String FROZEN;
  public String FROZEN_OTHER;
  public String UNFROZEN;
  public String UNFROZEN_OTHER;
  public String HEAL_SELF;
  public String HEAL_OTHER;
  public String HEAL_OTHER_SENDER;
  public String FLY_ENABLED;
  public String FLY_DISABLED;
  public String FLY_ENABLED_OTHER;
  public String FLY_DISABLED_OTHER;
  public String PLAYER_FILE_DELETE;
  public String PLAYER_FILE_DELETE_OTHER;
  public String MUTED;
  public String UNMUTED;
  public String MUTED_OTHER;
  public String UNMUTED_OTHER;
  public String NOTIFY_MUTED;
  public String NO_VAULTS;
  public String VAULT_NOT_FOUND;
  public String VAULT_CREATED;
  public String VAULT_MAX_HIT;
  public String VAULT_ITEMS;
  public String VAULT_DELETED;
  public String VAULT_NAME;
  public String SPEED_CHANGED;
  public String MESSAGE_SENT;
  public String MISSING_MESSAGE;
  public String MAIL_SENT;
  public String NO_MAIL;
  public String MAIL_REMOVED;
  public String HAS_MAIL;
  public String MAIL_INVALID;
  public String MAIL_REMOVED_ALL;
  public String INVALID_KIT_NAME;
  public String KIT_CREATED;
  public String KIT_NOTFOUND;
  public String KIT_REMOVED;
  public String NO_KITS;
  public String FULL_INV;
  public String KIT;
  public String SPY;
  public String SPY_OTHER;
  public String NICKNAME_OTHER;
  public String NICKNAME_SET;
  public String RANK_CHANGED;
  public String RANK_UP;
  public String RANK_UP_NOTIFY;
  public String NEXT_RANK;
  public String RANK_MAX;
  public String SPAM;
  public String NICK_NONE;
  public String PERK_CHANGED;
  public String ONLINE_TIME;
  public String EXPERIENCE;
  public String BALANCE;
  public String GOD_ENABLED;
  public String GOD_DISABLED;
  // Teleport
  public String TPA_USERNAME_NONE;
  public String TPA_USER_NOTFOUND;
  public String TPA_REQUEST_SENT;
  public String TPA_REQUEST;
  public String TPA_DENY;
  public String TPA_ACCEPED_OTHER;
  public String TPA_ACCEPTED;
  public String TELEPORT_COOLDOWN;
  public String TPA_NONE;
  public String TELEPORT_BACK;
  public String TELEPORTED;
  public String TELEPORT_TO;
  public String TELEPORTED_FROM;
  public String INVALID_HOME_NAME;
  public String NO_TPA;
  public String TPA_SELF;
  public String INVALID_LASTLOCATION;
  public String TPLOCK;
  public String RAND_TP;
  public String RTP_FAIL;
  // Team
  public String TEAM_CREATE_MISSING_NAME;
  public String TEAM_CREATED;
  public String TEAM_INVALID;
  public String TEAM_JOINED;
  public String TEAM_LEFT;
  public String TEAM_LEADER_PERM;
  public String TEAM_MISSING_NAME;
  public String TEAM_INVITED;
  public String TEAM_INVITED_OTHER;
  public String TEAM_KICKED;
  public String TEAM_KICKED_OTHER;
  public String TEAMADMIN_DISBAND;
  public String TEAM_NONE;
  public String TEAM_SET_VALUE;
  public String TEAM_INVAID_VALUE;
  // Protection
  public String CLAIM_BREAK;
  public String CLAIM_PLACE;
  public String CLAIM_INTERACT;
  public String CLAIM_EXPLOSION;
  public String CHUNK_CLAIMED;
  public String CHUNK_ALREADY_CLAIMED;
  public String CLAIM_REMOVED;
  public String MISSING_CLAIM;
  public String CLAIM_DISABLED;
  // Chat
  public String SPACER;
  public String CHANNEL_CHANGED;
  public String CHANNEL_PERMS;
  public String CHANNEL_INVALID;
  public String INVALID_NUMBER;
  public String WEBSITE;
  // Item
  public String NO_ITEM;
  public String NAME_CHANGED;
  public String ITEM_SENT;
  public String ITEM_NONE;
  // Eco
  public String CURRENT_MONEY;
  public String CURRENT_MONEY_OTHER;
  public String NEGATIVE_MONEY;
  public String MISSING_MONEY;
  public String MONEY_SENT;
  public String MONEY_SENT_RECEIVER;
  public String PURCHASE;
  public String MISSING_STACK;
  public String SELL_STACK;
  public String SIGN_INVALID;
  public String MONEY_NONE;
  public String PLAYER_INVENTORY_FULL;
  public String ITEM_SOLD;
  public String LINK_CHEST;
  public String SIGN_FIRST;
  public String LINKED;
  // Security
  public String SECURITY_CREATIVE_KICK;

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
