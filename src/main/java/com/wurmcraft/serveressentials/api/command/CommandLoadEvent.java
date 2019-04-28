package com.wurmcraft.serveressentials.api.command;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a module is loading a command
 */
public class CommandLoadEvent extends Event {

  /**
   * Name of the module this command is part o
   */
  public String moduleName;

  /**
   * instance of the command
   */
  public Command command;

  /**
   * The commands permission node if its been overwritten from the standard format
   */
  public String perm;

  /**
   * This command requires a trusted user to run it
   */
  public boolean trusted;

  public CommandLoadEvent(String moduleName,
      Command command, String perm, boolean trusted) {
    this.moduleName = moduleName;
    this.command = command;
    this.perm = perm;
    this.trusted = trusted;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }
}
