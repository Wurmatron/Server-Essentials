package com.wurmcraft.serveressentials.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Looks for this annotation of a method when hasSubCommand() is true
 *
 * <p>public void subCommandName(ICommandSender String[])
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

  String[] getAliases();
}
