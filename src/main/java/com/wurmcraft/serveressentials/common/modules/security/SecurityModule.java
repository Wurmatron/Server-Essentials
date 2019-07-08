package com.wurmcraft.serveressentials.common.modules.security;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.security.event.SecurityEvents;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

@Module(name = "Security")
public class SecurityModule {

  public static List<UUID> trustedUsers;

  public void setup() {
    loadTrustedUsers();
    MinecraftForge.EVENT_BUS.register(new SecurityEvents());
  }

  private static void loadTrustedUsers() {
    trustedUsers = new ArrayList<>();
    if (ConfigHandler.trustedUsers != null && !ConfigHandler.trustedUsers.isEmpty()) {
      if (ConfigHandler.trustedUsers.startsWith("http")
          || ConfigHandler.trustedUsers.startsWith("https")) {
        try {
          URL url = new URL(ConfigHandler.trustedUsers);
          Scanner reader = new Scanner(url.openStream());
          while (reader.hasNext()) {
            try {
              trustedUsers.add(UUID.fromString(reader.next()));
            } catch (IllegalArgumentException e) {
              ServerEssentialsServer.LOGGER.error(e.getMessage());
            }
          }
        } catch (Exception e) {
          ServerEssentialsServer.LOGGER.error(
              "Unable to load '" + ConfigHandler.trustedUsers + "'");
        }
      }
    }
  }

  public static boolean isTrusted(EntityPlayer player) {
    if (ServerEssentialsAPI.isModuleLoaded("Security")) {
      return trustedUsers.contains(player.getGameProfile().getId());
    } else {
      return FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getPlayerList()
          .canSendCommands(player.getGameProfile());
    }
  }

  public static List<String> getPlayerMods(EntityPlayer player) {
    EntityPlayerMP playerMP = (EntityPlayerMP) player;
    NetworkDispatcher network = NetworkDispatcher.get(playerMP.connection.netManager);
    return new ArrayList<>(network.getModList().keySet());
  }
}
