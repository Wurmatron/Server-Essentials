package com.wurmcraft.serveressentials.api;

import com.wurmcraft.serveressentials.api.command.Command;
import java.util.List;

public class ServerEssentialsAPI {

  /**
   * List of all the loaded Server-Essentials Modules
   */
  public static List<String> modules;

  /**
   * List of all loaded Server-Essentials Commands
   */
  public static List<Command> commands;

  public static boolean isModuleLoaded(String module) {
    return modules.stream().anyMatch(m -> m.equalsIgnoreCase(module));
  }

}
