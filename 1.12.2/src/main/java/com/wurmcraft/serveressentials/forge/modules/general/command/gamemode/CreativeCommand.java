package com.wurmcraft.serveressentials.forge.modules.general.command.gamemode;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;

@ModuleCommand(moduleName = "General", name = "Creative")
public class CreativeCommand {

  @Command(inputArguments = {})
  public void changeToSurvival(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(sender), "general.gamemode.creative") || !SERegistry
          .isModuleLoaded("Rank")) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        player.setGameType(GameType.CREATIVE);
        player.sendMessage(new TextComponentString(COMMAND_COLOR +
            PlayerUtils.getUserLanguage(player).GENERAL_GAMEMODE_CHANGED
                .replaceAll("%MODE%", COMMAND_INFO_COLOR + GameType.CREATIVE.name())));
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }
}
