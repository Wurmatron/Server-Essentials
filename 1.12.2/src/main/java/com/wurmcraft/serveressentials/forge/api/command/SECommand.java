package com.wurmcraft.serveressentials.forge.api.command;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.forge.common.utils.CommandParser;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class SECommand extends CommandBase {

  public static String COMMAND_COLOR = TextFormatting.AQUA.toString();
  public static String COMMAND_INFO_COLOR = TextFormatting.LIGHT_PURPLE.toString();
  public static String ERROR_COLOR = TextFormatting.RED.toString();
  public static String USAGE_COLOR = TextFormatting.GOLD.toString();

  // Command Instances
  private ModuleCommand command;
  private Object commandInstance;
  // Cache
  private NonBlockingHashMap<CommandArguments[], Method> cache;
  private NonBlockingHashMap<String, CommandArguments> argumentCache;
  private StringBuilder commandUsage;

  public SECommand(ModuleCommand command, Object commandInstance) {
    this.command = command;
    this.commandInstance = commandInstance;
    cache = new NonBlockingHashMap<>();
    argumentCache = new NonBlockingHashMap<>();
    commandUsage = new StringBuilder();
    for (Method method : commandInstance.getClass().getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class)) {
        Command commandAnnotation = method.getAnnotation(Command.class);
        cache.put(commandAnnotation.inputArguments(), method);
        if (commandAnnotation.inputNames().length > 0) {
          commandUsage.append(" (");
          commandUsage.append(Arrays.toString(commandAnnotation.inputNames()));
          commandUsage.append(") ");
        }
      }
    }
  }

  @Override
  public String getName() {
    return command.name().substring(0, 1).toUpperCase() + command.name().substring(1)
        .toUpperCase();
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return USAGE_COLOR + "/" + command.name() + commandUsage.toString();
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    CommandArguments[] commandArgs = inputToArguments(server, args);
    Object[] handlerData = hasHandler(commandArgs);
    if ((boolean) handlerData[0]) {
      Method commandExec = (Method) handlerData[1];
      Command command = commandExec.getDeclaredAnnotation(Command.class);
      if (command.subCommand().isEmpty()) {
        try {
          commandExec
              .invoke(commandInstance,
                  CommandParser.parseLineToArguments(sender, args, commandArgs));
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (args.length > 0 && args[0].equalsIgnoreCase(command.subCommand())) {
        for (Method m : cache.values()) {
          if (m.getName().equals(command.subCommand())) {
            commandExec = m;
          }
        }
        try {
          commandExec
              .invoke(commandInstance,
                  CommandParser.parseLineToArguments(sender, args, commandArgs));
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public List<String> getAliases() {
    List<String> aliases = new ArrayList<>();
    aliases.add(command.name().toLowerCase());
    aliases.add(command.name().toUpperCase());
    return aliases;
  }

  private Object[] hasHandler(CommandArguments[] args) {
    for (CommandArguments[] a : cache.keySet()) {
      if (Arrays.equals(a, args)) {
        return new Object[]{true, cache.get(a)};
      }
    }
    return new Object[]{false, null};
  }


  public CommandArguments[] inputToArguments(MinecraftServer server,
      String[] line) {
    CommandArguments[] arguments = new CommandArguments[line.length];
    for (int i = 0; i < line.length; i++) {
      arguments[i] = getArgumentType(line[i]);
    }
    return arguments;
  }

  private CommandArguments getArgumentType(String line) {
    if (argumentCache.containsKey(line.toUpperCase())) {
      return argumentCache.get(line.toUpperCase());
    }
    if (isInteger(line)) {
      return CommandArguments.INTEGER;
    } else if (isDouble(line)) {
      return CommandArguments.DOUBLE;
    } else if (isPlayerUsername(line)) {
      return CommandArguments.PLAYER;
    } else {
      return CommandArguments.STRING;
    }
  }

  private boolean isInteger(String line) {
    try {
      Integer.parseInt(line);
      argumentCache.put(line.toUpperCase(), CommandArguments.INTEGER);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean isDouble(String line) {
    try {
      Double.parseDouble(line);
      argumentCache.put(line.toUpperCase(), CommandArguments.DOUBLE);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean isPlayerUsername(String line) {
    for (String name : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getOnlinePlayerNames()) {
      if (line.equalsIgnoreCase(name)) {
        argumentCache.put(line.toUpperCase(), CommandArguments.PLAYER);
        return true;
      }
    }
    return false;
  }
}
