package wurmcraft.serveressentials.common.utils;

import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.Location;

public class RegionClaim {

		private Claim[] chunks = new Claim[1024];

		public RegionClaim() {}

		public Claim getClaim(int index) {
				return chunks[index];
		}

		public Claim getClaim(BlockPos loc) {
				int index = Math.abs(ChunkHelper.getChunkIndexWithinRegion(ChunkHelper.getChunkFromCords(loc)));
				if (index >= 0 && index < 1024)
						return chunks[index];
				return null;
		}

		public Claim getClaim(Location loc) {
				int index = Math.abs(ChunkHelper.getChunkIndexWithinRegion(ChunkHelper.getChunkFromCords(loc)));
				if (index > 0 && chunks.length > index) return chunks[index];
				return null;
		}

		public void deleteClaim(int index) {
				chunks[index] = null;
		}

		public void addClaim(Location loc, Claim claim) {
				chunks[ChunkHelper.getChunkIndexWithinRegion(ChunkHelper.getChunkFromCords(loc))] = claim;
		}
}
