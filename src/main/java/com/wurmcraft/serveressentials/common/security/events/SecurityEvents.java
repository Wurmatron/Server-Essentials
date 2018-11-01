package com.wurmcraft.serveressentials.common.security.events;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.security.SecurityModule;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class SecurityEvents {


  @SubscribeEvent
  public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent e) {
    if (ConfigHandler.autoOP
        && SecurityModule.isTrustedMember(e.player)
        && !FMLCommonHandler.instance()
        .getMinecraftServerInstance()
        .getPlayerList()
        .canSendCommands(e.player.getGameProfile())) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getCommandManager()
          .executeCommand(
              FMLCommonHandler.instance().getMinecraftServerInstance(),
              "op " + UsernameCache.getLastKnownUsername(e.player.getGameProfile().getId()));
      ChatHelper.sendMessage(e.player,
          LanguageModule.getLangfromUUID(e.player.getGameProfile().getId()).TRUSTED_USER);
    }

    if (ConfigHandler.altCheck) {
      HashMap<SocketAddress, UUID> cache = new HashMap<>();
      for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance()
          .getPlayerList().getPlayers()) {
        if (cache.containsKey(player.connection.netManager.getRemoteAddress())) {
          ChatHelper.notifyStaff(LanguageModule.getLangFromKey(ConfigHandler.defaultLanguage).POSSIBLY_ALT);
        }
      }
    }
  }
}
