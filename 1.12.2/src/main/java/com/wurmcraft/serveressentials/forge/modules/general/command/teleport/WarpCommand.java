package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.Warp;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Warp")
public class WarpCommand {

  public static final String[] NAME_BLACKLIST = new String[]{"list", "set"};

  @Command(inputArguments = {})
  public void listWarps(ICommandSender sender) {
    generalWarp(sender, "list");
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"List"})
  public void generalWarp(ICommandSender sender, String arg) {
    if (arg.equalsIgnoreCase("list")) {
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(sender), "general.warp.list") || !SERegistry
          .isModuleLoaded("Rank")) {
        for (Warp w : SECore.dataHandler.getDataFromKey(DataKey.WARP, new Warp())
            .values()) {
          if (SERegistry.isModuleLoaded("Rank") && RankUtils
              .hasPermission(RankUtils.getRank(sender), "general.warp." + w.name)
              || !SERegistry
              .isModuleLoaded("Rank")) {
            sender.sendMessage(TextComponentUtils.addClickCommand(TextComponentUtils
                    .addPosition(new TextComponentString(COMMAND_COLOR + w.name), w),
                "/warp " + w.name));
          }
        }
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    } else {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        for (Warp w : SECore.dataHandler.getDataFromKey(DataKey.WARP, new Warp())
            .values()) {
          if (w.name.equalsIgnoreCase(arg)) {
            if (SERegistry.isModuleLoaded("Rank") && RankUtils
                .hasPermission(RankUtils.getRank(sender), "general.warp." + w.name)
                || !SERegistry
                .isModuleLoaded("Rank")) {
              TeleportUtils.teleportTo(player, w);
              player.sendMessage(TextComponentUtils.addPosition(new TextComponentString(
                  COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_WARP
                      .replaceAll("%NAME%", COMMAND_INFO_COLOR + w.name)), w));
              return;
            } else {
              sender.sendMessage(new TextComponentString(
                  ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
            }
          }
        }
        player.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_WARP_NOT_FOUND
                .replaceAll("%NAME%", COMMAND_INFO_COLOR + arg)));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.STRING}, inputNames = {"Set", "Name"})
  public void setWarp(ICommandSender sender, String arg, String warpName) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.warp.create") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (arg.equalsIgnoreCase("set") || arg.equalsIgnoreCase("create")) {
          Warp warp = new Warp(warpName, player.posX, player.posY, player.posZ,
              player.dimension);
          SERegistry.register(DataKey.WARP, warp);
          sender.sendMessage(
              TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
                  PlayerUtils.getUserLanguage(player).GENERAL_WARP_CREATE
                      .replaceAll("%NAME%", COMMAND_INFO_COLOR + warp.name)), warp));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
