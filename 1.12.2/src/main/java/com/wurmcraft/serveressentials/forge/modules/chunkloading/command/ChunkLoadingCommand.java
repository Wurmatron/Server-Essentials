package com.wurmcraft.serveressentials.forge.modules.chunkloading.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static net.minecraft.command.CommandBase.getEntityName;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.data.json.ServerChunkData;
import com.wurmcraft.serveressentials.core.data.json.ServerChunkData.ChunkPos;
import com.wurmcraft.serveressentials.core.data.json.ServerChunkData.PlayerChunkData;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.chunkloading.ChunkLoadingUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.time.Instant;
import java.util.*;
import java.util.Arrays;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "ChunkLoading", name = "ChunkLoading", aliases = {
    "ChunkLoad"})
public class ChunkLoadingCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {
      "Set, Delete, List, Balance"})
  public void loadChunk(ICommandSender sender, String arg) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(sender), "chunkloading.chunkloading")
          || !SERegistry
          .isModuleLoaded("Rank")) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        ChunkPos newPos = new ChunkPos((int) (player.posX / 16),
            (int) (player.posZ / 16));
        if (arg.equalsIgnoreCase("Set")) {
          try {
            ServerChunkData chunkData = RestRequestGenerator.ChunkLoading
                .getLoadedChunks();
            if (chunkData != null) {
              if (chunkData.playerChunks != null) {
                for (PlayerChunkData p : chunkData.playerChunks) {
                  if (p.uuid.equals(player.getGameProfile().getId().toString())) {
                    if (p.pos != null && p.pos.length > 0) { // Already has entries
                      p.pos = Arrays.copyOf(p.pos, p.pos.length + 1);
                      p.pos[p.pos.length - 1] = newPos;
                    } else { // No Entries
                      p.pos = new ChunkPos[]{newPos};
                    }
                    return;
                  }
                }
                PlayerChunkData data = new PlayerChunkData(
                    player.getGameProfile().getId().toString(), new ChunkPos[]{newPos}, 0,
                    Instant.now().getEpochSecond());
                chunkData.playerChunks = Arrays
                    .copyOf(chunkData.playerChunks, chunkData.playerChunks.length + 1);
                chunkData.playerChunks[chunkData.playerChunks.length - 1] = data;
              } else {  // No Chunk-Loading Exists currently
                PlayerChunkData data = new PlayerChunkData(
                    player.getGameProfile().getId().toString(), new ChunkPos[]{newPos}, 0,
                    Instant.now().getEpochSecond());
                chunkData.playerChunks = new PlayerChunkData[]{data};
              }
            } else {
              PlayerChunkData data = new PlayerChunkData(
                  player.getGameProfile().getId().toString(), new ChunkPos[]{newPos}, 0,
                  Instant.now().getEpochSecond());
              chunkData = new ServerChunkData(SERegistry.globalConfig.serverID,
                  new PlayerChunkData[]{data});
            }
            RestRequestGenerator.ChunkLoading.overrideLoadedChunks(chunkData);
            ChunkLoadingUtils.loadedData = chunkData;
            ChunkLoadingUtils.updateTickets(false);
            player.sendMessage(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).CHUNKLOADING_SET));
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else if (arg.equalsIgnoreCase("Del")) {
          ServerChunkData chunkData = RestRequestGenerator.ChunkLoading.getLoadedChunks();
          if (chunkData != null && chunkData.playerChunks != null
              && chunkData.playerChunks.length > 0) {
            for (PlayerChunkData d : chunkData.playerChunks) {
              if (d.uuid.equals(player.getGameProfile().getId().toString())) {
                List<ChunkPos> validPos = new ArrayList<>();
                for (ChunkPos p : d.pos) {
                  if (p.x != newPos.x && p.z != newPos.z) {
                    validPos.add(p);
                  }
                }
              }
            }
            RestRequestGenerator.ChunkLoading.overrideLoadedChunks(chunkData);
            ChunkLoadingUtils.loadedData = chunkData;
            ChunkLoadingUtils.updateTickets(false);
            player.sendMessage(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).CHUNKLOADING_DEL));
          }
        } else if (arg.equalsIgnoreCase("list")) {
          for (PlayerChunkData d : ChunkLoadingUtils.loadedData.playerChunks) {
            if (d.uuid.equals(player.getGameProfile().getId().toString())) {
              for (ChunkPos p : d.pos) {
                sender
                    .sendMessage(
                        new TextComponentString(COMMAND_COLOR + p.x + ", " + p.z));
              }
            }
          }
        } else if (arg.equalsIgnoreCase("Balance") || arg.equalsIgnoreCase("Bal") || arg
            .equalsIgnoreCase("B") || arg.equalsIgnoreCase("Money")) {
          ServerChunkData chunkData = RestRequestGenerator.ChunkLoading.getLoadedChunks();
          double bal = 0;
          if (chunkData != null && chunkData.playerChunks != null
              && chunkData.playerChunks.length > 0) {
            for (PlayerChunkData d : chunkData.playerChunks) {
              if (d.uuid.equals(player.getGameProfile().getId().toString())) {
                bal = d.storedCurrency;
              }
            }
          }
          player.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).CHUNKLOADING_BALANCE
                  .replaceAll("%BAL%", COMMAND_INFO_COLOR + bal + COMMAND_COLOR)));
        }
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.DOUBLE}, inputNames = {"add", "amount"})
  public void chunkLoadingAdd(ICommandSender sender, String arg, double add) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(sender), "chunkloading.chunkloading")
          || !SERegistry
          .isModuleLoaded("Rank")) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        StoredPlayer playerData = (StoredPlayer) SERegistry
            .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        if (arg.equalsIgnoreCase("add")) {
          if (EcoUtils.hasTheMoney(playerData.global.wallet, add)) {
            ServerChunkData chunkData = RestRequestGenerator.ChunkLoading
                .getLoadedChunks();
            for (PlayerChunkData d : chunkData.playerChunks) {
              if (d.uuid.equals(player.getGameProfile().getId().toString())) {
                playerData.global = (GlobalPlayer) RestRequestGenerator.User
                    .getPlayer(player.getGameProfile().getId().toString());
                EcoUtils.setCurrency(playerData.global.wallet,
                    EcoUtils.getCurrency(playerData.global.wallet) - add);
                RestRequestGenerator.User
                    .overridePlayer(playerData.uuid, playerData.global);
                d.storedCurrency = d.storedCurrency + add;
                RestRequestGenerator.ChunkLoading.overrideLoadedChunks(chunkData);
                ChunkLoadingUtils.loadedData = chunkData;
                ChunkLoadingUtils.updateTickets(false);
                player.sendMessage(new TextComponentString(
                    COMMAND_COLOR + PlayerUtils
                        .getUserLanguage(player).CHUNKLOADING_BALANCE
                        .replaceAll("%BAL%",
                            COMMAND_INFO_COLOR + d.storedCurrency + COMMAND_COLOR)));
              }
            }
          } else {
            sender.sendMessage(new TextComponentString(ERROR_COLOR +
                PlayerUtils.getUserLanguage(sender).ERROR_INSUFFICENT_FUNDS
                    .replaceAll("%AMOUNT%",
                        "" + EcoUtils.getCurrency(playerData.global.wallet))));
          }
        }
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.INTEGER})
  public void chunkLoadingAdd(ICommandSender sender, String arg, int add) {
    chunkLoadingAdd(sender, arg, (double) add);
  }
}

