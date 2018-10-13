package com.wurmcraft.serveressentials.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Apply to a command class to apply / add it to automatic loading within the module system */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

  /** Name of the module to load this command with */
  String moduleName() default "";

  /**
   * Required permission node for the command
   *
   * <p>Default is <moduleName>.<commandName>
   */
  String perm() default "";

  /**
   * Require the user to be on the trusted list to be able to run the command
   *
   * <p>Note: Requires Security Module to be active
   */
  boolean trustedRequired() default false;
}
