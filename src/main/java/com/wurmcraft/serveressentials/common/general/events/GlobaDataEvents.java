package com.wurmcraft.serveressentials.common.general.events;

import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class GlobaDataEvents {

  @SubscribeEvent
  public void onJoin(PlayerEvent.PlayerLoggedInEvent e) {
    for (String motdLine : DataHelper.globalSettings.getMotd()) {
      e.player.sendMessage(new TextComponentString(motdLine.replaceAll("&", "\u00A7")));
    }
  }
}
