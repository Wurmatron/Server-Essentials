package com.wurmcraft.serveressentials.common.modules.ban;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.GlobalBan;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Module(name = "Ban")
public class BanModule {

  public void setup() {}

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    checkPlayer((EntityPlayerMP) e.player);
  }

  public static void checkPlayer(EntityPlayerMP player) {
    ServerEssentialsServer.instance.executors.schedule(
        () -> {
          GlobalBan ban = RequestGenerator.Ban.getBan(player.getGameProfile().getId().toString());
          if (ban != null) {
            ServerEssentialsServer.LOGGER.info(
                player.getGameProfile().getId().toString()
                    + " tried to join but is banned for "
                    + ban.reason);
            player.connection.disconnect(
                new TextComponentString(
                    TextFormatting.RED + " You have been banned for:" + ban.reason));
          }
        },
        0,
        TimeUnit.SECONDS);
  }
}
