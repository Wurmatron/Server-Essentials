package com.wurmcraft.serveressentials.core.utils;

import static com.wurmcraft.serveressentials.core.SECore.GSON;
import static com.wurmcraft.serveressentials.core.utils.AnnotationLoader.doesMethodExist;

import com.wurmcraft.serveressentials.core.Global;
import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.json.JsonParser;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.api.module.ModuleConfig;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class ModuleUtils extends SERegistry {

  private static NonBlockingHashMap<String, File> configCache = new NonBlockingHashMap<>();

  /** Load all the modules that can be loaded. */
  private static NonBlockingHashMap<String, ?> loadModules() {
    NonBlockingHashMap<String, Class<?>> modules = AnnotationLoader.searchForModules();
    loadedModules = modules; // Just temporary for module lookup
    NonBlockingHashMap<String, Object> modulesCanBeLoaded = new NonBlockingHashMap<>();
    for (String m : modules.keySet()) {
      Class<?> testModule = modules.get(m);
      if (isValidModule(testModule)) {
        try {
          modulesCanBeLoaded.put(m, modules.get(m).newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        SECore.logger.warning("Unable to load module '" + m + "'!");
      }
    }
    return loadedModules = modulesCanBeLoaded;
  }

  /** Load all the modules waiting between each phase for them to finish loading. */
  public static void loadAndSetupModules() {
    SECore.logger.info("Loading Modules ...");
    String[] modules = loadModules().keySet().toArray(new String[0]);
    if (modules.length > 0) {
      runAndWaitTillFinished(modules, "init");
      runAndWaitTillFinished(modules, "finalize");
    } else {
      SECore.logger.warning("No Modules Loaded!");
    }
  }

  /**
   * Runs the setup process for the given modules, waits for them to finish loading before
   * proceeding.
   *
   * @param modules modules to run initialize and finalize on
   * @param modulePhase init or finalize phase of the module
   */
  private static void runAndWaitTillFinished(String[] modules, String modulePhase) {
    AtomicInteger leftToComplete = new AtomicInteger(modules.length);
    for (String m : modules) {
      SECore.executors.schedule(
          () -> {
            Object moduleInstance = getModule(m);
            Module module = moduleInstance.getClass().getAnnotation(Module.class);
            try {
              AnnotationLoader.runMethod(
                  moduleInstance,
                  modulePhase.equals("init")
                      ? module.initalizeMethod()
                      : modulePhase.equals("finalize") ? module.completeSetup() : "",
                  null,
                  new Object[] {});
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            leftToComplete.decrementAndGet();
          },
          0,
          TimeUnit.MILLISECONDS);
    }
    // Wait for all modules to finish loading
    synchronized (Thread.currentThread()) {
      while (leftToComplete.get() > 0) {
        try {
          Thread.currentThread().wait(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Checks if a module can be loaded and has meet all its requirements to be loaded.
   *
   * @param clazz module class to check
   * @return if the module is configured and ready to be used
   */
  public static boolean isValidModule(Class<?> clazz) {
    if (clazz != null) {
      Module module = clazz.getAnnotation(Module.class);
      return canModuleBeLoaded(clazz, module) && hasDependenciesLoaded(module);
    }
    return false;
  }

  /**
   * Checks if a module has the required methods to load
   *
   * @param moduleClass class containing the module to check
   * @param module instance of the module annotation
   */
  public static boolean canModuleBeLoaded(Class<?> moduleClass, Module module) {
    if (module == null || module.name().isEmpty()) {
      return false;
    }
    if (doesMethodExist(moduleClass, module.initalizeMethod())
        && doesMethodExist(moduleClass, module.completeSetup())) {
      if (module.shouldAllaysBeLoaded()) {
        return true;
      } else {
        for (String m : SERegistry.globalConfig.enabledModules) {
          if (module.name().equalsIgnoreCase(m)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks if a modules dependencies have been loaded (recursively for all dependencies)
   *
   * @param module module to get the dependencies from
   * @return if the dependencies have been loaded
   */
  public static boolean hasDependenciesLoaded(Module module) {
    if (module.moduleDependencies().length > 0) {
      for (String s : module.moduleDependencies()) {
        try {
          if (!s.isEmpty()
              && !SERegistry.isModuleLoaded(s)
              && !hasDependenciesLoaded(
                  SERegistry.getModule(s).getClass().getAnnotation(Module.class))) {
            return false;
          }
        } catch (NoSuchElementException e) {
          SECore.logger.warning(
              "Unable to load module '" + module.name() + "' due to missing dependencies!");
          return false;
        }
      }
    }
    return true;
  }

  /** Loads the module configs / creates them with defaults if they dont exist */
  public static NonBlockingHashMap<String, JsonParser> loadModuleConfigs() {
    SECore.logger.info("Loading Module Configs ...");
    NonBlockingHashMap<String, JsonParser> loadedConfigs =
        AnnotationLoader.searchForModuleConfigs();
    for (String m : loadedConfigs.keySet()) {
      ModuleConfig module = loadedConfigs.get(m).getClass().getAnnotation(ModuleConfig.class);
      File moduleConfig = getModuleConfigFile(module, (StoredDataType) loadedConfigs.get(m));
      try {
        JsonParser loadedJson =
            FileUtils.getJson(moduleConfig, ((JsonParser) loadedConfigs.get(m)).getClass());
        loadedConfigs.put(m, loadedJson);
        configCache.put(m, moduleConfig);
        SERegistry.register(DataKey.MODULE_CONFIG, (StoredDataType) loadedJson);
      } catch (FileNotFoundException e) {
        SECore.logger.warning(
            "Unable to save module config '" + moduleConfig.getAbsolutePath() + "'");
      }
    }
    return loadedConfigs;
  }

  /**
   * Gets / Creates the config for a Module Config
   *
   * @param config module config you wish to get the file for
   * @return the file for the given module
   */
  private static File getModuleConfigFile(
      ModuleConfig config, StoredDataType defaultConfigInstance) {
    File moduleConfig =
        new File(
            SECore.SAVE_DIR
                + File.separator
                + "Modules"
                + File.separator
                + (defaultConfigInstance.getID().equalsIgnoreCase(config.moduleName())
                    ? config.moduleName()
                    : defaultConfigInstance.getID())
                + ".json");
    // Make sure the directory's exist first
    if (!moduleConfig.getParentFile().exists()) {
      if (!moduleConfig.getParentFile().mkdirs()) {
        SECore.logger.severe(
            "Unable to create save directory for "
                + Global.NAME
                + " Config Files! ("
                + moduleConfig.getParentFile().getAbsolutePath()
                + ")");
      }
    }
    if (!moduleConfig.exists()) {
      try {
        if (!moduleConfig.createNewFile()) {
          SECore.logger.warning(
              "Failed to save module config (" + moduleConfig.getAbsolutePath() + "')");
        } else {
          Files.write(moduleConfig.toPath(), GSON.toJson(defaultConfigInstance).getBytes());
        }
      } catch (IOException e) {
        e.printStackTrace();
        SECore.logger.warning(
            "Unable to save module config '" + moduleConfig.getAbsolutePath() + "'");
      }
    }
    return moduleConfig;
  }

  public static File getModuleConfigFile(String module) throws NoSuchElementException {
    if (configCache.containsKey(module)) {
      return configCache.get(module);
    }
    throw new NoSuchElementException(module + "'s config file is not configured!");
  }
}
