package com.wurmcraft.serveressentials.core.data.json;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.data.BasicDataHandler;
import com.wurmcraft.serveressentials.core.data.FileDataHandler;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

public class GlobalConfig implements StoredDataType {

  // General
  public boolean debug;
  public String[] enabledModules;
  public String dataStorgeType;

  // Performance / Timings
  public int threadPoolSize;

  public GlobalConfig() {
    this.debug = false;
    threadPoolSize = 2;
    enabledModules = new String[0]; // TODO Set Default Modules
    dataStorgeType = "File";
  }

  public GlobalConfig(
      boolean debug, String[] enabledModules, String dataStorgeType, int threadPoolSize) {
    this.debug = debug;
    this.enabledModules = enabledModules;
    this.dataStorgeType = dataStorgeType;
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
    SECore.dataHandler = getDataHandler(SERegistry.globalConfig.dataStorgeType);

    // Performance / Timings
    SECore.executors = new ScheduledThreadPoolExecutor(SERegistry.globalConfig.threadPoolSize);
    SECore.logger.info("Max Threads set to " + SERegistry.globalConfig.threadPoolSize);
  }

  private static IDataHandler getDataHandler(String name) {
    if (name.equalsIgnoreCase("File")) {
      return new FileDataHandler();
    }
    return new BasicDataHandler();
  }
}
