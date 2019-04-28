package com.wurmcraft.serveressentials.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Modules are the key part of server-essential by grouping features together into modules that can
 * be enabled and disabled to allow for customization along with different server command
 * requirements and structures.
 *
 * @see com.wurmcraft.serveressentials.api.command.Command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {

  /**
   * Name of the module that is being created within this class
   *
   * @return name of the module
   */
  String name();

  /**
   * Name of the method used for loading all the settings / data for this module including any
   * required.
   *
   * @return name of the method to look for to use for the creation / setup of the modules upon
   *     being loaded.
   */
  String setupMethod() default "setup";
}
