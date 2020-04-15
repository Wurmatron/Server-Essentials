package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import net.minecraft.command.ICommandSender;

public class PlayerUtils {

  public static Language getUserLanguage(String langKey) {
    return (Language) SERegistry.getStoredData(DataKey.LANGUAGE, langKey);
  }

  public static Language getUserLanguage(ICommandSender sender) {
    return (Language) SERegistry
        .getStoredData(DataKey.LANGUAGE, "en_us"); // TODO Load Default
  }
}
