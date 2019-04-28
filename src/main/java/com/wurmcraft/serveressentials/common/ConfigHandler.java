package com.wurmcraft.serveressentials.common;

import com.wurmcraft.serveressentials.common.reference.Global;
import net.minecraftforge.common.config.Config;

@Config(modid = Global.MODID, name = "ServerEssentials")
public class ConfigHandler {

  public static String[] modules = new String[]{};

  public static String[] disabledCommands = new String[]{};
}
