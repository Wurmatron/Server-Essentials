package com.wurmcraft.serveressentials.core.registry;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.utils.AnnotationLoader;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class SERegistry {

  // Loaded Data
  private static NonBlockingHashMap<String, Class<?>> loadedModules = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<String, Object> loadedData = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<String, Object> tempData = new NonBlockingHashMap<>();

  public static void loadAndSetup() {
    loadAndSetupModules();
  }

  /**
   * Gets a list of all the loaded valid modules
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
   * Checks if a given module has been loaded
   *
   * @param moduleName module to check
   */
  public static boolean isModuleLoaded(String moduleName) {
    return loadedModules.keySet().stream().anyMatch(m -> m.equalsIgnoreCase(moduleName));
  }

  /**
   * Gets a module based on its name and returns its instance if not it throws an
   * exception.
   *
   * @param moduleName module to find
   * @throws NoSuchElementException If no module is found
   */
  public static Class<?> getModule(String moduleName) throws NoSuchElementException {
    Class<?> module = loadedModules.get(moduleName);
    if (module == null) {
      throw new NoSuchElementException("Unable to find module '" + moduleName + "'!");
    }
    return module;
  }

  /**
   * Load all the modules that can be loaded
   */
  private static NonBlockingHashMap<String, Class<?>> loadModules() {
    NonBlockingHashMap<String, Class<?>> modules = AnnotationLoader.searchForModules();
    loadedModules = modules; // Just temporary for module lookup
    NonBlockingHashMap<String, Class<?>> modulesCanBeLoaded = new NonBlockingHashMap<>();
    for (String m : modules.keySet()) {
      Class<?> testModule = modules.get(m);
      if (AnnotationLoader.isValidModule(testModule)) {
        modulesCanBeLoaded.put(m, modules.get(m));
      } else {
        SECore.logger.warning("Unable to load module '" + m + "'!");
      }
    }
    return loadedModules = modulesCanBeLoaded;
  }

  private static void loadAndSetupModules() {
    SECore.logger.info("Loading Modules ...");
    String[] modules = loadModules().keySet().toArray(new String[0]);
    if (modules.length > 0) {
      // Initialize the Module
      for (String m : modules) {
        SECore.EXECUTORS.schedule(() -> {
          Class<?> moduleInstance = getModule(m);
          Module module = moduleInstance.getAnnotation(Module.class);
          try {
            AnnotationLoader.runMethod(moduleInstance, module.initalizeMethod(), null,
                new Object[]{});
          } catch (NoSuchMethodException e) {
            SECore.logger.severe("Unable to initialize Module '" + module.name() + "'!");
          }
        }, 0, TimeUnit.SECONDS);
      }
      // Finalize the Module
      for (String m : modules) {
        SECore.EXECUTORS.schedule(() -> {
          Class<?> moduleInstance = getModule(m);
          Module module = moduleInstance.getAnnotation(Module.class);
          try {
            AnnotationLoader.runMethod(moduleInstance, module.completeSetup(), null,
                new Object[]{});
          } catch (NoSuchMethodException e) {
            SECore.logger.severe("Unable to initialize Module '" + module.name() + "'!");
          }
        }, 0, TimeUnit.SECONDS);
      }

    } else {
      SECore.logger.severe("No Modules To Load!");
    }
  }


}
