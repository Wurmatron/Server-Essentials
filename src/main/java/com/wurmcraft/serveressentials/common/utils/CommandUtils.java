package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.Arrays;

public class CommandUtils {

  public static boolean isCommandDisabled(String name) {
    return Arrays.stream(ConfigHandler.disabledCommands).anyMatch(name::equalsIgnoreCase);
  }
}
