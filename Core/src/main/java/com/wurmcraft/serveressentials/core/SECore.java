package com.wurmcraft.serveressentials.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.data.BasicDataHandler;
import com.wurmcraft.serveressentials.core.data.json.GlobalConfig;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class SECore {

  // Config
  public static ScheduledExecutorService executors;
  public static IDataHandler dataHandler = new BasicDataHandler();

  public static Logger logger = Logger.getLogger(Global.NAME);
  public static File SAVE_DIR = new File("" + Global.NAME.replaceAll(" ", "_"));
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public static void setup() {
    SERegistry.loadAndSetup();
    GlobalConfig.setValues();
  }
}
