package wurmcraft.serveressentials.common.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;

public class WorldGenHandler {

  private static Method writeChunkToNBT;
  private static ChunkProviderServer providerServer;
  private static WorldServer server;
  private static final String[] obNames = new String[]{"func_75820_a", "writeChunkToNBT"};
  private final int chunksPerCycle;
  private int totalChunks;
  private int minX;
  private int maxX;
  private int minZ;
  private int maxZ;
  private HashMap<int[], ArrayList<int[]>> queuedChunks = new HashMap<>();
  private int[] regionID;
  private int lastTPS;
  public boolean running;
  public boolean finished;
  private int estTotalChunks;

  public WorldGenHandler(ChunkProviderServer providerServer, WorldServer server,
      int chunksPerCycle) {
    Class<?>[] cArg = new Class[]{Chunk.class, World.class, NBTTagCompound.class};
    try {
      writeChunkToNBT = AnvilChunkLoader.class.getDeclaredMethod(obNames[0], cArg);
    } catch (NoSuchMethodException e) {
      try {
        writeChunkToNBT = AnvilChunkLoader.class.getDeclaredMethod(obNames[1], cArg);
      } catch (NoSuchMethodException e1) {
        throw new RuntimeException("Cannot find AnvilChunkLoader.writeChunkToNBT");
      }
    }
    writeChunkToNBT.setAccessible(true);
    WorldGenHandler.providerServer = providerServer;
    WorldGenHandler.server = server;
    this.chunksPerCycle = chunksPerCycle;
    minX = (int) providerServer.world.getWorldBorder().getCenterX() - (int) (
        providerServer.world.getWorldBorder().getDiameter() / 2);
    maxX = (int) providerServer.world.getWorldBorder().getCenterX() + (int) (
        providerServer.world.getWorldBorder().getDiameter() / 2);
    minZ = (int) providerServer.world.getWorldBorder().getCenterZ() - (int) (
        providerServer.world.getWorldBorder().getDiameter() / 2);
    maxZ = (int) providerServer.world.getWorldBorder().getCenterZ() + (int) (
        providerServer.world.getWorldBorder().getDiameter() / 2);
    regionID = new int[]{minX / 32, minZ / 32};
    minX /= 16;
    minZ /= 16;
    running = true;
    lastTPS = -1;
    estTotalChunks = (maxX - minX) / 16 * (maxZ - minZ) / 16;
  }

  private static void write(Chunk chunk) {
    if (writeChunkToNBT != null && chunk != null) {
      AnvilChunkLoader anvil = (AnvilChunkLoader) providerServer.chunkLoader;
      NBTTagCompound levelTag = new NBTTagCompound();
      NBTTagCompound chunkTag = new NBTTagCompound();
      chunkTag.setTag("Level", levelTag);
      try {
        writeChunkToNBT.invoke(anvil, chunk, server, levelTag);
        DataOutputStream dataoutputstream = RegionFileCache
            .getChunkOutputStream(server.getChunkSaveLocation(), chunk.x, chunk.z);
        CompressedStreamTools.write(chunkTag, dataoutputstream);
      } catch (IllegalAccessException | IllegalArgumentException | IOException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.getCause().printStackTrace();
      }
    }
  }

  private void genChunk(int x, int z) {
    if (!RegionFileCache.createOrLoadRegionFile(server.getChunkSaveLocation(), x, z)
        .chunkExists(x & 0x1F, z & 0x1F)) {
      Chunk chunk = providerServer.provideChunk(x, z);
      chunk.populate(providerServer, providerServer.chunkGenerator);
      write(chunk);
    }
    totalChunks++;
  }

  public void cycle(World world) {
    if (checkTPS() && running) {
      for (int c = 0; c < chunksPerCycle; c++) {
        nextChunk();
      }
    }
    if (world.getWorldTime() % 100 == 0) {
      if (running) {
        notifyPlayers(0);
      } else if (!finished) {
        notifyPlayers(1);
        finished = true;
      }
    }
  }

  private void nextChunk() {
    if (queuedChunks.size() > 0 && running && queuedChunks.get(regionID) != null
        && queuedChunks.get(regionID).size() > 0) {
      ArrayList<int[]> possibleChunkToGen = queuedChunks.get(regionID);
      int[] chunkToGen = possibleChunkToGen.get(0);
      genChunk((regionID[0] * 32) / 16 + chunkToGen[0], (regionID[1] * 32) / 16 + chunkToGen[1]);
      possibleChunkToGen.remove(0);
      queuedChunks.put(regionID, possibleChunkToGen);
    } else if (running) {
      queueRegion();
    }
  }

  private void nextRegion() {
    if (++regionID[0] > (maxX / 32)) {
      regionID[0] = minX / 32;
      regionID[1]++;
      if (regionID[1] > (maxZ / 32)) {
        running = false;
      }
    }
  }

  private void queueRegion() {
    nextRegion();
    for (int x = 0; x < 32; x++) {
      for (int z = 0; z < 32; z++) {
        addToRegionList(regionID, new int[]{x, z});
      }
    }
  }

  private void addToRegionList(int[] regionID, int[] cords) {
    if (queuedChunks.containsKey(regionID)) {
      ArrayList<int[]> chunksToProccess = queuedChunks.get(regionID);
      chunksToProccess.add(cords);
      queuedChunks.put(regionID, chunksToProccess);
    } else {
      ArrayList<int[]> temp = new ArrayList<>();
      temp.add(cords);
      queuedChunks.put(regionID, temp);
    }
  }

  private boolean checkTPS() {
    int currentTPS = (int) WorldUtils.getTPS();
    if (lastTPS == -1) {
      lastTPS = currentTPS;
    }
    if (currentTPS > 5 && lastTPS > 10) {
      lastTPS = currentTPS;
      return true;
    } else {
      lastTPS = currentTPS;
    }
    return false;
  }

  private void notifyPlayers(int type) {
    for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getPlayerList().getPlayers()) {
      if (((PlayerData) DataHelper2
          .get(Keys.PLAYER_DATA, player.getGameProfile().getId().toString())).getRank()
          .hasPermission(Perm.PREGEN)) {
        if (type == 0) {
          ChatHelper.sendMessageTo(player, Local.PREGEN_NOTIFY.replaceAll("#", totalChunks + "")
              .replaceAll("&", "~" + (totalChunks / estTotalChunks) / 5));
        } else if (type == 1) {
          ChatHelper.sendMessageTo(player, Local.PREGEN_FINISHED.replaceAll("#", totalChunks + ""));
        }
      }
    }
  }
}
