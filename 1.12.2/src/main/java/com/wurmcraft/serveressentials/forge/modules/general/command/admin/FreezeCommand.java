package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.general.event.GeneralEvents;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Freeze", aliases = {"Bubble", "Pause"})
public class FreezeCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = "Player")
  public void freezePlayer(ICommandSender sender, EntityPlayer player) {
    if (GeneralEvents.isFrozen(player)) {
      GeneralEvents.removeFrozen(player);
      sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_FREEZE_OTHER
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())), player));
      player.sendMessage(
          new TextComponentString(PlayerUtils.getUserLanguage(player).GENERAL_FREEZE));
    } else {
      GeneralEvents.addFrozen(player, player.getPosition());
      sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_FREEZE_UNDO_OTHER
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())), player));
      player.sendMessage(new TextComponentString(
          PlayerUtils.getUserLanguage(player).GENERAL_FREEZE_UNDO));
    }
  }

}
