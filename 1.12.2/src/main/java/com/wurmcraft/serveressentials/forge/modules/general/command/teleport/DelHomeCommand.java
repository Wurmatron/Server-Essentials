package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "DelHome", aliases = {"DHome", "Dh"})
public class DelHomeCommand {

  @Command(inputArguments = {})
  public void delDefaultHome(ICommandSender sender) {
    delHome(sender, ((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).defaultHome);
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Home"})
  public void delHome(ICommandSender sender, String home) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.delhome") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() != null && sender
          .getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        try {
          StoredPlayer playerData = (StoredPlayer) SERegistry
              .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
          if (home.equalsIgnoreCase("all")) {
            playerData.server.homes = new Home[0];
            SERegistry.register(DataKey.PLAYER, playerData);
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_DELHOME_ALL));
          } else {
            for (Home h : playerData.server.homes) {
              if (h.name.equals(home)) {
                SERegistry
                    .register(DataKey.PLAYER, PlayerUtils.deleteHome(playerData, home));
                sender.sendMessage(
                    TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
                        PlayerUtils.getUserLanguage(sender).GENERAL_DELHOME_REMOVED
                            .replaceAll("%HOME%", COMMAND_INFO_COLOR + h.name)), h));
                return;
              }
            }
            for (Home h : playerData.server.homes) {
              if (h.name.equalsIgnoreCase(home)) {
                SERegistry
                    .register(DataKey.PLAYER, PlayerUtils.deleteHome(playerData, home));
                sender.sendMessage(
                    TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
                        PlayerUtils.getUserLanguage(sender).GENERAL_DELHOME_REMOVED
                            .replaceAll("%HOME%", COMMAND_INFO_COLOR + h.name)), h));
                return;
              }
            }
            sender.sendMessage(new TextComponentString(
                ERROR_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_HOME_NOT_FOUND
                    .replaceAll("%HOME%", COMMAND_INFO_COLOR + home)));
          }
        } catch (NoSuchElementException ignored) {
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
