package com.wurmcraft.serveressentials.core.utils;

import static com.wurmcraft.serveressentials.core.utils.AnnotationLoader.doesMethodExist;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class ModularUtils extends SERegistry {

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
      SECore.EXECUTORS.schedule(
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
    return doesMethodExist(moduleClass, module.initalizeMethod(), (Class<?>) null)
        && doesMethodExist(moduleClass, module.completeSetup(), (Class<?>) null);
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
}
