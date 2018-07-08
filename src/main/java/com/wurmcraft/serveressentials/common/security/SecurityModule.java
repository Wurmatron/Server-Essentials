package com.wurmcraft.serveressentials.common.security;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

@Module(name = "Security")
public class SecurityModule implements IModule {

  public static List<UUID> trusted = new ArrayList<>();

  @Override
  public void setup() {
    loadTrustedStaff();
  }

  public static List<String> getPlayerMods(EntityPlayer player) {
    EntityPlayerMP playerMP = (EntityPlayerMP) player;
    NetworkDispatcher network = NetworkDispatcher.get(playerMP.connection.netManager);
    return new ArrayList<>(network.getModList().keySet());
  }

  private static void loadTrustedStaff() {
    if (ConfigHandler.trustedStaff != null && ConfigHandler.trustedStaff.length() > 0) {
      if (trusted.size() > 0) {
        trusted.clear();
      }
      try {
        URL url = new URL(ConfigHandler.trustedStaff);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          try {
            UUID uuid = UUID.fromString(inputLine);
            trusted.add(uuid);
            ServerEssentialsServer.logger.debug(
                "\"" + uuid + "\" has been added to the trusted staff list");
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
          }
        }
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean isTrustedMember(EntityPlayer player) {
    if (trusted.size() > 0) {
      for (UUID uuid : trusted) {
        if (uuid.equals(player.getGameProfile().getId())) {
          return true;
        }
      }
    } else {
      loadTrustedStaff();
      for (UUID uuid : trusted) {
        if (uuid.equals(player.getGameProfile().getId())) {
          return true;
        }
      }
    }
    return false;
  }
}
