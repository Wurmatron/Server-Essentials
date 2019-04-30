package com.wurmcraft.serveressentials.common.storage.file;

import com.wurmcraft.serveressentials.common.ConfigHandler;

public class DataHelper {

  public static String saveLocation;

  public DataHelper() {
    saveLocation = ConfigHandler.saveLocation;
  }
}
