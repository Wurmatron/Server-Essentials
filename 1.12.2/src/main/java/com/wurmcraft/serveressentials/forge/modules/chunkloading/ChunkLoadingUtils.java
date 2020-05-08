package com.wurmcraft.serveressentials.forge.modules.chunkloading;

import com.wurmcraft.serveressentials.core.data.json.ServerChunkData;
import com.wurmcraft.serveressentials.core.data.json.ServerChunkData.ChunkPos;
import com.wurmcraft.serveressentials.core.data.json.ServerChunkData.PlayerChunkData;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class ChunkLoadingUtils {

  public static NonBlockingHashMap<String, Ticket> playerTicketStorage = new NonBlockingHashMap<>();
  public static ServerChunkData loadedData;

  public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors
      .newScheduledThreadPool(1);

  public static void updateTickets(boolean needsUpdate) {
    try {
    // Remove all old tickets
    for (PlayerChunkData player : loadedData.playerChunks) {
      Ticket playerTicket = playerTicketStorage.get(player.uuid);
      if (playerTicket != null) {
        ForgeChunkManager.releaseTicket(playerTicket);
      }
    }
    playerTicketStorage.clear();
    // Get Updated ChunkLoading List
    if (needsUpdate) {
      loadedData = RestRequestGenerator.ChunkLoading.getLoadedChunks();
    }
    // Recreate and apply chunkloading
    for (PlayerChunkData player : loadedData.playerChunks) {
      Ticket ticket = ForgeChunkManager
          .requestPlayerTicket(ServerEssentialsServer.INSTANCE, player.uuid,
              FMLCommonHandler.instance().getMinecraftServerInstance()
                  .getEntityWorld(), Type.NORMAL);
      for (ChunkPos p : player.pos) {
        ForgeChunkManager
            .forceChunk(ticket, new net.minecraft.util.math.ChunkPos(p.x, p.z));
      }
      playerTicketStorage.put(player.uuid, ticket);
    }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
