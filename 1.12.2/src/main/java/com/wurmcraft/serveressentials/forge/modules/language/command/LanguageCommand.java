package com.wurmcraft.serveressentials.forge.modules.language.command;

import static com.wurmcraft.serveressentials.core.SECore.GSON;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.URLUtils;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.core.api.module.config.LanguageConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.io.IOException;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Language", name = "Language", aliases = {"Lang"})
public class LanguageCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Language-Key"})
  public void changeLanguage(ICommandSender sender, String langKey) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "language.lang.change") || !SERegistry
        .isModuleLoaded("Rank")) {
      Language lang = null;
      try {
        lang = (Language) SERegistry.getStoredData(DataKey.LANGUAGE, langKey);

      } catch (NoSuchElementException e) {
        try {
          lang = GSON.fromJson(URLUtils.readStringFromURL(((LanguageConfig) SERegistry
              .getStoredData(DataKey.MODULE_CONFIG, "Language")).languageLocation
              + langKey
              + ".json"), Language.class);
        } catch (IOException f) {
          sender.sendMessage(new TextComponentString(
              ERROR_COLOR + PlayerUtils.getUserLanguage(sender).LANGUAGE_LANGUAGE_NO_FOUND
                  .replaceAll("%LANG%", COMMAND_INFO_COLOR + langKey)));
          return;
        }
      }
      SERegistry.register(DataKey.LANGUAGE, lang);
      if (sender != null && sender.getCommandSenderEntity() != null && sender
          .getCommandSenderEntity() instanceof EntityPlayer) {
        StoredPlayer playerData = (StoredPlayer) SERegistry.getStoredData(DataKey.PLAYER,
            ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId()
                .toString());
        playerData.global.language = lang.langKey;
        PlayerDataEvents.savePlayer(((EntityPlayer) sender.getCommandSenderEntity()));
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).LANGUAGE_LANGUAGE_SET
                .replaceAll("%LANG%", COMMAND_INFO_COLOR + lang.langKey)));
      } else if (sender != null) {
        LanguageConfig config = (LanguageConfig) SERegistry
            .getStoredData(DataKey.MODULE_CONFIG, "Language");
        config.defaultLang = lang.langKey;
        SECore.dataHandler.registerData(DataKey.MODULE_CONFIG, config);
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).LANGUAGE_LANGUAGE_SET
                .replaceAll("%LANG%", COMMAND_INFO_COLOR + lang.langKey)));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
