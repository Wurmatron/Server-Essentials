package com.wurmcraft.serveressentials.forge.modules.language.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static com.wurmcraft.serveressentials.forge.modules.language.LanguageUtils.lastMessageTracker;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Language", name = "Msg", aliases = {"Pm"})
public class MsgCommand {

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.STRING_ARR}, inputNames = {"Player", "Msg"})
  public void sendMessage(ICommandSender sender, EntityPlayer player, String[] msg) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "language.msg") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer sendingPlayer = (EntityPlayer) sender.getCommandSenderEntity();
        player.sendMessage(new TextComponentString(
            COMMAND_COLOR + sendingPlayer.getDisplayNameString() + "=> "
                + COMMAND_INFO_COLOR + Strings
                .join(msg, " ")));
        lastMessageTracker.put(player.getGameProfile().getId().toString(),
            sendingPlayer.getGameProfile().getId().toString());
        sendingPlayer.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).LANGUAGE_MSG));
        SECore.logger.info(COMMAND_COLOR + sendingPlayer.getDisplayNameString() + "=> "
            + COMMAND_INFO_COLOR + Strings
            .join(msg, " "));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
