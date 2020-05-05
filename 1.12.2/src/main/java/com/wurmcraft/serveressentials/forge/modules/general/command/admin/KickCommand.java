package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Kick")
public class KickCommand {

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.STRING_ARR}, inputNames = {"Player", "Msg"})
  public void kickPlayer(ICommandSender sender, EntityPlayer player, String[] msg) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tpahere") || !SERegistry
        .isModuleLoaded("Rank")) {
      ((EntityPlayerMP) player).connection.disconnect(new TextComponentString(
          Strings.join(msg, " ").replaceAll("&", "\u00a7")));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
