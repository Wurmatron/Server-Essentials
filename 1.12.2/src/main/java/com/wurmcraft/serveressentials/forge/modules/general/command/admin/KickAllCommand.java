package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General", name = "KickAll")
public class KickAllCommand {

  @Command(inputArguments = {})
  public void kickAll(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.kickall") || !SERegistry
        .isModuleLoaded("Rank")) {
      for (EntityPlayerMP player : FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getPlayerList().getPlayers()) {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayer)
            || !((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId()
            .toString().equals(player.getGameProfile().getId().toString())) {
          player.connection.disconnect(new TextComponentString(COMMAND_INFO_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_KICKALL_MSG));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.STRING})
  public void kickAll(ICommandSender sender, String msg) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.kickall") || !SERegistry
        .isModuleLoaded("Rank")) {
      for (EntityPlayerMP player : FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getPlayerList().getPlayers()) {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayer)
            || !((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId()
            .toString().equals(player.getGameProfile().getId().toString())) {
          player.connection.disconnect(new TextComponentString(
              COMMAND_INFO_COLOR + msg));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
