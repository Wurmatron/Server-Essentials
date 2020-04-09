package com.wurmcraft.serveressentials.core.registry.classpath;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;

@Module(name = "TestD", moduleDependencies = {"TestC", "TestB"})
public class TestModuleD {

  public void initSetup() {
    SECore.logger.info("Module TestD is initializing!");
  }

  public void finalizeModule() {
    SECore.logger.info("Module TestD is finalizing!");
  }
}
