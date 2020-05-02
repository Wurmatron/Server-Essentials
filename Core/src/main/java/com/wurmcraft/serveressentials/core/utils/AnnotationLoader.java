package com.wurmcraft.serveressentials.core.utils;

import static com.wurmcraft.serveressentials.core.utils.CommandUtils.canCommandBeLoaded;
import static com.wurmcraft.serveressentials.core.utils.ModuleUtils.canModuleBeLoaded;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;
import com.wurmcraft.serveressentials.core.api.module.Module;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
      SECore.logger.finest("Attempting to load module '" + module.name() + "'");
      if (canModuleBeLoaded(clazz, module)) {
        cachedModules.put(module.name(), clazz);
      }
    }
    return cachedModules;
  }

  /**
   * Searches the classpath to find any commands to be loaded
   *
   * @return a map of all the commands that are possible to be loaded
   */
  public static NonBlockingHashMap<String, Class<?>> searchForCommands() {
    Set<Class<?>> commands = REFLECTIONS.getTypesAnnotatedWith(ModuleCommand.class);
    NonBlockingHashMap<String, Class<?>> cachedCommands = new NonBlockingHashMap<>();
    for (Class<?> clazz : commands) {
      ModuleCommand command = clazz.getAnnotation(ModuleCommand.class);
      if (canCommandBeLoaded(clazz, command)) {
        cachedCommands.put(command.name(), clazz);
      }
    }
    return cachedCommands;
  }

  /**
   * Searches the classpath to find any Module Config's that can be loaded
   *
   * @return a map of all the module configs that can be loaded
   */
  public static NonBlockingHashMap<String, StoredDataType> searchForModuleConfigs()
      throws NullPointerException {
    Set<Class<?>> configs = REFLECTIONS.getTypesAnnotatedWith(ConfigModule.class);
    NonBlockingHashMap<String, StoredDataType> cachedConfigs = new NonBlockingHashMap<>();
    for (Class<?> clazz : configs) {
      try {
        Object configClass = clazz.newInstance();
        ConfigModule config = clazz.getAnnotation(ConfigModule.class);
        if (configClass instanceof StoredDataType) {
          cachedConfigs.put(config.moduleName(), (StoredDataType) configClass);
        } else {
          SECore.logger.warning(
              "Module '"
                  + config.moduleName()
                  + "' failed to load due to not subClass of JsonParser");
        }
      } catch (InstantiationException | IllegalAccessException e) {
        throw new NullPointerException(
            "Module '"
                + clazz.getAnnotation(ConfigModule.class).moduleName()
                + "' does not have a default constructor");
      }
    }
    return cachedConfigs;
  }

  /**
   * Checks if a given method exists within a class with the designated parameters
   *
   * @param classToTest module Class to test
   * @param method Name of the method to find
   * @param parameters Parameters the method is required to have
   */
  public static boolean doesMethodExist(
      Class<?> classToTest, String method, Class<?>... parameters) {
    try {
      classToTest.getDeclaredMethod(method, parameters);
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

  /**
   * Checks if a given method exists within a class with the designated parameters
   *
   * @param classToTest module Class to test
   * @param method Name of the method to find
   */
  public static boolean doesMethodExist(Class<?> classToTest, String method) {
    try {
      classToTest.getDeclaredMethod(method);
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

  /**
   * Runs a given method with the given input parameters
   *
   * @param classInstance class instance to run the method on
   * @param method name of the method to be run
   * @param methodParameters parameters to search for on the method
   * @param methodArguments arguments to run on the method
   */
  public static void runMethod(
      Object classInstance, String method, Class<?>[] methodParameters, Object[] methodArguments)
      throws NoSuchMethodException {
    try {
      Method methodToRun = classInstance.getClass().getMethod(method, methodParameters);
      methodToRun.invoke(classInstance, methodArguments);
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodException(
          "Method '"
              + method
              + "' does not exist within "
              + classInstance.getClass().getSimpleName());
    } catch (IllegalAccessException | InvocationTargetException e) {
      SECore.logger.severe(
          "Unable to invoke '" + method + "' in " + classInstance.getClass().getSimpleName());
    }
  }
}
