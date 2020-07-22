package com.wurmcraft.serveressentials.forge.api.command;

import static com.wurmcraft.serveressentials.forge.common.utils.CommandParser.getInstanceForArgument;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.CommandParser;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.command.PerkCommand.Perk;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class SECommand extends CommandBase {

  public static String COMMAND_COLOR = TextFormatting.AQUA.toString();
  public static String COMMAND_INFO_COLOR = TextFormatting.LIGHT_PURPLE.toString();
  public static String ERROR_COLOR = TextFormatting.RED.toString();
  public static String USAGE_COLOR = TextFormatting.GOLD.toString();

  private ModuleCommand command;
  private Object commandInstance;
  private boolean hasStringArray;
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
        if (commandAnnotation.inputArguments().length > 0 && commandAnnotation
            .inputArguments()[0].equals(CommandArguments.STRING_ARR)
            || commandAnnotation.inputArguments().length > 1 && commandAnnotation
            .inputArguments()[1].equals(CommandArguments.STRING_ARR)) {
          hasStringArray = true;
        }
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
    return command.name().toLowerCase();
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
          if(SERegistry.globalConfig.logCommandToCMD) {
            ServerEssentialsServer.logger.info(sender.getDisplayName().getUnformattedText() + " has run command `/" + getName() + " " + String.join(" ", args) + "'");
          }
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
          if(SERegistry.globalConfig.logCommandToCMD) {
            ServerEssentialsServer.logger.info(sender.getDisplayName().getUnformattedText() + " has run command `/" + getName() + " " + String.join(" ", args) + "'");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    } else {
      if (hasStringArray) {
        if (args.length > 0 && getArgumentType(args[0])
            .equals(CommandArguments.PLAYER)) { // Player, String[]
          for (Method m : commandInstance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Command.class)) {
              Command command = m.getDeclaredAnnotation(Command.class);
              if (command.inputArguments().length > 1 && command.inputArguments()[0]
                  .equals(CommandArguments.PLAYER) && command.inputArguments()[1]
                  .equals(CommandArguments.STRING_ARR)) {
                try {
                  m.invoke(commandInstance, sender,
                      getInstanceForArgument(args[0], CommandArguments.PLAYER),
                      Arrays.copyOfRange(args, 1, args.length));
                  if(SERegistry.globalConfig.logCommandToCMD) {
                    ServerEssentialsServer.logger.info(sender.getDisplayName().getUnformattedText() + " has run command `/" + getName() + " " + String.join(" ", args) + "'");
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          }
        } else if (args.length > 0) {
          for (Method m : commandInstance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Command.class)) {
              Command command = m.getDeclaredAnnotation(Command.class);
              if (command.inputArguments().length >= 1 && command.inputArguments()[0]
                  .equals(CommandArguments.STRING_ARR)) {
                try {
                  m.invoke(commandInstance, sender, args);
                  if(SERegistry.globalConfig.logCommandToCMD) {
                    ServerEssentialsServer.logger.info(sender.getDisplayName().getUnformattedText() + " has run command `/" + getName() + " " + String.join(" ", args) + "'");
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          }
        } else {
          sender.sendMessage(new TextComponentString(getUsage(sender)));
        }
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    }
  }

  @Override
  public List<String> getAliases() {
    List<String> aliases = new ArrayList<>();
    aliases.add(command.name().toLowerCase());
    aliases.add(command.name().toUpperCase());
    if (command.aliases().length > 0) {
      for (String a : command.aliases()) {
        aliases.add(a);
        aliases.add(a.toLowerCase());
        aliases.add(a.toUpperCase());
      }
    }
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
    } else if (isPerk(line)) {
      return CommandArguments.PERK;
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

  private boolean isPerk(String line) {
    for (Perk p : Perk.values()) {
      if (p.name().equalsIgnoreCase(line)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank")) {
      return RankUtils.hasPermission(RankUtils.getRank(sender),
          command.moduleName() + "." + command.name());
    } else {
      return true;
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos targetPos) {
    int pos = args.length - 1;
    for (CommandArguments[] a : cache.keySet()) {
      if (a.length == args.length || a.length == args.length + 1) {
        CommandArguments arg = a[pos];
        if (arg == CommandArguments.PLAYER) {
          return PlayerUtils.predictUsernames(args, pos);
        } else if (arg == CommandArguments.PERK) {
          List<String> perks = new ArrayList<>();
          for (Perk p : Perk.values()) {
            perks.add(p.name());
          }
          return perks;
        } else if (arg == CommandArguments.STRING) {
          Command cmd = cache.get(a).getAnnotation(Command.class);
          if (cmd.inputNames().length > pos) {
            String inputNames = cmd.inputNames()[pos];
            return Arrays.asList(inputNames.split(","));
          }
        }
      }
    }
    return super.getTabCompletions(server, sender, args, targetPos);
  }


}
