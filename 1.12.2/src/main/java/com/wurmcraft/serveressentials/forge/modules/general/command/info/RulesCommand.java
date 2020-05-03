package com.wurmcraft.serveressentials.forge.modules.general.command.info;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Rules")
public class RulesCommand {

  @Command(inputArguments = {})
  public void displayRules(ICommandSender sender) {
    String[] rules = ((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).rules;
    if (rules != null && rules.length > 0) {
      for (String r : rules) {
        sender.sendMessage(
            new TextComponentString(COMMAND_COLOR + r.replaceAll("&", "\u00a7")));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_RULES_NONE));
    }
  }
}
