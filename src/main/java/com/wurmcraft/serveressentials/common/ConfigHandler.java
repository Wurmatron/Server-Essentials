package com.wurmcraft.serveressentials.common;

import com.wurmcraft.serveressentials.common.reference.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = Global.MODID, name = "ServerEssentials")
public class ConfigHandler {

  @Comment("The modules that Server-Essentials will use")
  public static String[] modules = new String[] {};

  @Comment("A list of commands that are to be disabled and unable to run")
  public static String[] disabledCommands = new String[] {};

  @Comment("Maximum amount of threads Server-Essentials will use for additional processing")
  public static int maxProcessingThreads = 2;

  @Comment("Rest URL for Server-Essentials Data-Base (Empty will default to File-Storage)")
  public static String restURL = "";

  @Comment("Rest URL Login Info")
  public static String restAuth = "user:password";

  @Comment("Channel players will join upon joining the server")
  public static String defaultChannel = "global";

  @Comment("Channel that allows for outside mod support aka something like MatterLink")
  public static String globalChannel = "global";

  @Comment("Currency used by this server and its transactions")
  public static String serverCurrency = "";

  @Comment("The default language for any new player's that join the server")
  public static String defaultLanguage = "en_us";

  @Comment("Name of this server when communicating with others")
  public static String serverName = "";

  @Comment(
      "Currency that this server will accept as valid for the network, anything else is removed from the player")
  public static String[] activeCurrency = new String[] {};

  @Comment("Save directory")
  public static String saveLocation = "ServerEssentials";

  @Comment("Default server rank for new users")
  public static String defaultRank = "default";

  @Comment("Rank Sync Period in Seconds")
  public static int rankSyncPeriod = 90;

  @Comment("User Data Type Period in Seconds")
  public static int userDataSyncPeriod = 90;

  @Comment("How many ticks before checking for a user rank-up")
  public static int autoRankCheckPeriod = 6000;

  @Comment("Dont change this unless you know what it does")
  public static String languageURLFormat =
      "https://raw.githubusercontent.com/Wurmcraft/Server-Essentials/Post-Rest-Rework/lang/";

  @Comment("How the Chat-Module will format the in-game chat")
  public static String chatFormat =
      "%channel% %team% %rankPrefix% %username% %rankSuffix% %message%";

  @Comment("How to format private messages")
  public static String msgFormat = "%rankPrefix% %username%: &6%message";

  @Comment("How many times the same message can appear in chat before its blocked")
  public static int spamLimit = 3;

  @Comment("Character used for player sound notification in-chat")
  public static String chatNotification = "@";

  @Comment("Display MOTD on every join?")
  public static boolean motdOnJoin = true;

  @Comment("Delay between each teleport")
  public static int teleportTimer = 3;
}
