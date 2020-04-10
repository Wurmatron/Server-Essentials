package com.wurmcraft.serveressentials.core.registry;

import static com.wurmcraft.serveressentials.core.utils.CommandUtils.loadCommands;
import static com.wurmcraft.serveressentials.core.utils.ModuleUtils.loadAndSetupModules;
import static com.wurmcraft.serveressentials.core.utils.ModuleUtils.loadModuleConfigs;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import java.util.NoSuchElementException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class SERegistry {

  // Loaded Data
  protected static NonBlockingHashMap<String, ?> loadedModules = new NonBlockingHashMap<>();
  protected static NonBlockingHashMap<String, Object> loadedCommands = new NonBlockingHashMap<>();
  protected static NonBlockingHashMap<DataKey, NonBlockingHashMap<String, StoredDataType>>
      loadedData = new NonBlockingHashMap<>();
  protected static NonBlockingHashMap<DataKey, NonBlockingHashMap<String, StoredDataType>>
      tempData = new NonBlockingHashMap<>();

  public static void loadAndSetup() {
    loadAndSetupModules();
    loadModuleConfigs();
    loadCommands();
  }

  /**
   * Gets a list of all the loaded valid modules.
   *
   * @return a list of all the modules that are currently loaded
   */
  public static String[] getLoadedModules() {
    if (loadedModules != null && loadedModules.size() > 0) {
      return loadedModules.keySet().toArray(new String[0]);
    }
    return new String[0];
  }

  /**
   * Checks if a given module has been loaded.
   *
   * @param moduleName module to check
   */
  public static boolean isModuleLoaded(String moduleName) {
    return loadedModules.keySet().stream().anyMatch(m -> m.equalsIgnoreCase(moduleName));
  }

  /**
   * Gets a module based on its name and returns its instance if not it throws an exception.
   *
   * @param moduleName module to find
   * @throws NoSuchElementException If no module is found
   */
  public static Object getModule(String moduleName) throws NoSuchElementException {
    Object module = loadedModules.get(moduleName);
    if (module == null) {
      throw new NoSuchElementException("Unable to find module '" + moduleName + "'!");
    }
    return module;
  }

  /**
   * Gets a list of all the loaded valid commands.
   *
   * @return a list of all the commands that are currently loaded
   */
  public static String[] getLoadedCommands() {
    if (loadedCommands != null && loadedCommands.size() > 0) {
      return loadedCommands.keySet().toArray(new String[0]);
    }
    return new String[0];
  }

  /**
   * Checks if a given command has been loaded.
   *
   * @param commandName module to check
   */
  public static boolean isCommandLoaded(String commandName) {
    return loadedCommands.keySet().stream().anyMatch(m -> m.equalsIgnoreCase(commandName));
  }

  /**
   * Gets a module based on its name and returns its instance if not it throws an exception.
   *
   * @param commandName module to find
   * @throws NoSuchElementException If no module is found
   */
  public static Object getCommand(String commandName) throws NoSuchElementException {
    Object command = loadedCommands.get(commandName);
    if (command == null) {
      throw new NoSuchElementException("Unable to find command '" + commandName + "'!");
    }
    return command;
  }

  /**
   * Get Data that was stored in the database
   *
   * @param key key the data was stored under
   * @param dataID ID of the data that was stored
   * @return the instance of the data that was stored
   * @throws NoSuchElementException if the data cannot be found
   */
  public static StoredDataType getStoredData(DataKey key, String dataID)
      throws NoSuchElementException {
    if (SECore.dataHandler != null) {
      return SECore.dataHandler.getData(key, dataID);
    } else {
      SECore.logger.severe("A DataHandler does not exist or has been removed!");
    }
    throw new NoSuchElementException("Unable to load due to missing a DataHandler");
  }

  /**
   * Store a instance of a object in the database
   *
   * @param key key to store the data under
   * @param dataInstance Instance of the data you would like to store
   */
  public static void register(DataKey key, StoredDataType dataInstance) {
    if (SECore.dataHandler != null) {
      SECore.dataHandler.registerData(key, dataInstance);
    } else {
      SECore.logger.severe("A DataHandler does not exist or has been removed!");
    }
  }

  /**
   * Removes a instance from the database
   *
   * @param key Key the value was stored under
   * @param dataID instance with the same ID was the one you wish to remove
   */
  public static void delStoredData(DataKey key, String dataID) throws NoSuchElementException {
    if (SECore.dataHandler != null) {
      SECore.dataHandler.delData(key, dataID);
    } else {
      SECore.logger.severe("A DataHandler does not exist or has been removed!");
    }
  }
}
