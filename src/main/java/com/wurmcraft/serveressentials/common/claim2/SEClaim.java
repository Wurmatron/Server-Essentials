package com.wurmcraft.serveressentials.common.claim2;

import com.wurmcraft.serveressentials.api.json.claim2.Claim;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimOwner;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimPerm;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimResponse;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimType;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SEClaim implements Claim {

  // Config
  private static ClaimType claimType;

  // Unique
  private UUID uniqueID;
  private ClaimOwner owner;
  private LocationWrapper lowerBound;
  private LocationWrapper higherBound;
  private HashMap<UUID, ClaimPerm[]> trusted;

  // Cached
  private AxisAlignedBB bouding;

  // Dynamic
  private List<Entity> currentEntities = new ArrayList<>();
  private List<EntityPlayer> currentPlayers = new ArrayList<>();
  private World world;
  private boolean hasChanged;

  public SEClaim(
      UUID uniqueID,
      ClaimOwner owner,
      LocationWrapper lowerBound,
      LocationWrapper higherBound,
      HashMap<UUID, ClaimPerm[]> trusted) {
    this.uniqueID = uniqueID;
    this.owner = owner;
    this.lowerBound = lowerBound;
    this.higherBound = higherBound;
    this.trusted = trusted;
    createBounding();
  }

  public SEClaim(ClaimOwner owner, LocationWrapper lowerBound, LocationWrapper higherBound) {
    this(
        UUID.fromString(lowerBound.toString() + higherBound.toString()),
        owner,
        lowerBound,
        higherBound,
        new HashMap<>());
  }

  public SEClaim(
      ClaimOwner owner,
      LocationWrapper lowerBound,
      LocationWrapper higherBound,
      HashMap<UUID, ClaimPerm[]> trusted) {
    this(
        UUID.fromString(lowerBound.toString() + higherBound.toString()),
        owner,
        lowerBound,
        higherBound,
        trusted);
  }

  @Override
  public UUID getUniqueID() {
    return uniqueID;
  }

  @Override
  public ClaimOwner getOwner() {
    return owner;
  }

  @Override
  public LocationWrapper getLowerCorner() {
    return lowerBound;
  }

  @Override
  public LocationWrapper getHigherCorner() {
    return higherBound;
  }

  @Override
  public ClaimType getClaimType() {
    return claimType;
  }

  @Override
  public int getArea() {
    return getWidth()[0] * getWidth()[1];
  }

  @Override
  public int getClaimCount() {
    return getArea() * getHeight();
  }

  @Override
  public int[] getWidth() {
    int xWidth = higherBound.getX() - lowerBound.getX();
    int zWidth = higherBound.getZ() - lowerBound.getZ();
    return new int[]{xWidth, zWidth};
  }

  @Override
  public int getHeight() {
    return higherBound.getY() - lowerBound.getY();
  }

  @Override
  public List<Entity> getEntities() {
    return currentEntities;
  }

  @Override
  public List<EntityPlayer> getPlayers() {
    return currentPlayers;
  }

  @Override
  public World getWorld() {
    return world;
  }

  @Override
  public ClaimResponse transferOwner(ClaimOwner owner) {
    if (owner != null
        && owner.getOwner() != null
        && owner.hasPermission(owner.getOwner(), ClaimPerm.valueOf("ALL"))) {
      return ClaimResponse.SUCCESSFUL;
    }
    return ClaimResponse.FAILED;
  }

  @Override
  public HashMap<UUID, ClaimPerm[]> getTrusted() {
    return trusted;
  }

  @Override
  public ClaimResponse removeAllTrusted() {
    if (trusted.size() > 0) {
      trusted.clear();
      return ClaimResponse.SUCCESSFUL;
    }
    return ClaimResponse.FAILED;
  }

  @Override
  public ClaimResponse addTrusted(UUID uuid, ClaimPerm[] perms) {
    if (uuid != null && perms != null) {
      if (perms.length > 0) {
        trusted.put(uuid, perms);
        hasChanged = true;
        return ClaimResponse.SUCCESSFUL;
      } else {
        return ClaimResponse.EMPTY;
      }
    }
    return ClaimResponse.FAILED;
  }

  @Override
  public ClaimResponse addTrusted(UUID uuid) {
    // TODO Default Perms
    return addTrusted(uuid, new ClaimPerm[]{});
  }

  @Override
  public ClaimResponse addTrusted(Object group, ClaimPerm[] perms) {
    if (group != null && perms != null) {
      if (perms.length > 0) {
        if (group instanceof UUID) {
          addTrusted((UUID) group, perms);
          return ClaimResponse.SUCCESSFUL;
        } else if (group instanceof String) {
          addTrusted(UUID.fromString((String) group), perms);
          return ClaimResponse.SUCCESSFUL;
        } else {
          return ClaimResponse.UNSUPPORTED;
        }
      } else {
        return ClaimResponse.EMPTY;
      }
    }
    return ClaimResponse.FAILED;
  }

  @Override
  public ClaimResponse removeTrusted(Object id) {
    if (id != null) {
      if (id instanceof UUID) {
        if (trusted.containsKey(id)) {
          trusted.remove(id);
          hasChanged = true;
          return ClaimResponse.SUCCESSFUL;
        } else {
          return ClaimResponse.EMPTY;
        }
      } else if (id instanceof String) {
        if (trusted.containsKey(UUID.fromString((String) id))) {
          trusted.remove(UUID.fromString((String) id));
          hasChanged = true;
          return ClaimResponse.SUCCESSFUL;
        } else {
          return ClaimResponse.EMPTY;
        }
      }
    }
    return ClaimResponse.FAILED;
  }

  @Override
  public boolean isTrusted(UUID uuid) {
    return trusted.containsKey(uuid);
  }

  @Override
  public boolean isTrusted(UUID uuid, ClaimPerm[] perms) {
    if (isTrusted(uuid) && perms.length > 0) {
      for (ClaimPerm perm : perms) {
        boolean found = false;
        for (ClaimPerm userPerms : trusted.get(uuid)) {
          if (perm == userPerms) {
            found = true;
          }
        }
        if (!found) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean isTrusted(EntityPlayer player) {
    return isTrusted(player.getGameProfile().getId());
  }

  @Override
  public boolean isTrusted(EntityPlayer player, ClaimPerm[] perms) {
    return isTrusted(player.getGameProfile().getId(), perms);
  }

  @Override
  public boolean isTrusted(Object group) {
    if (group instanceof UUID) {
      return isTrusted((UUID) group);
    } else if (group instanceof String) {
      return isTrusted(UUID.fromString((String) group));
    }
    return false;
  }

  @Override
  public boolean isTrusted(Object group, ClaimPerm[] perms) {
    if (group instanceof UUID) {
      return isTrusted((UUID) group, perms);
    } else if (group instanceof String) {
      return isTrusted(UUID.fromString((String) group), perms);
    }
    return false;
  }

  @Override
  public boolean isAdminClaim() {
    return owner.isOwner(UUID.fromString("[ServerEssentials]"));
  }

  @Override
  public boolean isWithin(LocationWrapper loc) {
    return bouding.contains(new Vec3d(loc.getX(), loc.getY(), loc.getZ()));
  }

  @Override
  public ClaimResponse extend(int direction, int amount) {
    if (direction == 0) { // Down
      lowerBound =
          new LocationWrapper(
              lowerBound.getX(),
              lowerBound.getY() - amount,
              lowerBound.getZ(),
              lowerBound.getDim());
      hasChanged = true;
      return ClaimResponse.SUCCESSFUL;
    } else if (direction == 1) { // UP
      higherBound =
          new LocationWrapper(
              higherBound.getX(),
              higherBound.getY() + amount,
              higherBound.getZ(),
              higherBound.getDim());
      hasChanged = true;
      return ClaimResponse.SUCCESSFUL;
    } else if (direction == 2) { // North
      lowerBound =
          new LocationWrapper(
              lowerBound.getX() + amount,
              lowerBound.getY(),
              lowerBound.getZ(),
              lowerBound.getDim());
      createBounding();
      hasChanged = true;
      return ClaimResponse.SUCCESSFUL;
    } else if (direction == 3) { // South
      higherBound =
          new LocationWrapper(
              higherBound.getX() - amount,
              higherBound.getY(),
              higherBound.getZ(),
              higherBound.getDim());
      createBounding();
      hasChanged = true;
      return ClaimResponse.SUCCESSFUL;
    } else if (direction == 4) { // West
      lowerBound =
          new LocationWrapper(
              lowerBound.getX(),
              lowerBound.getY(),
              lowerBound.getZ() + amount,
              lowerBound.getDim());
      createBounding();
      hasChanged = true;
      return ClaimResponse.SUCCESSFUL;
    } else if (direction == 5) { // East
      higherBound =
          new LocationWrapper(
              higherBound.getX(),
              higherBound.getY(),
              higherBound.getZ() + amount,
              higherBound.getDim());
      createBounding();
      hasChanged = true;
      return ClaimResponse.SUCCESSFUL;
    }
    return ClaimResponse.FAILED;
  }

  private void createBounding() {
    bouding =
        new AxisAlignedBB(
            lowerBound.getX(),
            lowerBound.getY(),
            lowerBound.getZ(),
            higherBound.getX(),
            higherBound.getY(),
            higherBound.getZ());
  }

  public void setWorld(World world) {
    this.world = world;
  }

  @Override
  public boolean isDirty() {
    return hasChanged;
  }

  @Override
  public ClaimResponse setDirty(boolean dirty) {
    hasChanged = dirty;
    return ClaimResponse.SUCCESSFUL;
  }
}
