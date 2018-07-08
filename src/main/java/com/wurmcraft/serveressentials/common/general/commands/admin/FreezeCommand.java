package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.general.events.PlayerTickEvent;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class FreezeCommand extends SECommand {

  @Override
  public String getName() {
    return "freeze";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        PlayerTickEvent.toggleFrozen(player, player.getPosition());
        if (PlayerTickEvent.isFrozen(player)) {
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender)
                      .FROZEN_OTHER
                      .replaceAll("%PLAYER%", player.getDisplayNameString())));
          player.sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(player.getGameProfile().getId()).FROZEN));
        } else {
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender)
                      .UNFROZEN_OTHER
                      .replaceAll("%PLAYER%", player.getDisplayNameString())));
          player.sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(player.getGameProfile().getId()).UNFROZEN));
        }
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
