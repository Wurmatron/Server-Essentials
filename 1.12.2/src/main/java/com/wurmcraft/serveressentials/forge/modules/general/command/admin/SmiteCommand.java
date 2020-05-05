package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Smite")
public class SmiteCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void smitePlayer(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.smite") || !SERegistry
        .isModuleLoaded("Rank")) {
      player.getEntityWorld().addWeatherEffect(
          new EntityLightningBolt(player.world, player.posX, player.posY, player.posZ,
              false));
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SMITE
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }


}
