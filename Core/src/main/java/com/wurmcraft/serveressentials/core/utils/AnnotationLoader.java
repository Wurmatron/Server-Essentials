package com.wurmcraft.serveressentials.core.utils;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Set;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.reflections.Reflections;

public class AnnotationLoader {

  private static final Reflections REFLECTIONS = new Reflections("com.wurmcraft");

  /**
   * Searches the classpath to find any Modules to be loaded
   *
   * @return a map of all the modules that are possible to be loaded
   */
  public static NonBlockingHashMap<String, Class<?>> searchForModules() {
    Set<Class<?>> modules = REFLECTIONS.getTypesAnnotatedWith(Module.class);
    NonBlockingHashMap<String, Class<?>> cachedModules = new NonBlockingHashMap<>();
    for (Class<?> clazz : modules) {
      Module module = clazz.getAnnotation(Module.class);
      if (canModuleBeLoaded(clazz, module)) {
        cachedModules.put(module.name(), clazz);
      }
    }
    return cachedModules;
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
  private static boolean canModuleBeLoaded(Class<?> moduleClass, Module module) {
    if (module == null || module.name().isEmpty()) {
      return false;
    }
    return doesMethodExist(moduleClass, module.initalizeMethod(), null)
        && doesMethodExist(
        moduleClass, module.completeSetup(), null);
  }

  /**
   * Checks if a given method exists within a class with the designated parameters
   *
   * @param classToTest module Class to test
   * @param method Name of the method to find
   * @param parameters Parameters the method is required to have
   */
  public static boolean doesMethodExist(Class<?> classToTest, String method,
      Class<?>... parameters) {
    try {
      classToTest.getDeclaredMethod(method, parameters);
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

  /**
   * Checks if a modules dependencies have been loaded (recursively for all dependencies)
   *
   * @param module module to get the dependencies from
   * @return if the dependencies have been loaded
   */
  private static boolean hasDependenciesLoaded(Module module) {
    if (module.moduleDependencies().length > 0) {
      for (String s : module.moduleDependencies()) {
        try {
          if (!s.isEmpty() && !SERegistry.isModuleLoaded(s) && !hasDependenciesLoaded(
              SERegistry.getModule(s).getAnnotation(Module.class))) {
            return false;
          }
        } catch (NoSuchElementException e) {
          SECore.logger.warning("Unable to load module '" + module.name()
              + "' due to missing dependencies!");
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Runs a given method with the given input parameters
   *
   * @param clazz class instance to run the method on
   * @param method name of the method to be run
   * @param methodParameters parameters to search for on the method
   * @param methodArguments arguments to run on the method
   */
  public static void runMethod(Class<?> clazz, String method, Class<?>[] methodParameters,
      Object[] methodArguments)
      throws NoSuchMethodException {
    try {
      Method methodToRun = clazz.getMethod(method, methodParameters);
      methodToRun.invoke(clazz, methodArguments);
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodException(
          "Method '" + method + "' does not exist within " + clazz.getSimpleName());
    } catch (IllegalAccessException | InvocationTargetException e) {
      SECore.logger
          .severe("Unable to invoke '" + method + "' in " + clazz.getSimpleName());
    }
  }
}
