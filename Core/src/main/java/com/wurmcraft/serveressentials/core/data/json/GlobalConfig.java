package com.wurmcraft.serveressentials.core.data.json;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.json.JsonParser;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

public class GlobalConfig implements JsonParser {

  // General
  public boolean debug;

  // Performance / Timings
  public int threadPoolSize;

  public GlobalConfig() {
    this.debug = false;
    threadPoolSize = 2;
  }

  public GlobalConfig(boolean debug) {
    this.debug = debug;
  }

  @Override
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
    SECore.executors = new ScheduledThreadPoolExecutor(SERegistry.globalConfig.threadPoolSize);
    SECore.logger.info("Max Threads set to " + SERegistry.globalConfig.threadPoolSize);
  }
}
