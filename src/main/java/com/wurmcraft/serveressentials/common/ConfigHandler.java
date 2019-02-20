package com.wurmcraft.serveressentials.common;

import com.wurmcraft.serveressentials.api.json.global.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = Global.MODID, name = "ServerEssentials")
public class ConfigHandler {

  @Comment("Modules to be loaded")
  public static String[] modules =
      new String[] {
        "AutoRank",
        "Chat",
        "Claim",
        "Economy",
        "General",
        "Language",
        "Rank",
        "Security",
        "TeamCommand",
        "Teleportation",
        "Protect"
      };

  @Comment("How the data is stored (File, Rest)")
  public static String storageType = "file";

  @Comment("Required if using Rest module")
  public static String restURL = "";

  @Comment("Rest API Auth Key")
  public static String restLogin = "test:password";

  @Comment("Default Language")
  public static String defaultLanguage = "en_us";

  @Comment("Supported Server Languages (Requires Lang Files)")
  public static String[] supportedLanguages = new String[] {"en_us"};

  @Comment(
      "How long to wait (in minutes) before checking for an update for Rest API (Saves Bandwidth)")
  public static int syncPeriod = 5;

  @Comment("Default rank name")
  public static String defaultRank = "Default";

  @Comment("Directory were the data will be stored")
  public static String saveLocation = "Server-Essentials";

  @Comment("Default channel for users")
  public static String defaultChannel = "global";

  @Comment("Forge default channel on join")
  public static boolean forceDefaultChannelOnJoin = true;

  @Comment("How many of the same message will be displayed before flagging as spam")
  public static int spamLimit = 5;

  @Comment("How many seconds before another teleport can be completed")
  public static int teleportTimer = 4;

  @Comment(("How long you have to stay still to teleport"))
  public static int teleportDelay = 0;

  @Comment("Default home name")
  public static String defaultHome = "home";

  @Comment("How chat is displayed")
  public static String chatFormat =
      "%channel% " + "[%team%] " + "%rankPrefix% " + "%username% " + "%rankSuffix% " + "%message%";

  @Comment("(Rest Only) List of all the currencys this server will accept / acknowledge")
  public static String[] activeCurrency = new String[] {"Doge Coin", "NepNep Coin"};

  @Comment("Name of the server currency")
  public static String serverCurrency = "Doge Coin";

  @Comment("How many seconds before a TPA request will timeout")
  public static int tpaTimeout = 120;

  @Comment("URL to trusted staff list (UUID)")
  public static String trustedStaff = "";

  @Comment("Message displayed to users on server shutdown")
  public static String shutdownMessage = "&cServer Shutdown";

  @Comment(("Link  to your discord server"))
  public static String discordLink = "";

  @Comment("Changes how messages are displayed")
  public static String msgFormat = "[%sender% -> %username%] %message%";

  @Comment("Channel is not intercepted by Server-Essentials")
  public static String globalChannel = "global";

  @Comment("Currency all others converge into")
  public static String globalCurrency = "global";

  @Comment("Makes sure any Trusted User is OP'd upon joining")
  public static boolean autoOP = true;

  @Comment("Check if its safe before teleporting")
  public static boolean safeTeleport = true;

  @Comment(
      "Checks for duplicated ip addresses on the server, which may mean the user is using an alt")
  public static boolean altCheck = false;

  @Comment("Servers name when viewed from others")
  public static String serverName = "";
}
