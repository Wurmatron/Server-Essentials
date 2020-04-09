package com.wurmcraft.serveressentials.core.registry;

import static com.wurmcraft.serveressentials.core.utils.ModularUtils.loadAndSetupModules;

import java.util.NoSuchElementException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class SERegistry {

  // Loaded Data
  protected static NonBlockingHashMap<String, ?> loadedModules = new NonBlockingHashMap<>();
  protected static NonBlockingHashMap<String, Object> loadedData = new NonBlockingHashMap<>();
  protected static NonBlockingHashMap<String, Object> tempData = new NonBlockingHashMap<>();

  public static void loadAndSetup() {
    loadAndSetupModules();
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
}
