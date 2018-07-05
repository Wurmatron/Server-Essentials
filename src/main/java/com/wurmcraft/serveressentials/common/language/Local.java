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
  public String TP_HOME = "You have teleported to '%HOME%'";
  public String HOME_DELETED = "Home '%HOME%' has been deleted!";

  // Permissions
  public String PERM_ADDED = "Permission '%PERM% added to '%PLAYER%'";
  public String PERM_DEL = "Permission '%PERM% deleted from '%PLAYER%'";
  public String PERK_ADDED = "Permission '%PERM% added to '%PLAYER%'";
  public String PERK_DEL = "Permission '%PERM% deleted from '%PLAYER%'";


  // Chat
  public static String CHAT_SPACER = "=-=-=-=-=-=";
  public static String CHAT_RANK = "Rank";
  public static String CHAT_TEAM = "Team";
  public static String CHAT_LANG = "Language";
  public static String CHAT_LASTSEEN = "Seen";
  public static String PLAYER_ONLY = "Players Only!";
  public static String CHAT_NAME = "Name";
  public static String CHAT_PREFIX = "Prefix";
  public static String CHAT_SUFFIX = "Suffix";
  public static String CHAT_INHERITANCE = "Inheritance";
  public String CHAT_ACCEPT = "Accept";
  public String CHAT_DENY = "Deny";
  // Commands
  public static String RANK_NULL = "Rank Does not Exist!";
  public static String PLAYER_NOT_FOUND = "Player '%PLAYER%' not found!";
  public static String PLAYER_RANK_CHANGED = "You have changed '%PLAYER%'s rank to '%RANK%'";
  public static String INVALID_LANG = "Invalid Lang, %LANG%";
  public static String LANGUAGE_CHANGED = "Language Changed to, '%LANG%'";

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
