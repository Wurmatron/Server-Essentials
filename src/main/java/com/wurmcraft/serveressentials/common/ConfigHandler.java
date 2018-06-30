package com.wurmcraft.serveressentials.common;


import com.wurmcraft.serveressentials.common.reference.Global;
import net.minecraftforge.common.config.Config;

@Config(modid = Global.MODID, name = "ServerEssentials")
public class ConfigHandler {

  @Config.Comment("Modules to be loaded")
  @Config.RequiresMcRestart
  public static String[] modules = new String[]{"Chat", "Claim", "Info", "Language", "Rank", "Rest",
      "Security", "Teleportation"};

  @Config.Comment("Required if using Rest module")
  public static String restURL = "";
}
