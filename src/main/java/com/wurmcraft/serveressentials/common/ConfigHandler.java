package com.wurmcraft.serveressentials.common;

import com.wurmcraft.serveressentials.common.reference.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = Global.MODID, name = "ServerEssentials")
public class ConfigHandler {

  @Comment("Modules to be loaded")
  public static String[] modules =
      new String[]{
          "Chat", "Claim", "General", "Language", "Rank", "Rest", "Security", "Team",
          "Teleportation"
      };

  @Comment("How the data is stored (File, Rest)")
  public static String storageType = "file";

  @Comment("Required if using Rest module")
  public static String restURL = "";

  @Comment("Default Language")
  public static String defaultLanguage = "en_us";

  @Comment("Supported Server Languages (Requires Lang Files)")
  public static String[] supportedLanguages = new String[]{"en_us"};

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
}
