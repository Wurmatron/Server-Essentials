package wurmcraft.serveressentials.common.utils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkProviderServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WorldGenHandler {

	private static Method writeChunkToNBT;
	private static ChunkProviderServer providerServer;
	private static WorldServer server;
	private static final String[] obNames = new String[] {"func_75820_a","writeChunkToNBT"};
	private List <Chunk> preGenChunks;
	private final int chunksPerCycle;
	private int chunkCount;
	private WorldBorder border;
	private boolean negX;
	private boolean negZ;
	private int x;
	private int z;
	private int preX;
	private int preZ;

	public WorldGenHandler (ChunkProviderServer providerServer,WorldServer server,int chunksPerCycle) {
		Class <?>[] cArg = new Class[] {Chunk.class,World.class,NBTTagCompound.class};
		try {
			writeChunkToNBT = AnvilChunkLoader.class.getDeclaredMethod (obNames[0],cArg);
		} catch (NoSuchMethodException e) {
			try {
				writeChunkToNBT = AnvilChunkLoader.class.getDeclaredMethod (obNames[1],cArg);
			} catch (NoSuchMethodException e1) {
				throw new RuntimeException ("Cannot find AnvilChunkLoader.writeChunkToNBT");
			}
		}
			writeChunkToNBT.setAccessible (true);
		this.providerServer = providerServer;
		this.server = server;
		this.preGenChunks = new ArrayList <> ();
		this.chunksPerCycle = chunksPerCycle;
		this.border = providerServer.worldObj.getWorldBorder ();
	}

	private static void write (Chunk chunk) {
		if (writeChunkToNBT != null && chunk != null) {
			AnvilChunkLoader anvil = (AnvilChunkLoader) providerServer.chunkLoader;
			NBTTagCompound levelTag = new NBTTagCompound ();
			NBTTagCompound chunkTag = new NBTTagCompound ();
			chunkTag.setTag ("Level",levelTag);
			try {
				LogHandler.info (writeChunkToNBT + "");
				writeChunkToNBT.invoke (anvil,server,levelTag);
				DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream (server.getChunkSaveLocation (),chunk.xPosition,chunk.zPosition);
				CompressedStreamTools.write (chunkTag,dataoutputstream);
			} catch (IllegalAccessException | IllegalArgumentException | IOException e) {
				e.printStackTrace ();
			} catch (InvocationTargetException e) {
				e.getCause ().printStackTrace ();
			}
		}
	}

	private void genChunk (int x,int z) {
		if (!RegionFileCache.createOrLoadRegionFile (server.getChunkSaveLocation (),x,z).chunkExists (x & 0x1F,z & 0x1F)) {
			Chunk chunk = providerServer.provideChunk (x,z);
			chunk.populateChunk (providerServer,providerServer.chunkGenerator);
			preGenChunks.add (chunk);
			write (chunk);
			chunkCount++;
		} else
			chunkCount++;
	}

	public void cycle () {
		int proccessedChunks = 0;
		for (int c = 0; c < chunksPerCycle; c++) {
			nextChunk ();
			proccessedChunks++;
		}
		LogHandler.info ("Chunks: " + proccessedChunks + " " + chunkCount + " " + x + " " + z);
	}

	// TODO Fix this so it fills the world in correctly
	private void nextChunk () {
		int type = -1;
		++z;
		if (z % 32 == 0)
			type = 0;
		if (x % 32 == 0)
			type = 1;
		preX = x;
		preZ = z;
		handle (type);
		genChunk (x,z);
	}

	private void handle (int type) {
		if (type == 0) {
			x = handleNo (negX,x);
			negX = !negX;
		} else {
			z = handleNo (negZ,z);
			negZ = !negZ;
		}
	}

	private boolean isNeg (int num) {
		return Math.abs (num) != num;
	}

	private int handleNo (boolean neg,int no) {
		if (neg) {
			if (isNeg (no)) {
				no = --no;
			} else {
				int negNo = -no;
				no = --negNo;
			}
		} else {
			int posNo = Math.abs (no);
			no = ++posNo;
		}
		return no;
	}
}
