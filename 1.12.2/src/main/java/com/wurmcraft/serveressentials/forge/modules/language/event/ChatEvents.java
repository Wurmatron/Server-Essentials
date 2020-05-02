package com.wurmcraft.serveressentials.forge.modules.language.event;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEvents {

  @SubscribeEvent
  public void onChat(ServerChatEvent e) {
    e.setComponent(handleMessage(e.getPlayer(), e.getMessage()));
  }

  private static ITextComponent handleMessage(EntityPlayer player, String msg) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      Rank rank = null;
      if(playerData.global != null && playerData.global.rank != null) {
        rank = (Rank) SERegistry.getStoredData(DataKey.RANK, playerData.global.rank);
      }
      if(rank == null)
        rank = new Rank();
      return formatMessage(player, rank, msg);
    } catch (NoSuchElementException e) {
      return formatMessage(player, new Rank(), msg);
    }
  }

  public static ITextComponent formatMessage(EntityPlayer player, Rank rank, String msg) {
    return new TextComponentString(
        rank.getPrefix().replaceAll("&", "\u00a7") + " " + player.getName() + " " + rank
            .getSuffix().replaceAll("&", "\u00a7") + msg);
  }
}
