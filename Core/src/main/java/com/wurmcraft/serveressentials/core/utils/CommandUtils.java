package com.wurmcraft.serveressentials.core.utils;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class CommandUtils extends SERegistry {

  /**
   * Loads all the possible commands from the classpath.
   *
   * @return a list of commands with an instance of each
   * @see CommandUtils#isValidCommand(Class)
   */
  public static NonBlockingHashMap<String, Object> loadCommands() {
    SECore.logger.info("Loading Commands ...");
    NonBlockingHashMap<String, Class<?>> commands = AnnotationLoader.searchForCommands();
    NonBlockingHashMap<String, Object> validCommands = new NonBlockingHashMap<>();
    for (String c : commands.keySet()) {
      Class<?> command = commands.get(c);
      if (isValidCommand(command)) {
        try {
          validCommands.put(c, command.newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return loadedCommands = validCommands;
  }

  /**
   * Checks if a class contains the methods to be a command
   *
   * @param commandClass class for the creation of a command
   * @param command command annotation for this command class
   * @return if the given command contains the methods to be a command
   */
  public static boolean canCommandBeLoaded(Class<?> commandClass, ModuleCommand command) {
    if (command == null || command.name().isEmpty()) {
      return false;
    }
    String commandModule = command.moduleName();
    if (commandModule.isEmpty()) {
      String packageName = commandClass.getPackage().getName();
      commandModule = packageName.substring(packageName.lastIndexOf(".") + 1);
    }
    return doesCommandHaveExecutors(commandClass) && SERegistry.isModuleLoaded(commandModule);
  }

  /**
   * Checks if a given class have the Command annotation somewhere on a method
   *
   * @param commandClass class to check for methods
   * @return if a class contains a Command annotation
   */
  private static boolean doesCommandHaveExecutors(Class<?> commandClass) {
    if (commandClass != null) {
      Method[] methods = commandClass.getDeclaredMethods();
      return Arrays.stream(methods).anyMatch(m -> m.isAnnotationPresent(Command.class));
    }
    return false;
  }

  /**
   * Validates if a command has the required method and subCommand methods to be a valid command
   *
   * @param clazz command class to verify its able to used as a command
   * @return if a command has the valid methods and dependencies required to load
   */
  public static boolean isValidCommand(Class<?> clazz) {
    if (clazz != null) {
      Method[] methods = clazz.getDeclaredMethods();
      for (Method m : methods) {
        if (m.isAnnotationPresent(Command.class)) {
          Command command = m.getDeclaredAnnotation(Command.class);
          if (!command.subCommand().isEmpty()) {
            Method method = null;
            for (Method mm : methods) {
              if (mm.getName().equalsIgnoreCase(command.subCommand())) {
                method = mm;
                break;
              }
            }
            if (method == null
                || !Arrays.asList(command.inputArguments())
                    .containsAll(
                        Arrays.asList(method.getAnnotation(Command.class).inputArguments()))) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }
}
