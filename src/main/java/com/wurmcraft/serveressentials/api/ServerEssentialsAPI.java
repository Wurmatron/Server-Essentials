package com.wurmcraft.serveressentials.api;

import com.wurmcraft.serveressentials.api.command.Command;
import java.util.List;

public class ServerEssentialsAPI {

  /** List of all the loaded Server-Essentials Modules */
  public static List<String> modules;

  /** List of all loaded Server-Essentials Commands */
  public static List<Command> commands;

  /**
   * Checks if a given module of Server-Essentials is loaded
   *
   * @param module name of the module to check
   * @return if the specified module is currently active and loaded
   */
  public static boolean isModuleLoaded(String module) {
    return modules.stream().anyMatch(m -> m.equalsIgnoreCase(module));
  }
}
