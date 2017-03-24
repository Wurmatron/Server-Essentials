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
				if (ChunkHelper.getChunkIndexWithinRegion(ChunkHelper.getChunkFromCords(loc)) >= 0)
						return chunks[ChunkHelper.getChunkIndexWithinRegion(ChunkHelper.getChunkFromCords(loc))]; return null;
		}

		public Claim getClaim(Location loc) {
				return chunks[ChunkHelper.getChunkIndexWithinRegion(ChunkHelper.getChunkFromCords(loc))];
		}

		public void deleteClaim(int index) {
				chunks[index] = null;
		}

		public void addClaim(Location loc, Claim claim) {
				chunks[ChunkHelper.getChunkIndexWithinRegion(loc)] = claim;
		}
}
