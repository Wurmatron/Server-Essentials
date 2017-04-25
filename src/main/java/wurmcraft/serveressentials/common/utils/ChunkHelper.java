package wurmcraft.serveressentials.common.utils;

import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Location;

public class ChunkHelper {

		public static Location getChunkFromCords(BlockPos pos) {
				return new Location(pos.getX() >> 4, pos.getZ() >> 4);
		}

		public static Location getChunkFromCords(int x, int z) {
				return new Location(x >> 4, z >> 4);
		}

		public static Location getChunkFromCords(Location loc) {
				return new Location(loc.getX() >> 4, loc.getZ() >> 4);
		}

		public static String getSaveNameForRegion(BlockPos pos) {
				Location chunkCords = getChunkFromCords(pos);
				return "c." + (int) Math.floor(chunkCords.getX() / 32.0) + "." + (int) Math.floor(chunkCords.getZ() / 32.0) + ".json";
		}

		public static String getSaveNameForRegion(Location loc) {
				return "c." + loc.getX() + "." + loc.getZ() + ".json";
		}

		public static Location getRegionLocation(BlockPos loc) {
				Location chunkLocation = getChunkFromCords(new Location(loc.getX(),loc.getZ()));
				return new Location((int) Math.floor(chunkLocation.getX() / 32.0), (int) Math.floor(chunkLocation.getZ() / 32.0));
		}

		public static Location getRegionLocation(Location loc) {
				Location chunkLocation = getChunkFromCords(loc);
				return new Location((int) Math.floor(chunkLocation.getX() / 32.0), (int) Math.floor(chunkLocation.getZ() / 32.0));
		}

		public static Location getRegionLocation(Location loc, boolean isConverted) {
				if (isConverted) return new Location((int) Math.floor(loc.getX() / 32.0), (int) Math.floor(loc.getZ() / 32.0));
				else return getRegionLocation(loc);
		}

		public static int getChunkIndexWithinRegion(Location loc) {
					return (loc.getX() * 32) + (loc.getZ() / 16);
		}
}
