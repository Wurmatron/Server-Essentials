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
}
