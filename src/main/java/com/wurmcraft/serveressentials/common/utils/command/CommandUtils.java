package com.wurmcraft.serveressentials.common.utils.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CommandUtils {

  /** Check if a command is disabled by the config */
  public static boolean isCommandDisabled(String name) {
    return Arrays.stream(ConfigHandler.disabledCommands).anyMatch(name::equalsIgnoreCase);
  }

  /** Generate a list of all the commands aliases (just standard Case) */
  public static List<String> getCommandAliases(Command command) {
    List<String> aliases = new ArrayList<>();
    aliases.add(command.getName());
    aliases.addAll(command.getAliases(aliases));
    return aliases;
  }

  /** Check if a command instance has SubCommand's */
  public static boolean hasSubCommand(Command command) {
    return Arrays.stream(command.getClass().getDeclaredMethods())
        .anyMatch(method -> method.getDeclaredAnnotation(SubCommand.class) != null);
  }

  /** Get a Ordered Map of the subCommands and there aliases */
  public static Map<String, Method> getSubCommands(Command command) {
    Map<String, Method> subCommands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    Arrays.stream(command.getClass().getDeclaredMethods())
        .filter(method -> method.getDeclaredAnnotation(SubCommand.class) != null)
        .forEachOrdered(
            method ->
                Arrays.stream(method.getDeclaredAnnotation(SubCommand.class).getAliases())
                    .forEach(altName -> subCommands.put(altName, method)));
    return subCommands;
  }

  /** Get the module of the given command */
  public static String getCommandModule(Command command) {
    return command.getClass().getAnnotation(ModuleCommand.class).moduleName();
  }

  /** Create a list of Commands to be use with the MC Command API */
  public static List<SECommand> generateListOfCommandWrappers(List<Command> commands) {
    return commands.stream().map(CommandUtils::createWrapper).collect(Collectors.toList());
  }

  /** Create the wrapper for the Command into one usable by the MC Command API */
  private static SECommand createWrapper(Command command) {
    return new SECommand(getCommandModule(command), command);
  }
}
