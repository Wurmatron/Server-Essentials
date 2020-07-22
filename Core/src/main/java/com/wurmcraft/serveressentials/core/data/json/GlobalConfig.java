package com.wurmcraft.serveressentials.core.data.json;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.json.Validation;
import com.wurmcraft.serveressentials.core.data.BasicDataHandler;
import com.wurmcraft.serveressentials.core.data.FileDataHandler;
import com.wurmcraft.serveressentials.core.data.RestDataHandler;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

public class GlobalConfig implements StoredDataType {

  // General
  public boolean debug;
  public String[] enabledModules;
  public String dataStorgeType;
  public String serverID;
  public boolean logCommandToCMD;

  // Performance / Timings
  public int threadPoolSize;
  public int tempDataRemovalTime;

  // Rest Only
  public String restAuth;
  public String restURL;
  public String modpackVersion;

  public GlobalConfig() {
    this.debug = false;
    threadPoolSize = 2;
    enabledModules = new String[] {"General", "Language", "Rank"};
    dataStorgeType = "File";
    this.restAuth = "user:password";
    this.restURL = "https://rest.xxxx.com/";
    this.tempDataRemovalTime = 5;
    this.serverID = "Default";
    this.modpackVersion = "No Idea";
    logCommandToCMD = true;
  }

  public GlobalConfig(
      boolean debug,
      String[] enabledModules,
      String dataStorgeType,
      String serverID,
      boolean logCommandToCMD,
      int threadPoolSize,
      int tempDataRemovalTime,
      String restAuth,
      String restURL,
      String modpackVersion) {
    this.debug = debug;
    this.enabledModules = enabledModules;
    this.dataStorgeType = dataStorgeType;
    this.serverID = serverID;
    this.logCommandToCMD = logCommandToCMD;
    this.threadPoolSize = threadPoolSize;
    this.tempDataRemovalTime = tempDataRemovalTime;
    this.restAuth = restAuth;
    this.restURL = restURL;
    this.modpackVersion = modpackVersion;
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
    } else if (name.equalsIgnoreCase("Rest")) {
      try {
        Validation restValidate = RestRequestGenerator.Verify.get();
        if (restValidate == null) {
          SECore.logger.info("Defaulting to 'file' due to invalid rest configuration!!");
          return getDataHandler("File");
        } else {
          SECore.logger.info("Rest Version is '" + restValidate.version + "'");
        }
      } catch (Exception e) {
        e.printStackTrace();
        SECore.logger.info("Defaulting to 'file' due to invalid rest configuration!");
        return getDataHandler("File");
      }
      return new RestDataHandler();
    }
    return new BasicDataHandler();
  }
}
