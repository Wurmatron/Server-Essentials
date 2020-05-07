package com.wurmcraft.serveressentials.forge.modules.chunkloading.data;

import net.minecraft.util.math.ChunkPos;

public class ServerChunkData {

  public String serverID;
  public PlayerChunkData[] playerChunks;

  public ServerChunkData(String serverID,
      PlayerChunkData[] playerChunks) {
    this.serverID = serverID;
    this.playerChunks = playerChunks;
  }

  public class PlayerChunkData {

    public String uuid;
    public ChunkPos pos;
    public double storedCurrency;
    public long setupTime;

    public PlayerChunkData(String uuid, ChunkPos pos, double storedCurrency,
        long setupTime) {
      this.uuid = uuid;
      this.pos = pos;
      this.storedCurrency = storedCurrency;
      this.setupTime = setupTime;
    }
  }
}
