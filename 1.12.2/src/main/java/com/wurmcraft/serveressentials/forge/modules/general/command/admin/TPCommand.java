package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;


@ModuleCommand(moduleName = "General", name = "Tp", aliases = {"Teleport"})
public class TPCommand {

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.INTEGER,
      CommandArguments.INTEGER, CommandArguments.INTEGER,
      CommandArguments.INTEGER}, inputNames = {"Player", "X", "Y", "Z", "Dim"})
  public void teleportToCords(ICommandSender sender, EntityPlayer player, int x, int y,
      int z, int dim) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tp.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      teleportToCords(player, x, y, z, dim);
      sender.sendMessage(
          TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                  PlayerUtils.getUserLanguage(sender).GENREAL_TP_PLAYER_CORDS_OTHER
                      .replaceAll("%PLAYER%",
                          COMMAND_INFO_COLOR + player.getDisplayNameString())
                      .replaceAll("%POS%",
                          COMMAND_INFO_COLOR + x + ", " + y + ", " + z + ", " + dim)),
              player));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER, CommandArguments.INTEGER,
      CommandArguments.INTEGER, CommandArguments.INTEGER}, inputNames = {"Player", "X",
      "Y", "Z", "Dim"})
  public void teleportToCords(ICommandSender sender, int x, int y, int z, int dim) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      LocationWrapper pos = new LocationWrapper(x, y, z, dim);
      TeleportUtils.teleportTo((EntityPlayer) sender.getCommandSenderEntity(), pos);
      sender.sendMessage(TextComponentUtils.addPosition(new TextComponentString(
          PlayerUtils.getUserLanguage(sender).GENERAL_TP_CORDS.replaceAll("%POS%",
              COMMAND_INFO_COLOR + +x + ", " + y + ", " + z + ", " + dim)), pos));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.INTEGER,
      CommandArguments.INTEGER, CommandArguments.INTEGER,
      CommandArguments.INTEGER}, inputNames = {"Player", "X", "Y", "Z"})
  public void teleportToCords(ICommandSender sender, EntityPlayer player, int x, int y,
      int z) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tp.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      teleportToCords(player, x, y, z, player.dimension);
      sender.sendMessage(
          TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(sender).GENREAL_TP_PLAYER_CORDS_OTHER
                  .replaceAll("%PLAYER%",
                      COMMAND_INFO_COLOR + player.getDisplayNameString())
                  .replaceAll("%POS%",
                      COMMAND_INFO_COLOR + x + ", " + y + ", " + z)), player));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER, CommandArguments.INTEGER,
      CommandArguments.INTEGER}, inputNames = {"Player", "X", "Y", "Z"})
  public void teleportToCords(ICommandSender sender, int x, int y, int z) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      LocationWrapper pos = new LocationWrapper(x, y, z,
          ((EntityPlayer) sender.getCommandSenderEntity()).dimension);
      TeleportUtils.teleportTo((EntityPlayer) sender.getCommandSenderEntity(), pos);
      sender.sendMessage(TextComponentUtils.addPosition(new TextComponentString(
          PlayerUtils.getUserLanguage(sender).GENERAL_TP_CORDS.replaceAll("%POS%",
              COMMAND_INFO_COLOR + +x + ", " + y + ", " + z)), pos));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.INTEGER,
      CommandArguments.INTEGER})
  public void teleportNoY(ICommandSender sender, EntityPlayer player, int x, int z) {
    teleportToCords(sender, player, x,
        player.world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(), z);
  }

  @Command(inputArguments = {CommandArguments.INTEGER,
      CommandArguments.INTEGER})
  public void teleportNoY(ICommandSender sender, int x, int z) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      teleportToCords(sender, x, ((EntityPlayer) sender.getCommandSenderEntity()).world
          .getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(), z);
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.PLAYER}, inputNames = {"Teleporting Player", "Player"})
  public void teleportToPlayer(ICommandSender sender, EntityPlayer teleport,
      EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tp.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      teleportToPlayer(teleport, player);
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_TP
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + teleport.getDisplayNameString())
              .replaceAll("%PLAYER2%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void teleportToPlayer(ICommandSender sender, EntityPlayer player) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      TeleportUtils.teleportTo((EntityPlayer) sender.getCommandSenderEntity(),
          new LocationWrapper(player.posX, player.posY, player.posZ, player.dimension));
      sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_TP_OTHER
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())), player));
    }
  }
}
