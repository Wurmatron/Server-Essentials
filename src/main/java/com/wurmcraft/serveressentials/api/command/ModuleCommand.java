package com.wurmcraft.serveressentials.api.command;

import java.util.List;
import net.minecraft.command.ICommandSender;

/**
 * Added to a module command along with ICommand For easier use try SECommand
 *
 * @see net.minecraft.command.ICommand
 */
public interface ModuleCommand {

  /** Aliases for this command */
  List<String> getAltNames();

  /** Override a commands permission from Annotation or default generation */
  String getCommandPerm();

  /** Can the Console run this command */
  boolean canConsoleRun();

  /**
   * Allow for the creation of SubCommands
   *
   * @see SubCommand
   */
  boolean hasSubCommand();

  /** Explain the use of the command displayed via help */
  String getDescription(ICommandSender sender);
}
