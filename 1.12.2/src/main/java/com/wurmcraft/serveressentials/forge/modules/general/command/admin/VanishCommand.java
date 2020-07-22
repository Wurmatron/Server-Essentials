package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.general.event.GeneralEvents;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Vanish", aliases = {"V"})
public class VanishCommand {

  @Command(inputArguments = {})
  public void vanish(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.vanish") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (GeneralEvents.vanishedPlayers.contains(player)) {
          player.sendMessage(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_VANISH_UNVANISH));
          PlayerUtils.updateVanishStatus(player, true);
          GeneralEvents.vanishedPlayers.remove(player);
        } else {
          player.sendMessage(
              new TextComponentString(COMMAND_COLOR +
                  PlayerUtils.getUserLanguage(player).GENERAL_VANISH));
          PlayerUtils.updateVanishStatus(player, false);
          GeneralEvents.vanishedPlayers.add(player);
        }
      }
    }      sender.sendMessage(new TextComponentString(
        ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
  }
}
