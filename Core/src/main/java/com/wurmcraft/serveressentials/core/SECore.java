package com.wurmcraft.serveressentials.core;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

public class SECore {

  // TODO Config value
  public static final ScheduledExecutorService EXECUTORS = new ScheduledThreadPoolExecutor(
      2);
  public static Logger logger = Logger.getLogger(Global.NAME);

}
