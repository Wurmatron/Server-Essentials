package com.wurmcraft.serveressentials.core.registry.classpath.module;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;

@Module(name = "TestB")
public class TestModuleB {

  public static final String someVar = "B";

  public void initSetup() {
    SECore.logger.info("Module TestB is initializing!");
  }

  public void finalizeModule() {
    SECore.logger.info("Module TestB is finalizing!");
  }
}
