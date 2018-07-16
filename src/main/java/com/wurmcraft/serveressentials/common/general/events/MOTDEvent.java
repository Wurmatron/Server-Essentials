package com.wurmcraft.serveressentials.common.general.events;

import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MOTDEvent {

  @SubscribeEvent
  public void onServerPing(TickEvent.ServerTickEvent e) {
    if (DataHelper.globalSettings.getGlobalMOTD() != null && DataHelper.globalSettings.getGlobalMOTD().length > 0) {
      MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
      StringBuilder builder = new StringBuilder();
      for (String txt : DataHelper.globalSettings.getGlobalMOTD()) {
        builder.append(txt.replaceAll("&", "\u00A7") + "\n");
      }
      server
          .getServerStatusResponse()
          .setServerDescription(new TextComponentString(builder.toString()));
    }
  }
}
