package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;


import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.module.config.GeneralConfig;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "SetHome")
public class SetHomeCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Home"})
  public void setHomeSpecific(ICommandSender sender, String home) {
    if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      StoredPlayer playerData = PlayerUtils.getPlayer(player);
      int maxHomes = PlayerUtils.getMaxHomes(player);
      Home newHome = new Home(home,player.posX, player.posY, player.posZ, player.dimension);
      if(PlayerUtils.setHome(player, newHome)) {
        sender.sendMessage(new TextComponentString(ERROR_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SETHOME_SET.replaceAll("%HOME%", home)));
      } else if(maxHomes <= playerData.server.homes.length) {
        sender.sendMessage(new TextComponentString(ERROR_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SETHOME_MAX));
      } else {
        sender.sendMessage(new TextComponentString(ERROR_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SETHOME_INVALID));
      }
    }
  }

  @Command(inputArguments = {})
  public void setHome(ICommandSender sender) {
    setHomeSpecific(sender,((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).defaultHome);
  }

  @Command(inputArguments = {CommandArguments.INTEGER})
  public void setHomeInteger(ICommandSender sender, int name) {
    setHomeSpecific(sender,name + "");
  }
}
