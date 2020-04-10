package com.wurmcraft.serveressentials.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.data.BasicDataHandler;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

public class SECore {

  // TODO Config value
  public static final ScheduledExecutorService EXECUTORS = new ScheduledThreadPoolExecutor(2);
  public static IDataHandler dataHandler = new BasicDataHandler();

  public static Logger logger = Logger.getLogger(Global.NAME);
  public static File SAVE_DIR = new File("" + Global.NAME.replaceAll(" ", "_"));
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
