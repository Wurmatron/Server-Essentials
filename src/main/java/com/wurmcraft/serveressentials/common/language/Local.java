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
  public String TPA_SENT = "Tpa Request Sent!";
  public String TPA_Recive = "%PLAYER% has sent you a TPA Request %ACCEPT% | %DENY%";
  public String TPA_ACCEPT = "Tpa Request Accepted!";
  public String TPA_DENY = "Tpa Request Denied!";
  public String TP_HOME = "You have teleported to '%HOME%'";
  public String HOME_DELETED = "Home '%HOME%' has been deleted!";
  public String TP_BACK = "You have teleported back to your previous location";
  public String TP_BACK_FAIL = "Failed to find your last known location!";
  public String TP = "You have been teleported!";
  public String TP_HERE = "%PLAYER% has been teleported to your current location.";

  // Permissions
  public String PERM_ADDED = "Permission '%PERM% added to '%PLAYER%'";
  public String PERM_DEL = "Permission '%PERM% deleted from '%PLAYER%'";
  public String PERK_ADDED = "Permission '%PERM% added to '%PLAYER%'";
  public String PERK_DEL = "Permission '%PERM% deleted from '%PLAYER%'";

  // Chat
  public String CHAT_SPACER = "=-=-=-=-=-=";
  public String CHAT_RANK = "Rank";
  public String CHAT_TEAM = "Team";
  public String CHAT_LANG = "Language";
  public String CHAT_LASTSEEN = "Seen";
  public String PLAYER_ONLY = "Players Only!";
  public String CHAT_NAME = "Name";
  public String CHAT_PREFIX = "Prefix";
  public String CHAT_SUFFIX = "Suffix";
  public String CHAT_INHERITANCE = "Inheritance";
  public String CHAT_ACCEPT = "Accept";
  public String CHAT_DENY = "Deny";
  public String GLOBAL = "Global";
  public String LOCAL = "Local";
  public String CHAT_LEADER = "Leader";
  // Commands
  public String RANK_NULL = "Rank Does not Exist!";
  public String PLAYER_NOT_FOUND = "Player '%PLAYER%' not found!";
  public String PLAYER_RANK_CHANGED = "You have changed '%PLAYER%'s rank to '%RANK%'";
  public String INVALID_LANG = "Invalid Lang, %LANG%";
  public String LANGUAGE_CHANGED = "Language Changed to, '%LANG%'";
  public String GOD_DISABLE = "God Mode Disabled";
  public String GOD_ENABBLE = "God Mode Enabled";
  public String TPLOCK_DISABLED = "TP Lock Disabled";
  public String TPLOCK_ENABLED = "TP Lock Enabled";
  public String INVALID_NUMBER = "%NUMBER% is not a valid number!";

  // ECO
  public String PAYED = "You paid %PLAYER% %AMOUNT% %COIN%";
  public String INVALID_HAND = "You must be holding an item to create a Buy sign";
  public String SIGN_CREATED = "%TYPE% sign created!";
  public String PURCHASED = "You just purchased an item for %COST% %COIN%s";
  public String NO_MONEY ="You cannot afford to buy this";
  public String SOLD = "You just sold a item and gained %COST% %COIN%s";
  public String NO_ITEM = "You dont have any of this item!";

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
