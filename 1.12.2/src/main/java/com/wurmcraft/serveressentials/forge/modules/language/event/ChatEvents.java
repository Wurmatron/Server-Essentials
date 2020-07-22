package com.wurmcraft.serveressentials.forge.modules.language.event;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEvents {

  @SubscribeEvent
  public void onChat(ServerChatEvent e) {
    ITextComponent comp = handleMessage(e.getPlayer(), e.getMessage());
    if (comp != null) {
      e.setComponent(comp);
    } else {
      e.getPlayer().sendMessage(new TextComponentString(
          PlayerUtils.getUserLanguage(ERROR_COLOR + e.getPlayer()).ERROR_MUTED));
      e.setCanceled(true);
    }
  }

  private static ITextComponent handleMessage(EntityPlayer player, String msg) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      Rank rank = null;
      if (playerData.global != null && playerData.global.rank != null) {
        rank = (Rank) SERegistry.getStoredData(DataKey.RANK, playerData.global.rank);
        if (playerData.global.muted) {
          return null;
        }
      }
      if (rank == null) {
        rank = new Rank();
      }
      return formatMessage(player, handleUsername(player,playerData), rank, msg);
    } catch (NoSuchElementException e) {
      return formatMessage(player, player.getName(), new Rank(), msg);
    }
  }

  public static ITextComponent formatMessage(EntityPlayer player, String displayName,
      Rank rank, String msg) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(player), "language.chat.color") || !SERegistry
        .isModuleLoaded("Rank")) {
      return new TextComponentString(
          rank.getPrefix().replaceAll("&", "\u00a7") + " " + displayName + " \u00BB "
              + rank
              .getSuffix().replaceAll("&", "\u00a7") + msg.replaceAll("&", "\u00a7"));
    } else {
      return new TextComponentString(
          rank.getPrefix().replaceAll("&", "\u00a7") + " " + displayName + " \u00BB "
              + rank
              .getSuffix().replaceAll("&", "\u00a7") + msg);
    }
  }

  public static String handleUsername(EntityPlayer player, StoredPlayer playerData) {
    return (playerData.server.nick != null && !playerData.server.nick.isEmpty()) ?  "*" + playerData.server.nick.replaceAll("&", "\u00a7") : player.getName();
  }
}
