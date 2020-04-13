package com.wurmcraft.serveressentials.core.data.json;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

public class GlobalConfig implements StoredDataType {

  // General
  public boolean debug;
  public String[] enabledModules;

  // Performance / Timings
  public int threadPoolSize;

  public GlobalConfig() {
    this.debug = false;
    threadPoolSize = 2;
    enabledModules = new String[0]; // TODO Set Default Modules
  }

  public GlobalConfig(boolean debug, String[] enabledModules, int threadPoolSize) {
    this.debug = debug;
    this.enabledModules = enabledModules;
    this.threadPoolSize = threadPoolSize;
  }

  public String getID() {
    return "Global";
  }

  public static void setValues() {
    // General
    if (SERegistry.globalConfig.debug) {
      SECore.logger.info("Debug Mode Enabled");
      SECore.logger.setLevel(Level.ALL);
    }

    // Performance / Timings
    SECore.executors = new ScheduledThreadPoolExecutor(
        SERegistry.globalConfig.threadPoolSize);
    SECore.logger.info("Max Threads set to " + SERegistry.globalConfig.threadPoolSize);
  }
}
