package com.wurmcraft.serveressentials.forge.modules.autorank.command;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.common.utils.ChatHelper;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

@ModuleCommand(moduleName = "AutoRank", name = "AutoRank", aliases = {"AR"})
public class ARCommand {

  @Command(inputArguments = {})
  public void arBasic(ICommandSender sender) {
    check(sender, "check");
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"check"})
  public void check(ICommandSender sender, String arg) {
    if (arg.equalsIgnoreCase("check")) {
      Language senderLanguage = PlayerUtils.getUserLanguage(sender);
      if (RankUtils.hasPermission(sender, "autorank.autorank.check")) {
        ChatHelper.sendSpacerWithMessage(sender, senderLanguage.SPACER, "AutoRank");
      } else {
        ChatHelper.sendMessage(sender, senderLanguage.ERROR_NO_PERMS);
      }
    } else {
      ChatHelper.sendMessage(sender, "&6/ar &dcheck &b<username>");
    }
  }
}
