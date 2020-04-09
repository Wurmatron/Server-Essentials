package com.wurmcraft.serveressentials.core.registry.classpath.module;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;

@Module(
    name = "TestE",
    moduleDependencies = {"TestF"})
public class TestModuleE {

  public void initSetup() {
    SECore.logger.info("Module TestE is initializing!");
  }

  public void finalizeModule() {
    SECore.logger.info("Module TestE is finalizing!");
  }
}
