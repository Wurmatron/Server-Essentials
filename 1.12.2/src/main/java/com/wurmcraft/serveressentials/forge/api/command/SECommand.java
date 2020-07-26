package com.wurmcraft.serveressentials.forge.api.command;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.config.CommandCost;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.CommandParser;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.command.PerkCommand.Perk;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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
  private CommandCost cost;

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
    if (SERegistry.isModuleLoaded("Economy")) {
      EconomyConfig config = (EconomyConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Economy");
      if (config.commandCost != null && config.commandCost.length > 0) {
        for (CommandCost cmd : config.commandCost) {
          if (cmd.commandName.equalsIgnoreCase(getName())) {
            this.cost = cmd;
          }
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
    Object[] handler = findMatch(sender, cache.keySet(), args);
    if ((boolean) handler[0]) {
      try {
        Object[] commandArgs = CommandParser.parseLineToArguments(sender, args,
            ((Method) handler[1]).getDeclaredAnnotation(Command.class).inputArguments());
        ((Method) handler[1]).invoke(commandInstance, commandArgs);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }

  private Object[] findMatch(ICommandSender sender, Set<CommandArguments[]> methodArgs,
      String[] args) {
    CommandArguments[] exactMatch = findExactMatch(sender, methodArgs, args);
    if (exactMatch != null) {
      return new Object[]{true, cache.get(exactMatch)};
    } else {
      CommandArguments[] strArr = findStringArray(methodArgs, args);
      if (strArr != null) {
        return new Object[]{true, cache.get(strArr)};
      } else {
        CommandArguments[] fussyMatch = findFussyMatch(sender, methodArgs, args);
        if (fussyMatch != null) {
          return new Object[]{true, cache.get(fussyMatch)};
        }
      }
    }
    return new Object[]{false, null};
  }

  private CommandArguments[] findExactMatch(ICommandSender sender,
      Set<CommandArguments[]> methodArgs,
      String[] args) {
    for (CommandArguments[] ca : methodArgs) {
      if (ca.length == args.length) {
        boolean valid = true;
        for (int index = 0; index < ca.length; index++) {
          CommandArguments inputType = getArgumentType(sender, args[index]);
          if (SERegistry.globalConfig.debug) {
            ServerEssentialsServer.logger.debug(
                Arrays.toString(ca) + " => " + ca[index] + " " + inputType + " ("
                    + args[index] + ")");
          }
          if (!ca[index].equals(inputType)) {
            valid = false;
          }
        }
        if (valid) {
          return ca;
        }
      }
    }
    return null;
  }

  private CommandArguments[] findStringArray(Set<CommandArguments[]> methodArgs,
      String[] args) {
    for (CommandArguments[] ca : methodArgs) {
      if (ca.length == 1 && ca[0].equals(CommandArguments.STRING_ARR)) {
        return ca;
      }
    }
    return null;
  }

  private CommandArguments[] findFussyMatch(ICommandSender sender,
      Set<CommandArguments[]> methodArgs,
      String[] args) {
    for (CommandArguments[] ca : methodArgs) {
      if (ca.length == args.length) {
        boolean valid = true;
        for (int index = 0; index < ca.length; index++) {
          CommandArguments inputType = getArgumentType(sender, args[index]);
          if (ca[index].equals(CommandArguments.STRING) && inputType.stringable) {

          } else {
            valid = false;
          }
        }
        if (valid) {
          return ca;
        }
      }
    }
    return null;
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

  private CommandArguments getArgumentType(ICommandSender sender, String line) {
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
    } else if (isRank(line)) {
      return CommandArguments.RANK;
    } else if (sender.getCommandSenderEntity() instanceof EntityPlayer && isHome(
        (EntityPlayer) sender.getCommandSenderEntity(), line)) {
      return CommandArguments.HOME;
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

  private boolean isRank(String line) {
    for (Rank rank : SECore.dataHandler.getDataFromKey(DataKey.RANK, new Rank())
        .values()) {
      if (rank.getName().equalsIgnoreCase(line)) {
        return true;
      }
    }
    return false;
  }

  private boolean isHome(EntityPlayer player, String line) {
    Home[] homes = PlayerUtils.getPlayer(player).server.homes;
    for (Home home : homes) {
      if (home.name.equalsIgnoreCase(line)) {
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
    if (args.length < 1) {
      return getAllPossible(sender, 0);
    } else {
      return getAllPossible(sender, args.length - 1);
    }

  }

  private List<String> getAllPossible(ICommandSender sender, int index) {
    List<String> fill = new LinkedList<>();
    for (CommandArguments[] commandArg : cache.keySet()) {
      if (index < commandArg.length) {
        fill.addAll(autoFill(sender, commandArg[index], "",
            cache.get(commandArg).getDeclaredAnnotation(Command.class), index));
      }
    }
    return deduplicateList(fill);
  }

  private List<String> autoFill(ICommandSender sender, CommandArguments type, String line,
      Command cmd, int pos) {
    if (type.equals(CommandArguments.PLAYER)) {
      if (line != null && !line.isEmpty()) {
        return PlayerUtils.predictUsernames(line);
      } else {
        return Arrays.asList(
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                .getOnlinePlayerNames());
      }
    } else if (type.equals(CommandArguments.HOME) && sender
        .getCommandSenderEntity() instanceof EntityPlayer) {
      Home[] homes = PlayerUtils
          .getPlayer((EntityPlayer) sender.getCommandSenderEntity()).server.homes;
      if (homes != null && homes.length > 0) {
        return PlayerUtils.predictHome(line, homes);
      }
    } else if (type.equals(CommandArguments.RANK)) {
      return RankUtils.predictRank(line,
          SECore.dataHandler.getDataFromKey(DataKey.RANK, new Rank()).values()
              .toArray(new Rank[0]));
    } else if (type.equals(CommandArguments.PERK)) {
      List<String> perks = new ArrayList<>();
      for (Perk p : Perk.values()) {
        perks.add(p.name());
      }
      return perks;
    } else if (type.equals(CommandArguments.STRING) && cmd != null) {
      if (cmd.inputNames().length > pos) {
        String inputNames = cmd.inputNames()[pos];
        return Arrays.asList(inputNames.split(","));
      }
    }
    return new ArrayList<>();
  }

  private List<String> deduplicateList(List<String> values) {
    Set<String> noDuplicates = new LinkedHashSet<>(values);
    values.clear();
    values.addAll(noDuplicates);
    return values;
  }
}
