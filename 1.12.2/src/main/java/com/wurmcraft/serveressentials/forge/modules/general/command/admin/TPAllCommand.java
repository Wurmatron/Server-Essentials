package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General", name = "TPAll", aliases = {"TpAll"})
public class TPAllCommand {

  @Command(inputArguments = {})
  public void tpAll(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tpall") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance()
            .getPlayerList().getPlayers()) {
          if (!p.getGameProfile().getId().toString()
              .equals(player.getGameProfile().getId().toString())) {
            TeleportUtils.teleportTo(p,
                new LocationWrapper(player.getPosition().getX(),
                    player.getPosition().getY(),
                    player.getPosition().getZ(), player.dimension));
            p.sendMessage(TextComponentUtils
                .addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                    PlayerUtils.getUserLanguage(p).GENERAL_TPALL
                        .replaceAll("%PLAYER%", COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)), player));
          }
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Rank"})
  public void tpAllRank(ICommandSender sender, String rankString) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tpall.rank") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (SERegistry.isModuleLoaded("Rank")) {
        try {
          Rank rank = (Rank) SERegistry.getStoredData(DataKey.RANK, rankString);
          if (rank != null) {
            if (sender != null && sender
                .getCommandSenderEntity() instanceof EntityPlayer) {
              EntityPlayer sendingPlayer = (EntityPlayer) sender.getCommandSenderEntity();
              for (EntityPlayer p : FMLCommonHandler.instance()
                  .getMinecraftServerInstance()
                  .getPlayerList().getPlayers()) {
                if (RankUtils.getRank(p).getID().equals(rank.getID())) {
                  TeleportUtils.teleportTo(p,
                      new LocationWrapper(sendingPlayer.getPosition().getX(),
                          sendingPlayer.getPosition().getY(),
                          sendingPlayer.getPosition().getZ(), sendingPlayer.dimension));
                  p.sendMessage(TextComponentUtils
                      .addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                          PlayerUtils.getUserLanguage(p).GENERAL_TPALL
                              .replaceAll("%PLAYER%",
                                  COMMAND_INFO_COLOR + sendingPlayer.getDisplayNameString() + COMMAND_COLOR)), sendingPlayer));
                }
              }
            }
          } else {
            sender.sendMessage(new TextComponentString(ERROR_COLOR +
                PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND.replaceAll("%RANK%",COMMAND_INFO_COLOR +  rankString + COMMAND_COLOR)));
          }
        } catch (NoSuchElementException e) {
          sender.sendMessage(new TextComponentString(
              PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }

  }

}
