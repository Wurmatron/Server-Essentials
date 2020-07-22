package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Home", aliases = "H")
public class HomeCommand {

  @Command(inputArguments = {})
  public void defaultHome(ICommandSender sender) {
    home(sender, ((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).defaultHome);
  }

  @Command(inputArguments = {CommandArguments.STRING})
  public void home(ICommandSender sender, String home) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.home") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() != null && sender
          .getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (home.equalsIgnoreCase("list")) {
          try {
            StoredPlayer playerData = (StoredPlayer) SERegistry
                .getStoredData(DataKey.PLAYER,
                    player.getGameProfile().getId().toString());
            for (Home h : playerData.server.homes) {
              sender.sendMessage(TextComponentUtils.addPosition(TextComponentUtils
                  .addClickCommand(new TextComponentString(COMMAND_COLOR + h.name),
                      "/home " + h.name), h));
            }
            if (playerData.server.homes.length == 0) {
              sender.sendMessage(new TextComponentString(COMMAND_COLOR +
                  PlayerUtils.getUserLanguage(sender).GENERAL_HOME_NOT_FOUND
                      .replaceAll("%HOME%", ((GeneralConfig) SERegistry
                          .getStoredData(DataKey.MODULE_CONFIG,
                              "General")).defaultHome)));
            }
          } catch (NoSuchElementException e) {

            sender.sendMessage(new TextComponentString(ERROR_COLOR + "[]"));
          }
        } else {
          try {
            StoredPlayer playerData = (StoredPlayer) SERegistry
                .getStoredData(DataKey.PLAYER,
                    player.getGameProfile().getId().toString());
            for (Home h : playerData.server.homes) {
              if (h.name.equals(home)) {
                TeleportUtils.teleportTo(player, h);
                sender.sendMessage(
                    TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
                        PlayerUtils.getUserLanguage(sender).GENERAL_HOME_TELEPORT
                            .replaceAll("%HOME%", home)), h));
                return;
              }
            }
            for (Home h : playerData.server.homes) {
              if (h.name.equalsIgnoreCase(home)) {
                TeleportUtils.teleportTo(player, h);
                sender.sendMessage(TextComponentUtils.addPosition(new TextComponentString(
                    COMMAND_COLOR + PlayerUtils
                        .getUserLanguage(sender).GENERAL_HOME_TELEPORT
                        .replaceAll("%HOME%", home)), h));
                return;
              }
            }
          } catch (NoSuchElementException e) {
          }
          sender.sendMessage(new TextComponentString(
              ERROR_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_HOME_NOT_FOUND
                  .replaceAll("%HOME%", home)));
        }
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER})
  public void home(ICommandSender sender, int home) {
    home(sender, home + "");
  }

}
