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

  @Comment("Currency used by this server and its transactions")
  public static String serverCurrency = "";

  @Comment("The default language for any new player's that join the server")
  public static String defaultLanguage = "en_us";

  @Comment("Name of this server when communicating with others")
  public static String serverName = "";

  @Comment(
      "Currency that this server will accept as valid for the network, anything else is removed from the player")
  public static String[] activeCurrency = new String[] {};

  public static String saveLocation = "ServerEssentials";
}
