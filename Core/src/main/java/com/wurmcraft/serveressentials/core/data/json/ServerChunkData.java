package com.wurmcraft.serveressentials.core.data.json;

public class ServerChunkData {

  public String serverID;
  public PlayerChunkData[] playerChunks;

  public ServerChunkData(String serverID, PlayerChunkData[] playerChunks) {
    this.serverID = serverID;
    this.playerChunks = playerChunks;
  }

  public static class PlayerChunkData {

    public String uuid;
    public ChunkPos[] pos;
    public double storedCurrency;
    public long setupTime;

    public PlayerChunkData(String uuid, ChunkPos[] pos, double storedCurrency, long setupTime) {
      this.uuid = uuid;
      this.pos = pos;
      this.storedCurrency = storedCurrency;
      this.setupTime = setupTime;
    }
  }

  public static class ChunkPos {

    public int x;
    public int z;

    public ChunkPos(int x, int z) {
      this.x = x;
      this.z = z;
    }
  }
}
