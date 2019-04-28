package com.wurmcraft.serveressentials.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Allows for the easy creation of sub-command nodes to be executed depending on this commands input
 * arguments. Basically a subCommand is used for when a command has the same set of arguments in the
 * first argument of the command and you want to better separate the code without having to have a
 * ton of if-else blocks.
 *
 * <p>The code within this method should have the signature of (MinecraftServer,
 * ICommandSender,String[]) and will function exactly the same as
 * oduleCommand#execute(MinecraftServer, ICommandSender, String[])
 *
 * @see Command#execute(MinecraftServer, ICommandSender, String[])
 * @see Command
 * @see ModuleCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

  /**
   * A list of aliases that go along the the method's name to be used for the arguments input of the
   * command
   *
   * <p>This should be placed on a method with the signature of (MinecraftServer, ICommandSender,
   * String[]) were the last argument has lost an input used by this subCommand.
   *
   * @return list of aliases for this command ignoredCase
   * @see Command#execute(MinecraftServer, ICommandSender, String[])
   */
  String[] getAliases();
}
