package com.wurmcraft.serveressentials.core.registry.classpath;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;

@Module(name = "TestC", moduleDependencies = {"TestA"})
public class TestModuleC {

  public void initSetup() {
    SECore.logger.info("Module TestC is initializing!");
  }

  public void finalizeModule() {
    SECore.logger.info("Module TestC is finalizing!");
  }
}
