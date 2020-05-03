package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Top", aliases = {"T"})
public class TopCommand {

  @Command(inputArguments = {})
  public void selfTop(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      BlockPos topPos = player.getEntityWorld()
          .getTopSolidOrLiquidBlock(player.getPosition());
      LocationWrapper topLoc = new LocationWrapper(topPos.getX(), topPos.getY(),
          topPos.getZ(), player.dimension);
      TeleportUtils.teleportTo(player, topLoc);
      player.sendMessage(
          TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_TOP), topLoc));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = "Player")
  public void otherTop(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.top.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      selfTop(player);
      BlockPos topPos = player.getEntityWorld()
          .getTopSolidOrLiquidBlock(player.getPosition());
      LocationWrapper topLoc = new LocationWrapper(topPos.getX(), topPos.getY(),
          topPos.getZ(), player.dimension);
      player.sendMessage(
          TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_TOP_OTHER.replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())), topLoc));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
