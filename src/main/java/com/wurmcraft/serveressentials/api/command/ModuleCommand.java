package com.wurmcraft.serveressentials.api.command;

import com.wurmcraft.serveressentials.api.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * Creates a new Server-Essentials Command along with the usage of a custom command handler to allow
 * for ignoring case for all of its commands.
 *
 * @see Command for automatic registration with its given module along with custom permission nodes
 * @see SubCommand Allows for the easy creation of sub-command nodes to be executed depending on
 *     this commands input arguments.
 */
public abstract class ModuleCommand {

  /**
   * the command's names used for /<commandName> When this command is run it will automatically
   * ignoreCase
   */
  public abstract String getName();

  /**
   * Display how the commands arguments are to be used
   *
   * @param lang language of the user sending the command
   */
  public abstract String getUsage(Lang lang);

  /**
   * Displays what this command is used for / how it works
   *
   * @param lang language of the user sending the command
   */
  public abstract String getDescription(Lang lang);

  /**
   * All the different names this command is known as, and is going to be run under
   *
   * @param aliases the currently known aliases for this command, Most of the time its empty
   * @return list of all the aliases for this command ignoringCase
   * @see ModuleCommand#getName()
   * @see CommandBase#getAliases()
   */
  public List<String> getAliases(List<String> aliases) {
    return aliases;
  }

  /**
   * Auto Completion of commands when the user has pressed tab. This is used to generate a list of
   * all the possible inputs for the user at its current state. IE: user home names, player names,
   * warp names, etc..
   *
   * @param server instance of the minecraft server this command is run on
   * @param sender user that sent this command
   * @param args input arguments for this command
   * @param pos location of the user running the command
   * @return list of all the possible autoFill for this command at its current state
   */
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return new ArrayList<>();
  }

  /**
   * Executes the usage of the command for the given input's and users.
   *
   * @param server instance of the minecraft server running this command
   * @param sender user that is running this command
   * @param args input arguments for this command
   * @see CommandBase#execute(MinecraftServer, ICommandSender, String[])
   */
  public abstract void execute(MinecraftServer server, ICommandSender sender, String[] args);

  /**
   * Can this command run in the console and function correctly / Does this command need to be run
   * as a player to even function ? IE Does it set / change something about the users location /
   * data
   *
   * @return is this command able / meant to be run in the console?
   */
  public boolean canConsoleRun() {
    return false;
  }
}
