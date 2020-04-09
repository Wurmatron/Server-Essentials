package com.wurmcraft.serveressentials.core.registry.classpath.module;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;

@Module(name = "TestA")
public class TestModuleA {

  public void initSetup() {
    SECore.logger.info("Module TestA is initializing!");
  }

  public void finalizeModule() {
    SECore.logger.info("Module TestA is finalizing!");
  }
}
