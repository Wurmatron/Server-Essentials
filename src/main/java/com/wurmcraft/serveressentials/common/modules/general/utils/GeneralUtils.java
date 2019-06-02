package com.wurmcraft.serveressentials.common.modules.general.utils;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.io.File;
import java.util.*;
import java.util.ArrayList;

public class GeneralUtils {

  public static void setMotdLine(int lineNo, String line) {
    List<String> currentMotd = new ArrayList<>();
    if (GeneralModule.config.motd != null || GeneralModule.config.motd.length > 0) {
      Collections.addAll(currentMotd, GeneralModule.config.motd);
    }
    try {
      currentMotd.set(lineNo, line);
      GeneralModule.config.motd = currentMotd.toArray(new String[0]);
      DataHelper.save(new File(ConfigHandler.saveLocation), GeneralModule.config);
    } catch (ArrayIndexOutOfBoundsException e) {
      currentMotd.add(line);
      GeneralModule.config.motd = currentMotd.toArray(new String[0]);
      DataHelper.save(new File(ConfigHandler.saveLocation), GeneralModule.config);
    }
  }
}
