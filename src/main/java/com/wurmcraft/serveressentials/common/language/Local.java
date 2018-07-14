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
  public String TPA_SENT = "&9Tpa Request Sent!";
  public String TPA_Recive = "&b%PLAYER% &dhas sent you a TPA Request %ACCEPT%";
  public String TPA_ACCEPT = "&dTpa Request Accepted!";
  public String TPA_DENY = "&dTpa Request Denied!";
  public String TP_HOME = "&dYou have teleported to '&6%HOME%&d'";
  public String HOME_DELETED = "&dHome '%HOME%' has been deleted!";
  public String TP_BACK = "&dYou have teleported back to your previous location";
  public String TP_BACK_FAIL = "Failed to find your last known location!";
  public String TP = "&dYou have been teleported to %NAME%";
  public String TP_HERE = "&6%PLAYER%&d has been teleported to your current location.";
  public String TELEPORT_TOP = "&dYou have been teleported to the top!";
  public String HOME_NOTSET = "&dNo home has been set";
  public String HOME_HOVER = "&dX: &9%X%\n&dY: &9%Y%\n&dZ: &9%Z%\n&dD: &9%DIMENSION%";
  public String HOME_CREATED = "&dHome &6%Name% &dcreated";
  public String HOME_FAILED = "&dFailed to create home %HOME%";
  public String TP_HOME_OTHER = "'&6%FROM%&d' has been teleported to '&6%TO%&6'";

  // Permissions
  public String PERM_ADDED = "Permission '%PERM% added to '%PLAYER%'";
  public String PERM_DEL = "Permission '%PERM% deleted from '%PLAYER%'";
  public String PERK_ADDED = "Permission '%PERM% added to '%PLAYER%'";
  public String PERK_DEL = "Permission '%PERM% deleted from '%PLAYER%'";

  // Chat
  public String CHAT_SPACER =
      "&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=&e-&6=";
  public String CHAT_RANK = "Rank";
  public String CHAT_TEAM = "Team";
  public String CHAT_LANG = "Language";
  public String CHAT_LASTSEEN = "Seen";
  public String PLAYER_ONLY = "Players Only!";
  public String CHAT_NAME = "Name";
  public String CHAT_PREFIX = "Prefix";
  public String CHAT_SUFFIX = "Suffix";
  public String CHAT_INHERITANCE = "Inheritance";
  public String CHAT_ACCEPT = "&aAccept";
  public String CHAT_DENY = "&cDeny";
  public String GLOBAL = "Global";
  public String LOCAL = "Local";
  public String CHAT_LEADER = "Leader";
  public String CHANNEL_CHANGED = "&dYour channel has been changed to &6%CHANNEL%";
  public String CHAT_SPAM = "Dont Spam Chat!";
  public String MUTED_OTHER = "You have muted %PLAYER%";
  public String MUTED = "You have been muted!";
  public String UNMUTED_OTHER = "You have unmuted %PLAYER%";
  public String UNMUTED = "You have been umuted!";
  public String NICK = "Nick has been changeed to %NICK%";
  public String CHANNEL_LIST = "&6%TYPE% %PREFIX% %NAME%";
  public String HOME_LIST = "&d%HOME%";
  public String AFK = "&dYou are now AFK";
  public String NOTAFK = "&dYou are no longer AFK";
  public String AFK_OTHER = "&b%PLAYER% &dis now AFK";
  public String NOTAFK_OTHER = "&b%PLAYER% &dis no longer AFK";

  // Commands
  public String RANK_NULL = "Rank Does not Exist!";
  public String PLAYER_NOT_FOUND = "Player '%PLAYER%' not found!";
  public String PLAYER_RANK_CHANGED = "You have changed '%PLAYER%'s rank to '%RANK%'";
  public String INVALID_LANG = "Invalid Lang, %LANG%";
  public String LANGUAGE_CHANGED = "Language Changed to, '%LANG%'";
  public String GOD_DISABLE = "God Mode Disabled";
  public String GOD_ENABBLE = "God Mode Enabled";
  public String TPLOCK_DISABLED = "&dTP Lock Disabled";
  public String TPLOCK_ENABLED = "&dTP Lock Enabled";
  public String INVALID_NUMBER = "%NUMBER% is not a valid number!";
  public String MODE_INVALID = "Invalid Gamemode '%MODE%'";
  public String MODE_CHANGED = "You Gamemode has changed to %MODE%";
  public String MODE_CHANGED_OTHER = "You have changed %PLAYER% gamemode to %MODE%";
  public String FLY_ENABLED = "Flying Mode Enabled";
  public String FLY_DISABLED = "Flying Mode Disabled";
  public String FLY_ENABLED_OTHER = "Flying Mode Enabled for %PLAYER%";
  public String FLY_DISABLED_OTHER = "Flying Mode Disabled for %PLAYER%";
  public String PLAYER_INVENTORY = "Opening %PLAYER%'s Inventory";
  public String UNFROZEN = "You have been unfrozen";
  public String FROZEN = "You have been frozen";
  public String UNFROZEN_OTHER = "You have unfrozen %PLAYER%";
  public String FROZEN_OTHER = "You have frozen %PLAYER%";
  public String RTP = "&dYou have Randomly Teleported!";
  public String COMMAND_FORCED = "You were forced to run %COMMAND%";
  public String COMMAND_SENDER_FORCED = "You forced %PLAYER% to run the command %COMMAND%";
  public String COMMAND_NOT_FOUND = "Command %COMMAND% not found!";
  public String SUN = "Weather changed to sunny";
  public String FEED = "You have been feed";
  public String FEED_OTHER = "You just feed %PLAYER%";
  public String HEAL = "You have just been healed";
  public String HEAL_OTHER = "You have just healed %PLAYER%";
  public String SPEED_CHANGED = "Your speed has changed to %SPEED%";
  public String PING_RESPONSE = "Nep NepNep Nep Nep Nep";
  public String PLAYER_FILE_DELETE = "Your Player File has been deleted";
  public String PLAYER_FILE_DELETE_OTHER = "You have deleted %PLAYER%'s PlayerFile";

  // Claiming
  public String CLAIM_BREAK = "You cannot break a block in this claim owned by %PLAYER%!";
  public String CLAIM_PLACE = "You cannot place a block in %PLAYER%'s Claim";
  public String CLAIM_INTERACT = "You dont have %PLAYER%'s permission to interact in this claim!";
  public String CLAIM_EXPLOSION = "You cannot explode %PLAYER%'s claim!";
  public String CHUNK_CLAIMED = "Chunk Claimed!";
  public String CHUNK_ALREADY_CLAIMED = "Chunk is allready claimed!";
  public String CLAIM_REMOVED = "Claim Deleted!";
  public String MISSING_CLAIM = "No Claim in this chunk";

  // Eco
  public String PAYED = "You paid %PLAYER% %AMOUNT% %COIN%";
  public String INVALID_HAND = "You must be holding an item to create a Buy sign";
  public String SIGN_CREATED = "%TYPE% sign created!";
  public String PURCHASED = "You just purchased an item for %COST% %COIN%s";
  public String NO_MONEY = "You cannot afford to buy this";
  public String SOLD;
  public String NO_ITEM;

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
