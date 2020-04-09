package com.wurmcraft.serveressentials.core.api.module;

import java.lang.annotation.*;

/**
 * A Module is a group of features the preform a given goal such as handling Time-Based
 * Rankups or handling of user teleportation.
 *
 * The main purpose of the module system is to allow for customization and customization
 * between multiple server systems to allow for more customization to allow for a larger
 * range of server customizations to fit any server. Note: Certain modules may be enabled
 * / disabled according to the users needs so each module should act independently.
 * However if this is not the case such as requiring another module to function place it
 * under Module#moduleDependencies()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {

  /**
   * Name of the module for use in config and permissions
   *
   * @return Name given to this module when loading along with its permission given to
   * commands
   */
  String name();

  /**
   * The module is general required as such should be enabled by default
   *
   * @return Should this module be added upon first creation of the module config.
   */
  boolean defaultModule() default false;

  /**
   * This array is to be used only if this module requires another module such as
   * requiring the Rest module to be loaded for this module to function If any of the
   * required dependencies are missing the module will not load and print out a statement
   * about the module requiring another to load.
   *
   * @return A Array of modules required for this module to be loaded
   */
  @Deprecated
  String[] moduleDependencies() default "";

  /**
   * This "String" or method is to be used to initialize the module, This initialization
   * is used to setup and load any requirements required for the module to function. Such
   * as downloading language files or configs.
   *
   * @return name of the method in the class that are designated to "initialize" the
   * module.
   */
  String initalizeMethod() default "initSetup";

  /**
   * This "String" or method is to be used to finalize this module, This finalize is used
   * to load any inter-module interactions or complete any required caching before the
   * server has finished starting.
   *
   * @return name of the method in the class that are designated to "finalize" the module.
   */
  String completeSetup() default "finalizeModule";

}
