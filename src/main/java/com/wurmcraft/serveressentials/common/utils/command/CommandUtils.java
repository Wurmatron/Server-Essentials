package com.wurmcraft.serveressentials.common.utils.command;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
    for (Method method : command.getClass().getDeclaredMethods()) {
      if (method.getDeclaredAnnotation(SubCommand.class) != null) {
        for (String altName : method.getDeclaredAnnotation(SubCommand.class).aliases()) {
          subCommands.put(altName, method);
        }
        subCommands.put(method.getName(), method);
      }
    }
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

  public static boolean isUserConsole(ICommandSender sender) {
    return sender.getCommandSenderEntity() == null;
  }

  public static List<String> predictUsernames(String[] args, int index) {
    List<String> possibleUsernames =
        Arrays.asList(
            FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames());
    if (args.length > index && args[index] != null) {
      return predictName(args[index], possibleUsernames);
    } else {
      return possibleUsernames;
    }
  }

  public static List<String> predictRank(String[] args, int index) {
    List<String> possibleRanks =
        Arrays.stream(ServerEssentialsAPI.rankManager.getRanks())
            .map(Rank::getID)
            .collect(Collectors.toList());
    if (args.length > index && args[index] != null) {
      return predictName(args[index], possibleRanks);
    } else {
      return possibleRanks;
    }
  }

  private static List<String> predictName(String current, List<String> possibleNames) {
    List<String> predictedNames = new ArrayList<>();
    for (String name : possibleNames) {
      if (name.toLowerCase().startsWith(current.toLowerCase())
          || name.toLowerCase().endsWith(current.toLowerCase())) {
        predictedNames.add(name);
      }
    }
    return predictedNames;
  }

  public static EntityPlayer getPlayerForName(String name) {
    for (EntityPlayer player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      if (player.getName().equalsIgnoreCase(name)) {
        return player;
      }
    }
    List<String> possibleNames = predictUsernames(new String[] {name}, 0);
    if (possibleNames.size() == 1) {
      for (EntityPlayer player :
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
        if (player.getName().equalsIgnoreCase(possibleNames.get(0))) {
          return player;
        }
      }
    }
    return null;
  }

  public static boolean hasPerm(String perm, UUID uuid) {
    return hasPerm(perm, uuid.toString());
  }

  public static boolean hasPerm(String perm, String uuid) {
    return generatePermssionList(Objects.requireNonNull(UserManager.getUserRank(uuid)))
        .contains(perm);
  }

  public static boolean hasPerm(String perm, EntityPlayer player) {
    return hasPerm(perm, player.getGameProfile().getId());
  }

  public static List<String> generatePermssionList(Rank rank) {
    List<String> perms = new ArrayList<>();
    Collections.addAll(perms, rank.getPermission());
    for (String otherRanks : rank.getInheritance()) {
      Rank prevRank = ServerEssentialsAPI.rankManager.getRank(otherRanks);
      Collections.addAll(perms, prevRank.getPermission());
    }
    return perms;
  }
}
