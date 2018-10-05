package com.wurmcraft.serveressentials.common.claim2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.claim2.Claim;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.util.math.ChunkPos;
import org.apache.commons.io.FileUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class ClaimManager {

  public static NonBlockingHashMap<Integer, ClaimManager> instances = new NonBlockingHashMap<>();
  public static File saveDir = new File(ConfigHandler.saveLocation + File.separator + "Claims");

  private int dimensionID;
  private HashMap<String, List<UUID>> claimLookup = new HashMap<>();
  private HashMap<UUID, Claim> claimData = new HashMap<>();
  private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private List<ChunkPos> quickCheckCache = new ArrayList<>();

  public static ClaimManager getFromDimID(int id) {
    if (instances.get(id) == null) {
      instances.put(id, new ClaimManager(id));
    }
    return instances.get(id);
  }

  public ClaimManager(int dimensionID) {
    this.dimensionID = dimensionID;
    loadAllClaims();
  }

  private void loadAllClaims() {

    File dimensionDir = getDimensionClaimDirectory();
    if (dimensionDir.exists()) {
      for (File file : Objects.requireNonNull(dimensionDir.listFiles())) {
        loadClaim(file);
      }
    } else {
      dimensionDir.mkdirs();
    }
  }

  private void loadClaim(File file) {
    if (file.getName().endsWith(".json")) {
      try {
        Claim claim = GSON.fromJson(new FileReader(file), SEClaim.class);
        claimData.put(claim.getUniqueID(), claim);
        String lowerKey = getLocationIDForLocation(claim.getLowerCorner());
        String higherKey = getLocationIDForLocation(claim.getLowerCorner());
        addClaimToKey(lowerKey, claim);
        if (!lowerKey.equals(higherKey)) {
          addClaimToKey(higherKey, claim);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void addClaimToKey(String key, Claim claim) {
    if (claimData.containsKey(key)) {
      List<UUID> data = claimLookup.get(key);
      data.add(claim.getUniqueID());
    } else {
      List<UUID> data = new ArrayList<>();
      data.add(claim.getUniqueID());
      claimLookup.put(key, data);
    }
  }

  public void saveClaim(Claim claim) {
    File file =
        new File(getDimensionClaimDirectory() + File.separator + claim.getUniqueID() + ".json");
    try {
      file.createNewFile();
      String line = GSON.toJson(claim);
      FileUtils.write(file, line);
      loadClaim(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File getDimensionClaimDirectory() {
    return new File(saveDir + File.separator + dimensionID);
  }

  private void save() {
    for (Claim claim : claimData.values()) {
      if (claim.isDirty()) {
        saveClaim(claim);
      }
    }
  }

  public static void saveAll() {
    for (ClaimManager manager : instances.values()) {
      manager.save();
    }
  }

  private static String getLocationIDForLocation(LocationWrapper loc) {
    return (loc.getX() >> 5) + " " + (loc.getY() >> 5) + " " + (loc.getZ() >> 5);
  }

  private Claim getClaim(UUID uuid) {
    return claimData.get(uuid);
  }

  public static Claim getClaim(ClaimManager manager, LocationWrapper loc) {
    if (manager != null) {
      for (UUID claimID : manager.claimLookup.get(getLocationIDForLocation(loc))) {
        if (manager.getClaim(claimID).isWithin(loc)) {
          return manager.getClaim(claimID);
        }
      }
    }
    return null;
  }

  public static Claim getClaim(ChunkPos quickCheck, LocationWrapper loc) {
    ClaimManager manger = getFromDimID(loc.getDim());
    if (manger.quickCheckCache.contains(quickCheck)) {
      return getClaim(manger, loc);
    }
    return null;
  }

  public static Claim getClaim(LocationWrapper loc) {
    return getClaim(new ChunkPos(loc.getX() >> 4, loc.getZ() >> 4), loc);
  }
}
