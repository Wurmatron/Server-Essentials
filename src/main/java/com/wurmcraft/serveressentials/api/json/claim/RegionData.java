package com.wurmcraft.serveressentials.api.json.claim;

import com.wurmcraft.serveressentials.api.json.user.Location;
import com.wurmcraft.serveressentials.common.claim.ChunkManager;
import net.minecraft.util.math.BlockPos;

public class RegionData {

  private Claim[] claims = new Claim[1024];

  public Claim getClaim(int index) {
    if (index <= claims.length) {
      return claims[index];
    }
    return null;
  }

  public void setClaim(int index, Claim claim) {
    if (index <= claims.length) {
      claims[index] = claim;
    }
  }

  public void addClaim(Location loc, Claim claim) {
        setClaim(ChunkManager.getIndexForClaim(loc), claim);
  }

  public void addClaim(BlockPos pos, Claim claim) {
        setClaim(ChunkManager.getIndexForClaim(pos), claim);
  }
}
