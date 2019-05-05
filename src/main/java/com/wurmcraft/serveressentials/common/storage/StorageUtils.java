package com.wurmcraft.serveressentials.common.storage;

import com.wurmcraft.serveressentials.api.storage.Storage;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.storage.file.FileStorage;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.storage.rest.RestStorage;

public class StorageUtils {

  public static String getActiveStorageType() {
    return isRest() ? "Rest" : "File";
  }

  public static Storage setupStorage() {
    if (getActiveStorageType().equalsIgnoreCase("Rest")) {
      return new RestStorage();
    } else if (getActiveStorageType().equalsIgnoreCase("File")) {
      return new FileStorage();
    }
    return null;
  }

  private static boolean isRest() {
    return ConfigHandler.restAuth.length() > 0
        && ConfigHandler.restURL.length() > 0
        && RequestGenerator.Status.getValidation() != null;
  }
}
