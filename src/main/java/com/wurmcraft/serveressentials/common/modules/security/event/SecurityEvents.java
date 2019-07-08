package com.wurmcraft.serveressentials.common.modules.security.event;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.security.SecurityModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class SecurityEvents {

  @SubscribeEvent
  public void onJoinEvent(PlayerLoggedInEvent e) {
    if (ConfigHandler.autoOP
        && SecurityModule.isTrusted(e.player)
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
      ChatHelper.sendMessage(
          e.player, LanguageModule.getUserLanguage(e.player).local.SECURITY_TRUSTED);
    }
    if (ConfigHandler.checkForAlt) {
      HashMap<SocketAddress, UUID> cache = new HashMap<>();
      for (EntityPlayerMP player :
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
        if (cache.containsKey(player.connection.netManager.getRemoteAddress())) {
          ChatHelper.notifyStaff(
              LanguageModule.getUserLanguage(player)
                  .local
                  .SECURITY_ALT
                  .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                  .replaceAll(
                      Replacment.PLAYER2,
                      player.connection.netManager.getRemoteAddress().toString()));
        }
      }
    }
  }
}
