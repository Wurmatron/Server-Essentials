package com.wurmcraft.serveressentials.api;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.interfaces.IRankManager;
import com.wurmcraft.serveressentials.api.storage.Storage;
import java.util.List;

public class ServerEssentialsAPI {

  public static String storageType;

  /** List of all the loaded Server-Essentials Modules */
  public static List<String> modules;

  /** List of all loaded Server-Essentials Commands */
  public static List<Command> commands;

  /** Currently Active Rank Manager */
  public static IRankManager rankManager;

  /** Current file type storage type */
  public static Storage storage;

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
