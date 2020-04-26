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
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "SetHome")
public class SetHomeCommand {

  public static List<String> INVALID_HOME_NAMES = Arrays.asList("list", "all");


  @Command(inputArguments = {})
  public void defaultHome(ICommandSender sender) {
    setHome(sender, ((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).defaultHome);
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Home"})
  public void setHome(ICommandSender sender, String home) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.sethome") || !SERegistry
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
          } catch (NoSuchElementException e) {
            sender.sendMessage(new TextComponentString(ERROR_COLOR + "[]"));
          }
        } else {
          Home playerHome = new Home(home, player.posX, player.posY, player.posZ,
              player.dimension);
          boolean setHome = PlayerUtils.setHome(player, playerHome);
          PlayerDataEvents.savePlayer(player); // Force Save Player
          if (INVALID_HOME_NAMES.contains(playerHome.name.toLowerCase())) {
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils
                    .getUserLanguage(sender).GENERAL_SETHOME_INVALID
                    .replaceAll("%HOME%", COMMAND_INFO_COLOR + home)));
            return;
          }
          if (setHome) {
            sender.sendMessage(TextComponentUtils.addPosition(new TextComponentString(
                    COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SETHOME_SET
                        .replaceAll("%HOME%", COMMAND_INFO_COLOR + playerHome.name)),
                playerHome));
          } else {
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SETHOME_MAX
                    .replaceAll("%AMOUNT%",
                        COMMAND_INFO_COLOR + PlayerUtils.getMaxHomes(player))));
          }
        }
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }
}
